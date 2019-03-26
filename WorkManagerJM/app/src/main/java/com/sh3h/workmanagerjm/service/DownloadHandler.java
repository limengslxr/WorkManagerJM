package com.sh3h.workmanagerjm.service;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.retrofit.DispatchInfo;
import com.sh3h.dataprovider.data.entity.retrofit.MeterCard;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsInfo;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsResult;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.squareup.otto.Bus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.schedulers.Schedulers;


class DownloadHandler extends BaseHandler {
    private static final String TAG = "DownloadHandler";
    private volatile boolean downloadMediaOver = true;
    private List<SyncMessage> medias = new ArrayList<>();

    DownloadHandler(MainApplication mainApplication,
                    DataManager dataManager,
                    ConfigHelper configHelper,
                    PreferencesHelper preferencesHelper,
                    EventPosterHelper eventPosterHelper,
                    Bus bus) {
        super(mainApplication, dataManager, configHelper, preferencesHelper, eventPosterHelper, bus);
    }

    @Override
    protected boolean process(SyncMessage syncMessage) {
        if (!super.process(syncMessage)) {
            return false;
        }

        switch (syncMessage.getOperate()) {
            case SyncConst.DOWNLOAD_ALL_TASK:
                downloadAllTask(syncMessage.getType());
                break;
            case SyncConst.DOWNLOAD_MEDIAS:
                medias.add(syncMessage);
                downloadMedias();
                break;
            case SyncConst.DOWNLOAD_METER_CARD:
                downloadMeterCard(syncMessage.getCardId());
                break;
            case SyncConst.DOWNLOAD_DISPATCH:
                downloadDispatch(syncMessage);
                break;
            case SyncConst.DOWNLOAD_STATISTICS:
                downloadStatistics(syncMessage);
                break;
            case SyncConst.DOWNLOAD_VERIFY:
                downloadVerify();
                break;
            default:
                break;
        }
        return true;
    }

