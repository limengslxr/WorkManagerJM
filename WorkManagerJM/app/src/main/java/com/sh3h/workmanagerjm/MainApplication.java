package com.sh3h.workmanagerjm;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.retrofit.DeleteTask;
import com.sh3h.dataprovider.data.entity.retrofit.DeleteVerifyTask;
import com.sh3h.dataprovider.data.entity.retrofit.DownloadTaskResult;
import com.sh3h.dataprovider.data.entity.retrofit.MessageUpdate;
import com.sh3h.dataprovider.data.entity.retrofit.MultipleTask;
import com.sh3h.dataprovider.data.entity.retrofit.PushLeader;
import com.sh3h.dataprovider.data.entity.retrofit.TransformStation;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.xml.XmlHelper;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.TransformUtil;
import com.sh3h.ipc.IMainService;
import com.sh3h.ipc.IRemoteServiceCallback;
import com.sh3h.ipc.location.MyLocation;
import com.sh3h.ipc.module.MyModule;
import com.sh3h.localprovider.entity.Task;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.injection.component.ApplicationComponent;
import com.sh3h.workmanagerjm.injection.component.DaggerApplicationComponent;
import com.sh3h.workmanagerjm.injection.module.ApplicationModule;
import com.sh3h.workmanagerjm.service.SyncService;
import com.sh3h.workmanagerjm.ui.list.ListActivity;
import com.sh3h.workmanagerjm.util.SystemUtil;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

