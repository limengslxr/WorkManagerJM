package com.sh3h.workmanagerjm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.util.AndroidComponentUtil;
import com.squareup.otto.Bus;

import javax.inject.Inject;


public class SyncService extends Service {
    private static final String TAG = "SyncService";

    @Inject
    DataManager mDataManager;

    @Inject
    ConfigHelper mConfigHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @Inject
    Bus mBus;

    private DownloadHandler mDownloadHandler;
    private UploadHandler mUploadHandler;
    private QueryHandler mQueryHandler;
    private OtherHandler mOtherHandler;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, SyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.i(TAG, "onCreate");
        MainApplication.get(this).getComponent().inject(this);
        BaseHandler.init();
        mDownloadHandler = new DownloadHandler(MainApplication.get(this), mDataManager,
                mConfigHelper, preferencesHelper, mEventPosterHelper, mBus);
        mUploadHandler = new UploadHandler(MainApplication.get(this), mDataManager,
                mConfigHelper, preferencesHelper, mEventPosterHelper, mBus);
        mQueryHandler = new QueryHandler(MainApplication.get(this), mDataManager,
                mConfigHelper, preferencesHelper, mEventPosterHelper, mBus);
        mOtherHandler = new OtherHandler(MainApplication.get(this), mDataManager,
                mConfigHelper, preferencesHelper, mEventPosterHelper, mBus);
        mDownloadHandler.start();
        mUploadHandler.start();
        mQueryHandler.start();
        mOtherHandler.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        int operate = intent == null ? 0 : intent.getIntExtra(SyncConst.SYNC_OPERATE, 0);
        if (!NetworkUtil.isNetworkConnected(this)) {
            ApplicationsUtil.showMessage(this, R.string.text_network_not_connect);
            stopSelf(startId);
            UIBusEvent.NetworkNotConnect connect = new UIBusEvent.NetworkNotConnect();
            connect.setOperate(operate);
            mEventPosterHelper.postEventSafely(connect);
            LogUtil.i(TAG, "---onStartCommand---network isn't connected");
            return START_NOT_STICKY;
        }

        try {
            long taskId = intent.getLongExtra(SyncConst.TASK_ID, 0);
            int taskType = intent.getIntExtra(SyncConst.TASK_TYPE, 0);
            int subType = intent.getIntExtra(SyncConst.TASK_SUB_TYPE, 0);
            String cardId = intent.getStringExtra(SyncConst.CARD_ID);
            String name = intent.getStringExtra(SyncConst.NAME);
            String address = intent.getStringExtra(SyncConst.ADDRESS);
            String phone = intent.getStringExtra(SyncConst.PHONE);
            String gangYinHao = intent.getStringExtra(SyncConst.GANG_YIN_HAO);
            String resolvePerson = intent.getStringExtra(SyncConst.RESOLVE_PERSON);
            long resolveTime = intent.getLongExtra(SyncConst.RESOLVE_TIME, 0);
            boolean fuzzySearch = intent.getBooleanExtra(SyncConst.FUZZY_SEARCH, false);

            String fileName = intent.getStringExtra(SyncConst.FILE_NAME);
            String fileUrl = intent.getStringExtra(SyncConst.FILE_URL);
            int fileType = intent.getIntExtra(SyncConst.FILE_TYPE, 0);

            String station = intent.getStringExtra(SyncConst.STATION);
            String volume = intent.getStringExtra(SyncConst.VOLUME);
            long beginTime = intent.getLongExtra(SyncConst.BEGIN_TIME, 0L);
            long endTime = intent.getLongExtra(SyncConst.END_TIME, 0L);

            SyncMessage message = new SyncMessage(operate);
            switch (operate) {
                case SyncConst.NONE:
                    LogUtil.i(TAG, "onStartCommand none");
                    break;
                case SyncConst.DOWNLOAD_ALL_TASK:
                    message.setType(taskType);
                    mDownloadHandler.post(message);
                    break;
                case SyncConst.UPLOAD_ONE_WORK:
                    message.setTaskId(taskId);
                    mUploadHandler.post(message);
                    break;
                case SyncConst.UPLOAD_ONE_MEDIA:
                    message.setTaskId(taskId);
                    mUploadHandler.post(message);
                    break;
                case SyncConst.UPLOAD_ALL_TASK:
                    mUploadHandler.post(message);
                    break;
                case SyncConst.QUERY_TASK_WITH_CONDITION:
                    message.setTaskId(taskId);
                    message.setType(taskType);
                    message.setSubType(subType);
                    message.setCardId(cardId);
                    message.setCardName(name);
                    message.setAddress(address);
                    message.setPhone(phone);
                    message.setGangYinHao(gangYinHao);
                    message.setResolvePerson(resolvePerson);
                    message.setResolveTime(resolveTime);
                    message.setFuzzySearch(fuzzySearch);
                    mQueryHandler.process(message);
                    break;
                case SyncConst.DOWNLOAD_MEDIAS:
                    message.setFileName(fileName);
                    message.setUrl(fileUrl);
                    message.setFileType(fileType);
                    mDownloadHandler.post(message);
                    break;
                case SyncConst.DOWNLOAD_METER_CARD:
                    message.setCardId(cardId);
                    mDownloadHandler.post(message);
                    break;
                case SyncConst.DOWNLOAD_DISPATCH:
                    message.setType(taskType);
                    message.setSubType(subType);
                    message.setStation(station);
                    message.setVolume(volume);
                    message.setBeginTime(beginTime);
                    message.setEndTime(endTime);
                    mDownloadHandler.post(message);
                    break;
                case SyncConst.DOWNLOAD_STATISTICS:
                    message.setBeginTime(beginTime);
                    message.setEndTime(endTime);
                    mDownloadHandler.post(message);
                    break;
                case SyncConst.DOWNLOAD_VERIFY:
                    mDownloadHandler.post(message);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            mDownloadHandler.stop();
            mUploadHandler.stop();
            mQueryHandler.stop();
            mOtherHandler.stop();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseHandler.destroy();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
