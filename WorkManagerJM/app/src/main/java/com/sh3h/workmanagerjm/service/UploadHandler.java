package com.sh3h.workmanagerjm.service;

import com.sh3h.dataprovider.data.BusEvent;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.squareup.otto.Bus;

import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

class UploadHandler extends BaseHandler {
    private static final String TAG = "UploadHandler";
    private static final int MAX_UPLOAD_TASK_NUMBER = 100;
    private static final int MAX_UPLOAD_MEDIA_NUMBER = 10;
    private boolean controlUpload = false;
    private boolean syncResult = true;
    private BusEvent.StatisticsUpload statisticsUpload;

    UploadHandler(MainApplication mainApplication,
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

        long taskId = syncMessage.getTaskId();
        switch (syncMessage.getOperate()) {
            case SyncConst.UPLOAD_ONE_WORK:
                uploadTask(taskId);
                break;
            case SyncConst.UPLOAD_ONE_MEDIA:
                uploadMediasOfOneTask(taskId);
                break;
            case SyncConst.UPLOAD_ALL_TASK:
                controlUploadTask();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 上传一张历史列表中一个taskId的所有单子
     *
     * @param taskId 任务号
     */
    private void uploadTask(long taskId) {
        mSubscription = mDataManager.uploadTask(taskId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<History>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "-----uploadOneTask  onCompleted-----" + taskId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateOneFlag(false, taskId);
                        LogUtil.d(TAG, "---uploadOneTask---" + e.getMessage() + "--" + taskId);
                    }

                    @Override
                    public void onNext(List<History> histories) {
                        boolean flag = true;
                        boolean isNeedUploadMedia = false;
                        long taskId = 0;
                        for (History history : histories) {
                            taskId = history.getTaskId();
                            if ((history.getState() == ConstantUtil.State.HANDLE
                                    || history.getState() == ConstantUtil.State.BACK)
                                    && history.getUploadFlag() == ConstantUtil.UploadFlag.UPLOADED) {
                                isNeedUploadMedia = true;
                            }

                            if (history.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            if (isNeedUploadMedia) {
                                uploadMediasOfOneTask(taskId);
                            } else {
                                updateOneFlag(true, taskId);
                            }
                        } else {
                            updateOneFlag(false, taskId);
                        }
                    }
                });
    }

    /**
     * 上传一个taskId 的所有未上传的照片
     *
     * @param taskId 任务号
     */
    private void uploadMediasOfOneTask(final long taskId) {
        mSubscription = mDataManager.uploadMediasOfOneTask(taskId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<MultiMedia>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        updateOneMediaFlag(0, false, taskId);
                    }

                    @Override
                    public void onNext(List<MultiMedia> multiMedias) {
                        boolean result = true;
                        for (MultiMedia media : multiMedias) {
                            if (media.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                                result = false;
                                break;
                            }
                        }
                        updateOneMediaFlag(multiMedias.size(), result, taskId);
                    }
                });
    }

    private void controlUploadTask() {
        if (controlUpload) {
            return;
        }

        syncResult = true;
        controlUpload = true;
        statisticsUpload = new BusEvent.StatisticsUpload();

        getUploadAllTask();
    }

    private void getUploadAllTask() {
        mSubscription = mDataManager.getUploadAllTask(0, MAX_UPLOAD_TASK_NUMBER)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<History>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<History> histories) {
                        int size = histories == null ? 0 : histories.size();
                        if (size == 0) {
                            getUploadAllMedias();
                        } else {
                            uploadAllTask(histories);
                        }
                    }
                });
    }

    /**
     * 上传所有未上传工单
     */
    private void uploadAllTask(List<History> histories) {
        int totalSize = histories.size();
        mSubscription = mDataManager.uploadAllTask(histories)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<History>>() {
                    @Override
                    public void onCompleted() {
                        if (totalSize < MAX_UPLOAD_TASK_NUMBER) {
                            getUploadAllMedias();
                        } else if (totalSize == MAX_UPLOAD_TASK_NUMBER) {
                            getUploadAllTask();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        statisticsUpload.setFailTextCount(statisticsUpload.getFailTextCount()
                                + totalSize);
                        if (totalSize < MAX_UPLOAD_TASK_NUMBER) {
                            getUploadAllMedias();
                        } else if (totalSize == MAX_UPLOAD_TASK_NUMBER) {
                            getUploadAllTask();
                        }
                    }

                    @Override
                    public void onNext(List<History> histories) {
                        int successSize = 0;
                        int allSize = histories.size();
                        for (History history : histories) {
                            if (history.getUploadFlag() == ConstantUtil.UploadFlag.UPLOADED) {
                                successSize++;
                            }
                        }

                        statisticsUpload.setSuccessTextCount(statisticsUpload.getSuccessTextCount() + successSize);
                        statisticsUpload.setFailTextCount(statisticsUpload.getFailTextCount()
                                + allSize - successSize);
                    }
                });
    }

    private void getUploadAllMedias() {
        mSubscription = mDataManager.getUploadAllMedias(0, MAX_UPLOAD_MEDIA_NUMBER)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<MultiMedia>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<MultiMedia> multiMediaList) {
                        int size = multiMediaList == null ? 0 : multiMediaList.size();
                        if (size == 0) {
                            updateFlag(syncResult);
                        } else {
                            uploadAllMedias(multiMediaList);
                        }
                    }
                });
    }

    /**
     * 上传全部多媒体
     */
    private void uploadAllMedias(List<MultiMedia> multiMediaList) {
        int totalSize = multiMediaList.size();
        mSubscription = mDataManager.uploadAllMedias(multiMediaList)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<MultiMedia>>() {
                    @Override
                    public void onCompleted() {
                        if (totalSize < MAX_UPLOAD_MEDIA_NUMBER) {
                            updateFlag(syncResult);
                        } else if (totalSize == MAX_UPLOAD_MEDIA_NUMBER) {
                            getUploadAllMedias();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        syncResult = false;
                        if (totalSize < MAX_UPLOAD_MEDIA_NUMBER) {
                            updateFlag(syncResult);
                        } else if (totalSize == MAX_UPLOAD_MEDIA_NUMBER) {
                            getUploadAllMedias();
                        }
                        statisticsUpload.setFailMediaCount(statisticsUpload.getFailMediaCount() + totalSize);
                    }

                    @Override
                    public void onNext(List<MultiMedia> multiMedias) {
                        int successSize = 0;
                        int allSize = multiMedias.size();
                        for (MultiMedia history : multiMedias) {
                            if (history.getUploadFlag() == ConstantUtil.UploadFlag.UPLOADED) {
                                successSize++;
                            }
                        }
                        statisticsUpload.setSuccessMediaCount(
                                statisticsUpload.getSuccessMediaCount() + successSize);
                        statisticsUpload.setFailMediaCount(statisticsUpload.getFailMediaCount()
                                + allSize - successSize);
                    }
                });
    }

    private void updateFlag(boolean result) {
        mDataManager.updateUploadFlag()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        syncTask(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        syncTask(result);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    private void updateOneFlag(boolean result, long taskId) {
        mDataManager.updateUploadFlag()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        uploadOneTask(result, taskId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        uploadOneTask(result, taskId);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    private void updateOneMediaFlag(int number, boolean result, long taskId) {
        mDataManager.updateUploadFlag()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        uploadOneTaskMedia(number, result, taskId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        uploadOneTaskMedia(number, result, taskId);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    private void syncTask(boolean result) {
        controlUpload = false;
        UIBusEvent.SyncTask task = new UIBusEvent.SyncTask();
        task.setSuccess(result);
        task.setMessage(result ? R.string.toast_sync_task_success : R.string.toast_sync_task_fail);
        mEventPosterHelper.postEventSafely(task);
    }

    private void uploadOneTask(boolean result, long taskId) {
        UIBusEvent.UploadOneTaskResult task = new UIBusEvent.UploadOneTaskResult();
        task.setSuccess(result);
        task.setTaskId(taskId);
        mEventPosterHelper.postEventSafely(task);
    }

    private void uploadOneTaskMedia(int number, boolean result, long taskId) {
        UIBusEvent.UploadOneTaskMediaResult task = new UIBusEvent.UploadOneTaskMediaResult();
        task.setSuccess(result);
        task.setTaskId(taskId);
        task.setMediaNumber(number);
        mEventPosterHelper.postEventSafely(task);
    }

}