//import android.support.multidex.MultiDex;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    public static final String HOST_SERVICE_NAME = "com.sh3h.mainshell.service.HostService";
    public static final String BINDING_NAME = "bindingName";

    @Inject
    Bus mEventBus;

    @Inject
    DataManager mDataManager;

    @Inject
    ConfigHelper mConfigHelper;

    @Inject
    XmlHelper mXmlHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    EventPosterHelper mEventPosterHelper;

    private ApplicationComponent mApplicationComponent;

    private static final int MY_MSG_LOCATION = 1;
    private static final int MY_MSG_MODULE = 2;
    private static final int MY_MSG_EXIT = 3;
    private long timeError;
    private Handler mHandler = new MyHandler(this);
    private IMainService iMainService;
    private static MyLocation myLocation;
    private List<Integer> downloadTypes;

    private IRemoteServiceCallback mCallBack = new IRemoteServiceCallback.Stub() {
        @Override
        public void locationChanged(MyLocation myLocation) throws RemoteException {
            MainApplication.myLocation = myLocation;
        }

        @Override
        public void moduleChanged(MyModule myModule) throws RemoteException {
            mHandler.sendMessage(mHandler.obtainMessage(MY_MSG_MODULE, myModule));
        }

        @Override
        public void exitSystem() throws RemoteException {
            mHandler.sendMessage(mHandler.obtainMessage(MY_MSG_EXIT));
        }
    };

    private ServiceConnection mainConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMainService = IMainService.Stub.asInterface(iBinder);
            try {
                iMainService.registerCallback(mCallBack);
                iMainService.addPid(android.os.Process.myPid());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName){
            iMainService = null;
        }
    };

    public MainApplication() {
        mApplicationComponent = null;
        timeError = 0;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);

        mEventBus.register(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public ConfigHelper getConfigHelper() {
        return mConfigHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public void setTimeError(long timeError) {
        this.timeError = timeError;
    }

    public long getCurrentTime() {
        long time = new Date().getTime();
        return time + timeError;
    }

    public Date getCurrentDate() {
        return new Date(getCurrentTime());
    }

    private static class MyHandler extends Handler {
        private MainApplication mMainApplication;

        MyHandler(MainApplication mainApplication) {
            mMainApplication = mainApplication;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MY_MSG_LOCATION:
                    break;
                case MY_MSG_MODULE:
                    analyzeData(msg);
                    break;
                case MY_MSG_EXIT:
                    exit();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        /**
         * for example
         * {
         * "packageName": "com.sh3h.meterreading",
         * "activityName": "com.sh3h.meterreading.ui.xxx.xxx",
         * "data" : [
         * "count#0",
         * "mqtt#xxxxxxx"
         * ]
         * }
         */

        private void analyzeData(Message message) {
            Object obj = message.obj;
            if (!(obj instanceof MyModule)) {
                return;
            }

            MyModule myModule = (MyModule) message.obj;
            String info = myModule.getInfo();
            if (TextUtil.isNullOrEmpty(info)) {
                return;
            }

            JSONObject object;
            String packageName;
            JSONArray dataArray;
            try {
                object = new JSONObject(info);
                packageName = object.getString("packageName");
                dataArray = object.getJSONArray("data");

                PackageInfo packageInfo = SystemUtil.getPackageInfo(mMainApplication);
                if (packageInfo == null) {
                    return;
                }

                if (packageInfo.packageName.equals(packageName)) {
                    return;
                }

                for (int i = 0; i < dataArray.length(); i++) {
                    String str = dataArray.getString(i);
                    if (str.startsWith(MyModule.PUSH_MESSAGE) && str.contains(MyModule.SEPARATOR)) {
                        int index = str.indexOf(MyModule.SEPARATOR);
                        parseMqtt(str.substring(index + 1));
                    }
                    //图片质量
                    if (str.startsWith(MyModule.PHOTO_QUALITY) && str.contains(MyModule.SEPARATOR)) {
                        int index = str.indexOf(MyModule.SEPARATOR);
                        parsePicQuality(str.substring(index + 1));
                    }
                    //清除缓存
                    if (str.startsWith(MyModule.CLEAR_CACHE) && str.contains(MyModule.SEPARATOR)) {
                        int index = str.indexOf(MyModule.SEPARATOR);
                        clearHistory(str.substring(index + 1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void clearHistory(String str) {
            if (!String.valueOf(true).equals(str)) {
                return;
            }
            mMainApplication.mDataManager.clearHistory().
                    subscribeOn(Schedulers.io()).
                    subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                            LogUtil.i(TAG, "clearHistory completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.i(TAG, "clearHistory onError");
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            LogUtil.i(TAG, "clearHistory onNext");
                            if (aBoolean) {
                                mMainApplication.clearCount();
                                mMainApplication.showMessage();
                            }
                        }
                    });
        }

        private void parsePicQuality(String str) {
            mMainApplication.mDataManager.savePicQuality(str).
                    subscribeOn(Schedulers.io()).
                    subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                            LogUtil.i(TAG, "parsePicQuality completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.i(TAG, "parsePicQuality onError");
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            LogUtil.i(TAG, "parsePicQuality onNext");
                        }
                    });
        }

        private void parseMqtt(String info) throws Exception {
            if (TextUtil.isNullOrEmpty(info)) {
                return;
            }

            JSONObject jsonObject = new JSONObject(info);
            if (jsonObject.isNull(ConstantUtil.Message.TYPE)
                    || jsonObject.isNull(ConstantUtil.Message.CONTENT)) {
                return;
            }

            int type = jsonObject.optInt(ConstantUtil.Message.TYPE);
            JSONArray jsonArray = jsonObject.optJSONArray(ConstantUtil.Message.CONTENT);
            Observable<Boolean> observable = null;
            Gson gson = new Gson();
            PreferencesHelper helper = mMainApplication.getPreferencesHelper();
            String account = helper.getUserSession().getAccount();
            List<DeleteTask> deleteTasks;
            UIBusEvent.DeleteTasks deleteTask;
            switch (type){
                case ConstantUtil.Message.SINGLE_NEW_TASK:
                    List<DownloadTaskResult> entities = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<DownloadTaskResult>>() {}.getType());
                    List<Task> tasks = new ArrayList<>();
                    for (DownloadTaskResult resultEntity : entities) {
                        tasks.add(TransformUtil.transform(resultEntity,account));
                    }
                    observable = mMainApplication.mDataManager.insertTasks(tasks);
                    break;
                case ConstantUtil.Message.UPDATE_STATE:
                    List<MessageUpdate> messageUpdates = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<MessageUpdate>>() {}.getType());
                    UIBusEvent.UpdateTaskState updateTaskState = new UIBusEvent.UpdateTaskState();
                    updateTaskState.setUpdateStates(messageUpdates);
                    mMainApplication.mEventPosterHelper.postEventSafely(updateTaskState);
                    observable = mMainApplication.mDataManager.updateTaskState(messageUpdates);
                    break;
                case ConstantUtil.Message.APPLY_PUSH_LEADER:
                    List<PushLeader> pushLeaders = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<PushLeader>>() {}.getType());
                    mMainApplication.mEventPosterHelper.postEventSafely(new UIBusEvent.ApplyVerify());
                    //推送给领导，仅仅只是提示一下
                    observable = Observable.just(true);
                    break;
                case ConstantUtil.Message.DELETE_TASK:
                case ConstantUtil.Message.DELETE_BACKGROUND_CANCEL:
                case ConstantUtil.Message.DELETE_REJECT_TASK:
                case ConstantUtil.Message.DELETE_HANDLER_TASK:
                    deleteTasks = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<DeleteTask>>() {}.getType());
                    deleteTask = new UIBusEvent.DeleteTasks(deleteTasks);
                    mMainApplication.mEventPosterHelper.postEventSafely(deleteTask);
                    observable = mMainApplication.mDataManager.deleteTask(deleteTasks);
                    break;
                case ConstantUtil.Message.DELETE_ALREADY_DISPATCH:
                case ConstantUtil.Message.DELETE_ALREADY_TRANSFORM:
                    deleteTasks = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<DeleteTask>>() {}.getType());
                    deleteTask = new UIBusEvent.DeleteTasks(ConstantUtil.TaskEntrance.DISPATCH, deleteTasks);
                    mMainApplication.mEventPosterHelper.postEventSafely(deleteTask);
                    observable = Observable.just(true);
                    break;
                case ConstantUtil.Message.DELETE_ALREADY_VERIFY:
                    List<DeleteVerifyTask> deleteVerifyTasks = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<DeleteVerifyTask>>() {}.getType());
                    UIBusEvent.DeleteVerifyTasks deleteVerifyTask = new UIBusEvent.DeleteVerifyTasks(deleteVerifyTasks);
                    mMainApplication.mEventPosterHelper.postEventSafely(deleteVerifyTask);
                    observable = Observable.just(true);
                    break;
                case ConstantUtil.Message.MULTIPLE_NEW_TASK:
                    List<MultipleTask> multipleTasks = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<MultipleTask>>() {}.getType());
                    UIBusEvent.MultipleTask multipleTask = new UIBusEvent.MultipleTask();
                    List<Integer> types = new ArrayList<>();
                    int downloadType;
                    for (MultipleTask task : multipleTasks){
                        downloadType = task.getType();
                        if (!types.contains(downloadType)){
                            types.add(downloadType);
                        }
                    }
                    multipleTask.setTypes(types);
                    mMainApplication.setDownloadTypes(types);
                    mMainApplication.mEventPosterHelper.postEventSafely(multipleTask);
                    observable = Observable.just(true);
                    break;
                case ConstantUtil.Message.TRANSFORM_STATION:
                    List<TransformStation> transformStations = gson.fromJson(jsonArray.toString(),
                            new TypeToken<ArrayList<TransformStation>>() {}.getType());
                    mMainApplication.mEventPosterHelper.postEventSafely(new UIBusEvent.TransformStation());
                    //推送给领导，仅仅只是提示一下
                    observable = Observable.just(true);
                    break;
                default:
                    observable = null;
                    break;
            }

            if (observable == null) {
                return;
            }

            observable.subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                            LogUtil.i(TAG, "parseMqtt completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(TAG, e.toString());
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            LogUtil.i(TAG, "parseMqtt onNext:" + aBoolean);
                        }
                    });
        }

        private void exit() {
            mMainApplication.destroy();
        }
    }

    /**
     * ORDERTASKBW,METER_INSTALL,CALL_PAY
     */
    private void clearCount() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(MyModule.PACKAGE_NAME, getApplicationContext().getPackageName());
            jsonObject.put(MyModule.ACTIVITY_NAME,
                    ListActivity.class.getName() + "#" + ConstantUtil.TaskEntrance.ORDERTASKBW);
            JSONArray array = new JSONArray();
            array.put(MyModule.COUNT + "#" + ConstantUtil.EMPTY_DATA);
            jsonObject.put(MyModule.DATA, array);

            JSONObject jsonObjectInStall = new JSONObject();
            jsonObjectInStall.put(MyModule.PACKAGE_NAME, getApplicationContext().getPackageName());
            jsonObjectInStall.put(MyModule.ACTIVITY_NAME,
                    ListActivity.class.getName() + "#" + ConstantUtil.TaskEntrance.METER_INSTALL);
            JSONArray arrayInstall = new JSONArray();
            arrayInstall.put(MyModule.COUNT + "#" + ConstantUtil.EMPTY_DATA);
            jsonObjectInStall .put(MyModule.DATA, arrayInstall);

            JSONObject jsonObjectCallPay = new JSONObject();
            jsonObjectCallPay.put(MyModule.PACKAGE_NAME, getApplicationContext().getPackageName());
            jsonObjectCallPay.put(MyModule.ACTIVITY_NAME,
                    ListActivity.class.getName() + "#" + ConstantUtil.TaskEntrance.CALL_PAY);
            JSONArray arrayCallPay = new JSONArray();
            arrayCallPay.put(MyModule.COUNT + "#" + ConstantUtil.EMPTY_DATA);
            jsonObjectCallPay.put(MyModule.DATA, arrayInstall);

            MyModule myModule = new MyModule(jsonObject.toString());
            MyModule myModuleInstall=new MyModule(jsonObjectInStall.toString());
            MyModule myModuleCallPay=new MyModule(jsonObjectCallPay.toString());

            setMyModule(myModule);
            setMyModule(myModuleInstall);
            setMyModule(myModuleCallPay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage() {
        ApplicationsUtil.showMessage(MainApplication.this, R.string.clear_completed);
    }


    private void destroy() {
        stopOrUnbindService();
        System.exit(0);
    }

    private void stopOrUnbindService() {

        if (SyncService.isRunning(this)) {
            stopService(SyncService.getStartIntent(this));
        }
        if (iMainService != null) {
            Log.i(TAG, "---unbindHostService---2");
            unbindService(mainConnection);
            iMainService = null;
        }
    }

    public void setMyModule(MyModule myModule) {
        if (myModule == null || iMainService == null) {
            return;
        }
        try {
            iMainService.setMyModule(myModule);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startOrBindService() {
        if (iMainService == null) {
            Intent intent = new Intent(HOST_SERVICE_NAME);
            intent = createExplicitFromImplicitIntent(this, intent);
            if (intent != null) {
                intent.putExtra(BINDING_NAME, IMainService.class.getName());
                bindService(intent, mainConnection, BIND_AUTO_CREATE);
            }
        }
    }

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public List<Integer> getDownloadTypes() {
        return downloadTypes;
    }

    public void setDownloadTypes(List<Integer> downloadTypes) {
        this.downloadTypes = downloadTypes;
    }

    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     * <p>
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     * <p>
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     *
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    private static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

}
