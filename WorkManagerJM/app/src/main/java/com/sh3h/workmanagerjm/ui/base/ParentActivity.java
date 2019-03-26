package com.sh3h.workmanagerjm.ui.base;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.condition.TaskCondition;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.preference.UserSession;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.service.SyncConst;
import com.sh3h.workmanagerjm.service.SyncService;
import com.sh3h.workmanagerjm.util.PermissionsChecker;
import com.squareup.otto.Bus;

import java.util.Calendar;

import javax.inject.Inject;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ParentActivity extends BaseActivity implements SwipeBackActivityBase {
    private static final String TAG = "ParentActivity";

    @Inject
    PermissionsChecker permissionsChecker;
    @Inject
    ConfigHelper configHelper;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    DataManager dataManager;
    @Inject
    Bus bus;

    //主程序向子程序发送的账号
    protected static final String ACCOUNT = "account";
    private SwipeBackActivityHelper mHelper;
    protected static TaskCondition taskCondition = new TaskCondition();
    protected static HistoryCondition historyCondition = new HistoryCondition();
    protected static DUData data = new DUData();

    //初始化到那个步骤
    protected static int initStep = ConstantUtil.InitStep.CHECK_PERMISSION;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivityComponent().inject(this);
        if (bundle != null) {
            initStep = bundle.getInt(ConstantUtil.InitStep.INIT_STEP);
            data = bundle.getParcelable(ConstantUtil.Parcel.DUDATA);
            assert data != null;
            String account = preferencesHelper.getUserSession().getAccount();
            taskCondition.setAccount(account);
            historyCondition.setAccount(account);
        } else {
            Intent intent = getIntent();
            String param = intent.getStringExtra(ConstantUtil.TaskEntrance.PARAMES);
            if (!TextUtil.isNullOrEmpty(param)) {
                data.setTaskEntrance(param);
                saveUserInfo(intent);
            }
        }

        setBothAnimation();

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ConstantUtil.Parcel.DUDATA, data);
        outState.putInt(ConstantUtil.InitStep.INIT_STEP, initStep);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if ((v == null) && (mHelper != null)) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public DUData getDUData() {
        return data;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantUtil.RequestCode.CHECK_PERMISSION
                && hasAllPermissionsGranted(grantResults)) {
            initStep = ConstantUtil.InitStep.INIT_CONFIG;
            init();
        } else {
            popupPermissionDialog();
        }
    }

    protected void saveUserInfo(Intent intent) {
        if (intent == null) {
            return;
        }

        String account = intent.getStringExtra(ConstantUtil.TaskEntrance.ACCOUNT);
        if (!TextUtil.isNullOrEmpty(account)) {
            taskCondition.setAccount(account);
            historyCondition.setAccount(account);
        }
        int userId = intent.getIntExtra(ConstantUtil.TaskEntrance.USER_ID, 0);
        String userName = intent.getStringExtra(ConstantUtil.TaskEntrance.USER_NAME);

        UserSession session = preferencesHelper.getUserSession();
        if (!TextUtil.isNullOrEmpty(account) && !TextUtil.EMPTY.equals(account.trim())) {
            session.setAccount(account);
        }
        if (userId > 0) {
            session.setUserId(userId);
        }
        if (!TextUtil.isNullOrEmpty(userName) && !TextUtil.EMPTY.equals(userName.trim())) {
            session.setUserName(userName);
        }
        session.save();

        saveUserConfig();
    }

    protected void hideKeyBoard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null && manager.isActive() && getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void selectTime(int type) {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR); //获取Calendar对象中的年
        int month = calendar.get(Calendar.MONTH);//获取Calendar对象中的月
        int day = calendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, year);
                    calendar1.set(Calendar.MONTH, monthOfYear);
                    calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    selectTimeResult(type, calendar1.getTimeInMillis());
                }, mYear, month, day);
        datePickerDialog.show();
    }

    protected void selectTimeResult(int type, long time) {

    }

    protected void downloadTask(int type) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.DOWNLOAD_ALL_TASK);
        intent.putExtra(SyncConst.TASK_TYPE, type);
        startService(intent);
    }

    protected void syncTask() {
        showProgress(R.string.toast_sync_task);
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.UPLOAD_ALL_TASK);
        startService(intent);
    }

    protected void syncOneTask(long taskId) {
        showProgress(R.string.toast_sync_task);
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.UPLOAD_ONE_WORK);
        intent.putExtra(SyncConst.TASK_ID, taskId);
        startService(intent);
    }

    protected void syncOneMedias(long taskId) {
        showProgress(R.string.toast_sync_task);
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.UPLOAD_ONE_MEDIA);
        intent.putExtra(SyncConst.TASK_ID, taskId);
        startService(intent);
    }

    protected void searchTaskWithCondition(long taskId, int type, int subType, String cardId,
                                           String name, String address, String resolvePerson,
                                           long resolveTime, String gangYinHao,
                                           String phone, boolean fuzzySearch) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.QUERY_TASK_WITH_CONDITION);
        intent.putExtra(SyncConst.TASK_ID, taskId);
        intent.putExtra(SyncConst.TASK_TYPE, type);
        intent.putExtra(SyncConst.TASK_SUB_TYPE, subType);
        intent.putExtra(SyncConst.CARD_ID, cardId);
        intent.putExtra(SyncConst.NAME, name);
        intent.putExtra(SyncConst.ADDRESS, address);
        intent.putExtra(SyncConst.PHONE, phone);
        intent.putExtra(SyncConst.GANG_YIN_HAO, gangYinHao);
        intent.putExtra(SyncConst.RESOLVE_PERSON, resolvePerson);
        intent.putExtra(SyncConst.RESOLVE_TIME, resolveTime);
        intent.putExtra(SyncConst.FUZZY_SEARCH, fuzzySearch);
        startService(intent);
    }

    //拆表工单和停水工单增加“欠费”标识，要求从营收系统实时同步欠费状态。
    protected void downloadMeterCard() {
        DUTask task = data.getDuTask();
        if (task == null) {
            return;
        }

        if (task.getType() != ConstantUtil.WorkType.TASK_BIAOWU) {
            return;
        }

        int subType = task.getSubType();
        if (subType != ConstantUtil.WorkSubType.SPLIT_METER
                && subType != ConstantUtil.WorkSubType.STOP_METER) {
            return;
        }

        String cardId = task.getCardId();
        if (TextUtil.isNullOrEmpty(cardId)) {
            return;
        }

        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.DOWNLOAD_METER_CARD);
        intent.putExtra(SyncConst.CARD_ID, cardId);
        startService(intent);
    }

    protected void downloadDispatch(int type, int subType, String station, String volume,
                                    long beginTime, long endTime) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.DOWNLOAD_DISPATCH);
        intent.putExtra(SyncConst.TASK_TYPE, type);
        intent.putExtra(SyncConst.TASK_SUB_TYPE, subType);
        intent.putExtra(SyncConst.STATION, station);
        intent.putExtra(SyncConst.VOLUME, volume);
        intent.putExtra(SyncConst.BEGIN_TIME, beginTime);
        intent.putExtra(SyncConst.END_TIME, endTime);
        startService(intent);
    }

    protected void downloadStatistics(long beginTime, long endTime) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.DOWNLOAD_STATISTICS);
        intent.putExtra(SyncConst.BEGIN_TIME, beginTime);
        intent.putExtra(SyncConst.END_TIME, endTime);
        startService(intent);
    }

    protected void downloadVerify() {
        if (data == null) {
            return;
        }

        if (!ConstantUtil.TaskEntrance.VERIFY.equals(data.getTaskEntrance())) {
            return;
        }

        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_OPERATE, SyncConst.DOWNLOAD_VERIFY);
        startService(intent);
    }

    private void init() {
        switch (initStep) {
            case ConstantUtil.InitStep.CHECK_PERMISSION:
                checkPermissions();
                break;
            case ConstantUtil.InitStep.INIT_CONFIG:
                initConfig();
                break;
            case ConstantUtil.InitStep.CLEAN_DATA:
                cleanData();
                break;
            case ConstantUtil.InitStep.UPDATE_UPLOAD_FLAG:
                updateUploadFlag();
                break;
            case ConstantUtil.InitStep.DOWNLOAD_WORD:
                downloadWord();
                break;
            case ConstantUtil.InitStep.DOWNLOAD_MATERIAL:
                downloadMaterial();
                break;
            case ConstantUtil.InitStep.DOWNLOAD_PERSON:
                downLoadPersons();
                break;
            default:
                LogUtil.i(TAG, "---初始化结束--");
                break;
        }
    }

    private void cleanData() {
        dataManager.cleanData()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        initStep = ConstantUtil.InitStep.UPDATE_UPLOAD_FLAG;
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        initStep = ConstantUtil.InitStep.UPDATE_UPLOAD_FLAG;
                        init();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    private void updateUploadFlag() {
        dataManager.updateUploadFlag()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        initStep = ConstantUtil.InitStep.DOWNLOAD_WORD;
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        initStep = ConstantUtil.InitStep.DOWNLOAD_WORD;
                        init();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    private void downloadWord() {
        dataManager.downLoadWords()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        initStep = ConstantUtil.InitStep.DOWNLOAD_MATERIAL;
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        initStep = ConstantUtil.InitStep.DOWNLOAD_MATERIAL;
                        ApplicationsUtil.showMessage(getApplicationContext(),
                                R.string.toast_download_word_error);
                        init();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    private void downloadMaterial() {
        dataManager.downLoadMaterials()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        initStep = ConstantUtil.InitStep.DOWNLOAD_PERSON;
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        initStep = ConstantUtil.InitStep.DOWNLOAD_PERSON;
                        ApplicationsUtil.showMessage(getApplicationContext(),
                                R.string.toast_download_material_error);
                        init();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    private void downLoadPersons() {
        dataManager.downLoadPersons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        initStep = ConstantUtil.InitStep.INIT_OVER;
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        initStep = ConstantUtil.InitStep.INIT_OVER;
                        init();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsChecker.lacksPermissions(PERMISSIONS)) {
            requestPermissions(PERMISSIONS, ConstantUtil.RequestCode.CHECK_PERMISSION);
        } else {
            initStep = ConstantUtil.InitStep.INIT_CONFIG;
            init();
        }
    }

    //是否已经取得所有权限
    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    //没有赋予相关权限，退出程序
    private void popupPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_info)
                .setMessage(R.string.text_lack_permissions)
                .setCancelable(false)
                .setPositiveButton(R.string.text_ok, (dialogInterface, i) ->
                        System.exit(0)
                )
                .setNegativeButton(R.string.text_cancel, (dialogInterface, i) ->
                        System.exit(0)
                )
                .create()
                .show();
    }

    private void initConfig() {
        configHelper.initDefaultConfigs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "---initConfig onCompleted---");
                        dataManager.initLogger();
                        initStep = ConstantUtil.InitStep.CLEAN_DATA;
                        initConfigEnd();
                        startOrBindService();
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "---initConfig onError---" + e.getMessage());
                        System.exit(0);
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Log.i(TAG, "---initConfig onNext---");
                    }
                });
    }

    //初始化结束后加载数据
    protected void initConfigEnd() {

    }

    private void startOrBindService() {
        MainApplication.get(getApplicationContext()).startOrBindService();
    }

    private void saveUserConfig() {
        dataManager.saveUserConfig().
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "---saveUserConfig onCompleted---");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "---saveUserConfig onError---");
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Log.i(TAG, "---saveUserConfig onError---");
                    }
                });
    }

}