    private void downloadAllTask(int taskType) {
        mSubscription = mDataManager.downLoadWork(taskType, 1, 1000).
                subscribeOn(Schedulers.io()).
                subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "----downloadAllTask onCompleted----");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "---downloadAllTask---" + e.getMessage());
                        downloadWorkEnd(false);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        downloadWorkEnd(aBoolean);
                    }
                });
    }

    private void downloadMedias() {
        if (!downloadMediaOver) {
            return;
        }
        downloadMediaOver = false;

        if (medias.size() == 0) {
            downloadMediaOver = true;
            return;
        }

        SyncMessage message = medias.remove(0);

        String fileName = message.getFileName();
        String fileUrl = message.getUrl();
        int type = message.getFileType();
        if (TextUtil.isNullOrEmpty(fileName) || TextUtil.isNullOrEmpty(fileUrl)) {
            downloadMediaOver = true;
            downloadMedias();
            return;
        }

        File file;
        switch (type) {
            case ConstantUtil.FileType.FILE_PICTURE:
                file = new File(mConfigHelper.getImageFolderPath(), fileName);
                break;
            case ConstantUtil.FileType.FILE_VOICE:
                file = new File(mConfigHelper.getSoundFolderPath(), fileName);
                break;
            default:
                file = null;
                break;
        }

        if (file != null && file.exists()) {
            downloadMediaOver = true;
            downloadMedias();
            return;
        }

        mDataManager.downloadMedia(fileUrl, fileName, type)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "----downloadMedias onCompleted----");
                        downloadMediaOver = true;
                        downloadMedias();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG, "----downloadMedias onError----");
                        downloadMediaOver = true;
                        downloadMedias();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.d(TAG, fileName + "----" + fileUrl);
                    }
                });

    }

    private void downloadMeterCard(String cardId) {
        if (TextUtil.isNullOrEmpty(cardId)) {
            return;
        }

        mDataManager.downloadMeterCard(cardId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<MeterCard>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        downloadMeterCard(false, cardId, null);
                    }

                    @Override
                    public void onNext(MeterCard meterCard) {
                        downloadMeterCard(true, cardId, meterCard);
                    }
                });
    }

    private void downloadDispatch(SyncMessage syncMessage) {
        if (syncMessage == null) {
            return;
        }

        DispatchInfo info = new DispatchInfo();
        info.setType(syncMessage.getType());
        info.setSubType(syncMessage.getSubType());
        info.setStation(syncMessage.getStation());
        info.setVolume(syncMessage.getVolume());
        info.setBeginTime(syncMessage.getBeginTime());
        info.setEndTime(syncMessage.getEndTime());
        LogUtil.i(TAG, String.format(Locale.CHINA, "download dispatch list param:%d-%d-%s-%d-%d",
                syncMessage.getType(), syncMessage.getSubType(), syncMessage.getStation(),
                syncMessage.getBeginTime(), syncMessage.getEndTime()));

        mDataManager.downloadDispatch(info)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, String.format(Locale.CHINA,
                                "download dispatch list onError:%s", e.toString()));
                        getDispatch(false, null);
                    }

                    @Override
                    public void onNext(List<DUData> duData) {
                        getDispatch(true, duData);
                    }
                });
    }

    private void downloadStatistics(SyncMessage syncMessage) {
        if (syncMessage == null) {
            return;
        }

        StatisticsInfo info = new StatisticsInfo();
        info.setAccount(mDataManager.getAccount());
        info.setBeginTime(syncMessage.getBeginTime());
        info.setEndTime(syncMessage.getEndTime());

        mDataManager.downloadStatistics(info)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<StatisticsResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getStatistics(false, null);
                    }

                    @Override
                    public void onNext(List<StatisticsResult> statisticsResults) {
                        getStatistics(true, statisticsResults);
                    }
                });
    }

    private void downloadVerify() {
        mDataManager.downloadVerify()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getVerify(false, null);
                    }

                    @Override
                    public void onNext(List<DUData> data) {
                        getVerify(true, data);
                    }
                });
    }

    private void downloadWorkEnd(boolean result) {
        UIBusEvent.DownloadTask downloadTask = new UIBusEvent.DownloadTask();
        downloadTask.setMessage(result ? R.string.toast_download_task_success :
                R.string.toast_download_task_fail);
        downloadTask.setSuccess(result);
        mEventPosterHelper.postEventSafely(downloadTask);
    }

    private void downloadMeterCard(boolean result, String cardId, MeterCard meterCard) {
        UIBusEvent.DownloadMeterCard downloadMeterCard = new UIBusEvent.DownloadMeterCard();
        downloadMeterCard.setSuccess(result);
        downloadMeterCard.setCardId(cardId);
        downloadMeterCard.setMeterCard(meterCard);
        mEventPosterHelper.postEventSafely(downloadMeterCard);
    }

    private void getDispatch(boolean result, List<DUData> data) {
        UIBusEvent.DispatchTask dispatchTask = new UIBusEvent.DispatchTask();
        dispatchTask.setMessage(result ? R.string.toast_download_dispatch_success :
                R.string.toast_download_dispatch_fail);
        dispatchTask.setSuccess(result);
        dispatchTask.setList(data);
        mEventPosterHelper.postEventSafely(dispatchTask);
    }

    private void getStatistics(boolean result, List<StatisticsResult> data) {
        UIBusEvent.StatisticsTask dispatchTask = new UIBusEvent.StatisticsTask();
        dispatchTask.setMessage(result ? R.string.toast_download_statistics_success :
                R.string.toast_download_statistics_fail);
        dispatchTask.setSuccess(result);
        dispatchTask.setList(data);
        mEventPosterHelper.postEventSafely(dispatchTask);
    }

    private void getVerify(boolean result, List<DUData> data) {
        UIBusEvent.VerifyTask verifyTask = new UIBusEvent.VerifyTask();
        verifyTask.setMessage(result ? R.string.toast_download_verify_success :
                R.string.toast_download_verify_fail);
        verifyTask.setSuccess(result);
        verifyTask.setList(data);
        mEventPosterHelper.postEventSafely(verifyTask);
    }

}
