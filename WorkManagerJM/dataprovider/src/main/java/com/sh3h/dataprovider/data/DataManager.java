package com.sh3h.dataprovider.data;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.condition.MaterialCondition;
import com.sh3h.dataprovider.condition.MultiMediaCondition;
import com.sh3h.dataprovider.condition.TaskCondition;
import com.sh3h.dataprovider.condition.TemporaryCondition;
import com.sh3h.dataprovider.condition.WordCondition;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyAssistInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.DeleteTask;
import com.sh3h.dataprovider.data.entity.retrofit.DispatchInfo;
import com.sh3h.dataprovider.data.entity.retrofit.DispatchResult;
import com.sh3h.dataprovider.data.entity.retrofit.DownLoadWorkInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.DownloadTaskResult;
import com.sh3h.dataprovider.data.entity.retrofit.MaterialResult;
import com.sh3h.dataprovider.data.entity.retrofit.MessageUpdate;
import com.sh3h.dataprovider.data.entity.retrofit.MeterCard;
import com.sh3h.dataprovider.data.entity.retrofit.PersonResult;
import com.sh3h.dataprovider.data.entity.retrofit.SearchDetailInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsInfo;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsResult;
import com.sh3h.dataprovider.data.entity.retrofit.UploadReportInfo;
import com.sh3h.dataprovider.data.entity.retrofit.UploadReportResult;
import com.sh3h.dataprovider.data.entity.retrofit.UploadTaskInfo;
import com.sh3h.dataprovider.data.entity.retrofit.UploadWorkRelationInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.UploadWorkResultEntity;
import com.sh3h.dataprovider.data.entity.retrofit.VerifyResult;
import com.sh3h.dataprovider.data.entity.retrofit.WordInfo;
import com.sh3h.dataprovider.data.entity.retrofit.WordResult;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDispatchHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUTransformCancelHandle;
import com.sh3h.dataprovider.data.entity.ui.DUVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.db.DbHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.remote.HttpHelper;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.TransformUtil;
import com.sh3h.localprovider.condition.HistoryConditionDb;
import com.sh3h.localprovider.condition.MultiMediaConditionDb;
import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.localprovider.entity.Task;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

import static com.sh3h.localprovider.condition.HistoryConditionDb.GET_TASKS_BY_TASK_ID_SET_UPLOADING;


@Singleton
public class DataManager {
    private static final String TAG = "DataManager";

    private final HttpHelper mHttpHelper;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ConfigHelper mConfigHelper;

    @Inject
    public DataManager(HttpHelper httpHelper,
                       PreferencesHelper preferencesHelper,
                       DbHelper dbHelper,
                       ConfigHelper configHelper) {
        mHttpHelper = httpHelper;
        mPreferencesHelper = preferencesHelper;
        mDbHelper = dbHelper;
        mConfigHelper = configHelper;
    }

    public void destroy() {
        mDbHelper.destroy();
    }

    public void clearData() {
        mDbHelper.clearData();
    }

    /**
     * initialize logger file
     */
    public void initLogger() {
        LogUtil.initLogger(mConfigHelper.getLogFilePath().getPath());
        mDbHelper.init();
    }

    /**
     * close the logger file
     */
    public void closeLogger() {
        LogUtil.closeLogger();
    }

    public String getAccount() {
        String account = mPreferencesHelper.getUserSession().getAccount();
        return TextUtil.isNullOrEmpty(account) ? "289" : account;
    }

    /**
     * 根据条件获取任务列表
     *
     * @param condition 任务条件
     */
    public Observable<List<DUData>> getTaskList(TaskCondition condition) {
        condition.setAccount(getAccount());
        return mDbHelper.getTaskList(condition);
    }

    public Observable<Long> getTaskCount(TaskCondition condition) {
        condition.setAccount(getAccount());
        return mDbHelper.getTaskCount(condition);
    }

    public Observable<List<DUData>> getHistoryList(HistoryCondition condition) {
        return mDbHelper.getHistoryList(condition);
    }

    public Observable<DUData> getReportTemporary(TemporaryCondition condition) {
        return mDbHelper.getReportTemporary(condition);
    }

    public File getImageFolderPath() {
        return mConfigHelper.getImageFolderPath();
    }

    public File getSoundFolderPath() {
        return mConfigHelper.getSoundFolderPath();
    }

    public Observable<Boolean> downLoadWork(int type, int since, int count) {
        DownLoadWorkInfoEntity entity = new DownLoadWorkInfoEntity(getAccount(), type, since, count);
        return mHttpHelper.downLoadWork(entity).
                concatMap((list) -> {
                            List<Task> tasks = new ArrayList<>();
                            for (DownloadTaskResult resultEntity : list) {
                                tasks.add(TransformUtil.transform(resultEntity, getAccount()));
                            }
                            return mDbHelper.insertTasks(tasks, true, type, getAccount());
                        }
                );
    }

    public Observable<Boolean> downLoadWords() {
        WordInfo entity = new WordInfo();
        entity.setGroup("all");
        return mHttpHelper.downLoadWords(entity)
                .concatMap((results) -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null ||
                            results.getData().size() == ConstantUtil.EMPTY_DATA) {
                        return Observable.error(new Exception("results is error"));
                    }

                    List<WordResult> resultEntities = results.getData();
                    return mDbHelper.saveWords(TransformUtil.transformToWord(resultEntities));
                });
    }

    public Observable<Boolean> downLoadMaterials() {
        return mHttpHelper.downLoadMaterials()
                .concatMap((results) -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null ||
                            results.getData().size() == ConstantUtil.EMPTY_DATA) {
                        return Observable.error(new Exception("results is error"));
                    }
                    List<MaterialResult> materialResults = results.getData();
                    return mDbHelper.saveMaterials(TransformUtil.transformToMaterial(materialResults));
                });
    }

    public Observable<Boolean> downLoadPersons() {
        return mHttpHelper.downLoadPersons()
                .concatMap((results) -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null ||
                            results.getData().size() == ConstantUtil.EMPTY_DATA) {
                        return Observable.error(new Exception("results is error"));
                    }
                    List<PersonResult> materialResults = results.getData();
                    return mDbHelper.savePersons(TransformUtil.transformToPerson(materialResults));
                });
    }

    public Observable<DUData> handleTask(HistoryCondition condition) {
        condition.setAccount(getAccount());
        DUHandle handle = condition.getDuHandle();
        switch (handle.getReportType()) {
            case ConstantUtil.ReportType.Handle:
                return mDbHelper.insertHistory(condition);
            case ConstantUtil.ReportType.Report:
                return mDbHelper.insertReportHistory(condition);
            case ConstantUtil.ReportType.Assist:
                return assistTask(condition);
            case ConstantUtil.ReportType.Apply:
                return applyTask(condition);
            default:
                return Observable.error(new Throwable("unKnow error"));
        }
    }

    public Observable<String> dispatch(DUDispatchHandle handle) {
        handle.setAccount(getAccount());
        return mHttpHelper.dispatch(handle)
                .concatMap(result -> {
                    if (result.getCode() != ConstantUtil.SUCCESS_CODE
                            || result.getStatusCode() != HttpURLConnection.HTTP_OK) {
                        return Observable.error(new Exception("dispatch is error"));
                    }

                    return Observable.just(handle.getIds());
                });
    }

    public Observable<String> transformCancel(DUTransformCancelHandle handle) {
        handle.setAccount(getAccount());
        return mHttpHelper.transformCancel(handle)
                .concatMap(result -> {
                    if (result.getCode() != ConstantUtil.SUCCESS_CODE ||
                            result.getStatusCode() != HttpURLConnection.HTTP_OK) {
                        return Observable.error(new Exception("dispatch is error"));
                    }

                    return Observable.just(handle.getIds());
                });
    }

    public Observable<DUVerifyHandle> verifyTask(DUVerifyHandle handle) {
        handle.setVerifyPerson(getAccount());
        return mHttpHelper.verifyTask(handle)
                .concatMap(result -> {
                    if (result.getCode() != ConstantUtil.SUCCESS_CODE ||
                            result.getStatusCode() != HttpURLConnection.HTTP_OK) {
                        return Observable.error(new Exception("dispatch is error"));
                    }

                    return Observable.just(handle);
                });
    }

    public Observable<DUData> temporaryData(DUHandle handle) {
        handle.setAccount(getAccount());
        return mDbHelper.insertTemporary(handle);
    }

    public Observable<DUMedia> insertMedia(DUMedia duMedia) {
        return mDbHelper.insertMedia(duMedia);
    }

    public Observable<Boolean> deleteMedia(DUMedia duMedia) {
        return mDbHelper.deleteMedia(duMedia)
                .concatMap(aVoid ->
                        Observable.create(subscriber -> {
                            File folder;
                            switch (duMedia.getFileType()) {
                                case ConstantUtil.FileType.FILE_PICTURE:
                                    folder = getImageFolderPath();
                                    break;
                                case ConstantUtil.FileType.FILE_VOICE:
                                    folder = getSoundFolderPath();
                                    break;
                                default:
                                    folder = getImageFolderPath();
                                    break;
                            }
                            File mediaFile = new File(folder, duMedia.getFileName());
                            try {
                                if (mediaFile.exists()) {
                                    mediaFile.delete();
                                    LogUtil.i(TAG, "deleteMedia:%s", duMedia.toString());
                                }
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                subscriber.onError(new Throwable("delete photo error"));
                            }
                        }));
    }

    public Observable<ArrayList<DUData>> searchWork(SearchDetailInfoEntity infoEntity) {
        return mHttpHelper.searchWork(infoEntity);
    }

    public Observable<DUData> searchHandle(DUData duData) {
        if (duData.getHandles() != null && duData.getHandles().size() > 0) {
            return Observable.just(duData);
        }

        DUTask task = duData.getDuTask();
        if (task == null || task.getTaskId() == 0) {
            return Observable.just(duData);
        }

        return mHttpHelper.searchHandle(duData);
    }

    public Observable<List<History>> uploadTask(long taskId) {
        HistoryConditionDb conditionDb = new HistoryConditionDb();
        conditionDb.setOperate(GET_TASKS_BY_TASK_ID_SET_UPLOADING);
        conditionDb.setAccount(getAccount());
        conditionDb.setTaskId(taskId);

        return mDbHelper.getHistoryTasks(conditionDb)
                .concatMap(histories -> {
                    if (histories == null || histories.size() == 0) {
                        return Observable.error(new Exception("no not uploaded data"));
                    }

                    int reportType = histories.get(0).getReportType();
                    switch (reportType) {
                        case ConstantUtil.ReportType.Report:
                            return updateAllLocalTask(histories);
                        case ConstantUtil.ReportType.Handle:
                            return updateAllServerTask(histories);
                        default:
                            return Observable.error(new Throwable("uploadAllTask reportType error"));
                    }
                });
    }

    public Observable<List<MultiMedia>> uploadMediasOfOneTask(long taskId) {
        MultiMediaConditionDb conditionDb = new MultiMediaConditionDb();
        conditionDb.setOperate(MultiMediaConditionDb.GET_NOT_UPLOAD_MEDIAS_OF_ONE_TASK);
        conditionDb.setTaskId(taskId);
        conditionDb.setAccount(getAccount());
        return mDbHelper.getMultiMedias(conditionDb)
                .concatMap(multiMedias -> {
                    if (multiMedias.size() <= 6) {
                        return Observable.just(multiMedias);
                    }

                    List<List<MultiMedia>> list = new ArrayList<>();
                    List<MultiMedia> medias = new ArrayList<>();
                    list.add(medias);
                    for (MultiMedia media : multiMedias) {
                        if (medias.size() >= 6) {
                            medias = new ArrayList<>();
                            list.add(medias);
                        }
                        medias.add(media);
                    }
                    return Observable.from(list);
                })
                .concatMap(this::uploadOneTaskMedias);
    }

    public Observable<List<History>> getUploadAllTask(int offset, int limit) {
        HistoryConditionDb conditionDb = new HistoryConditionDb();
        conditionDb.setOperate(HistoryConditionDb.GET_NOT_UPLOAD_UPDATE_FALG);
        conditionDb.setUploadFlag(ConstantUtil.UploadFlag.NOT_UPLOAD);
        conditionDb.setAccount(getAccount());
        conditionDb.setLimit(limit);
        conditionDb.setOffset(offset);
        return mDbHelper.getHistoryTasks(conditionDb);
    }

    public Observable<List<History>> uploadAllTask(List<History> historyList) {
        return Observable.just(historyList)
                //分组发射
                .concatMap(histories -> {
                    List<List<History>> allHistories = new ArrayList<>();
                    List<History> list = new ArrayList<>();
                    allHistories.add(list);
                    int reportType = histories.get(0).getReportType();
                    for (History history : histories) {
                        if (reportType != history.getReportType()) {
                            list = new ArrayList<>();
                            allHistories.add(list);
                        }
                        list.add(history);
                        reportType = history.getReportType();
                    }
                    return Observable.from(allHistories);
                })
                .concatMap(histories -> {
                    int reportType = histories.get(0).getReportType();
                    switch (reportType) {
                        case ConstantUtil.ReportType.Report:
                            return updateAllLocalTask(histories);
                        case ConstantUtil.ReportType.Handle:
                            return updateAllServerTask(histories);
                        default:
                            return Observable.error(new Throwable("uploadAllTask reportType error"));
                    }
                });
    }

    public Observable<List<MultiMedia>> getUploadAllMedias(int offset, int limit) {
        MultiMediaConditionDb conditionDb = new MultiMediaConditionDb();
        conditionDb.setOperate(MultiMediaConditionDb.GET_NOT_UPLOAD_UPDATE_FLAG);
        conditionDb.setAccount(getAccount());
        conditionDb.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
        conditionDb.setOffset(offset);
        conditionDb.setLimit(limit);
        return mDbHelper.getMultiMedias(conditionDb);
    }

    public Observable<List<MultiMedia>> uploadAllMedias(List<MultiMedia> multiMediaList) {
        return Observable.just(multiMediaList)
                .concatMap(multiMedias -> {
                    List<List<MultiMedia>> list = new ArrayList<>();
                    List<MultiMedia> medias = new ArrayList<>();
                    list.add(medias);
                    Long taskId = multiMedias.get(0).getTaskId();
                    for (MultiMedia media : multiMedias) {
                        if (taskId != media.getTaskId()) {
                            medias = new ArrayList<>();
                            list.add(medias);
                        } else {
                            if (medias.size() >= 6) {
                                medias = new ArrayList<>();
                                list.add(medias);
                            }
                        }
                        taskId = media.getTaskId();
                        medias.add(media);
                    }
                    return Observable.from(list);
                })
                .onBackpressureBuffer(100)
                .concatMap(this::uploadOneTaskMedias);
    }

    public Observable<Boolean> insertTasks(List<Task> list) {
        return mDbHelper.insertTasks(list, false, 0, getAccount());
    }

    public Observable<Boolean> updateTaskState(List<MessageUpdate> messageUpdates) {
        TaskCondition condition = new TaskCondition();
        condition.setOperate(TaskCondition.UPDATE_TASK_LIST_STATE);
        condition.setAccount(getAccount());
        condition.setList(messageUpdates);
        return mDbHelper.updateTaskState(condition);
    }

    public Observable<Boolean> deleteTask(List<DeleteTask> deleteTasks) {
        if (deleteTasks == null || deleteTasks.size() == 0) {
            return Observable.just(false);
        }

        List<Long> taskIds = new ArrayList<>();
        for (DeleteTask deleteTask : deleteTasks) {
            taskIds.add(deleteTask.getTaskId());
        }

        return mDbHelper.deleteTask(taskIds);
    }

    public String getBaseUri() {
        return mConfigHelper.getBaseUri();
    }

    public String getReverseBaseUri() {
        return mConfigHelper.getReverseBaseUri();
    }

    public boolean isUsingReservedAddress() {
        return mConfigHelper.isUsingReservedAddress();
    }

    public Observable<Boolean> saveNetWorkSetting(String baseUri, String reservedBaseUri,
                                                  boolean isUsingReservedAddress) {
        return mConfigHelper.saveNetWorkSetting(baseUri, reservedBaseUri, isUsingReservedAddress);
    }

    public Observable<Boolean> clearHistory() {
        return mDbHelper.deleteAllData().//表的记录
                concatMap(aBoolean -> {
            if (!aBoolean) {
                return Observable.error(new Exception("delete failure"));
            }
            return mConfigHelper.deleteAllPicture();//清除照片
        }).concatMap(o -> {
            return mConfigHelper.deleteLog();  //log
        });
    }

    public Observable<Void> saveUserConfig() {
        return mConfigHelper.initUserConfig(mPreferencesHelper.getUserSession().getUserId());
    }

    public Observable<Boolean> savePicQuality(String str) {
        return mConfigHelper.savePicQuality(str);
    }

    public Observable<Boolean> cleanData() {
        return Observable.just(true);
//        return mDbHelper.cleanData(getAccount());
    }

    public Observable<Boolean> updateUploadFlag() {
        return mDbHelper.updateUploadFlag();
    }

    public Observable<List<DUMedia>> getDUMedias(MultiMediaCondition condition) {
        condition.setAccount(getAccount());
        return mDbHelper.getMultiMedias(TransformUtil.transformMultiMediaConditionDb(condition))
                .map(medias -> {
                    List<DUMedia> list = new ArrayList<>();
                    if (medias != null) {
                        for (MultiMedia media : medias) {
                            list.add(TransformUtil.transformDUMedia(media));
                        }
                    }
                    return list;
                });
    }

    public Observable<MeterCard> downloadMeterCard(@NonNull String cardId) {
        return mHttpHelper.downloadMeterCard(cardId.trim());
    }

    public Observable<List<StatisticsResult>> downloadStatistics(StatisticsInfo info) {
        return mHttpHelper.downloadStatistics(info)
                .concatMap(results -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE
                            || results.getStatusCode() != HttpURLConnection.HTTP_OK
                            || results.getData() == null) {
                        return Observable.error(new Exception("downloadStatistics error"));
                    }

                    List<StatisticsResult> list = results.getData();
                    return Observable.just(list);
                });
    }

    public Observable<List<DUData>> downloadDispatch(DispatchInfo dispatchInfo) {
        return mHttpHelper.downloadDispatch(dispatchInfo)
                .concatMap(results -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null) {
                        return Observable.error(new Exception("downloadDispatch error"));
                    }

                    List<DispatchResult> dispatchResults = results.getData();
                    LogUtil.i(TAG, String.format(Locale.CHINA,
                            "download dispatch list size:%d", dispatchResults.size()));
                    List<DUData> list = new ArrayList<>();
                    for (DispatchResult result : dispatchResults) {
                        list.add(TransformUtil.transformDUData(result, getAccount()));
                    }
                    return Observable.just(list);
                });
    }

    public Observable<List<DUData>> downloadVerify() {
        return mHttpHelper.downloadVerify(getAccount())
                .concatMap(results -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null) {
                        return Observable.error(new Exception("downloadVerify error"));
                    }

                    List<VerifyResult> verifyResults = results.getData();
                    List<DUData> list = new ArrayList<>();
                    for (VerifyResult result : verifyResults) {
                        list.add(TransformUtil.transformDUData(result, getAccount()));
                    }
                    return Observable.just(list);
                });
    }

    public Observable<Boolean> downloadMedia(String url, String fileName, int fileType) {
        return mHttpHelper.downloadMedia(url, fileName, fileType);
    }

    public Observable<List<DUMaterial>> getMaterial(MaterialCondition condition) {
        return mDbHelper.getMaterial(condition);
    }

    public Observable<List<DUWord>> getWord(WordCondition condition) {
        return mDbHelper.getWord(condition);
    }

    public Observable<List<DUWord>> getWord(long parentId, String group) {
        return mDbHelper.getWord(parentId, group);
    }

    public Observable<List<DUWord>> getOneGroup(String group) {
        return mDbHelper.getOneGroup(group);
    }

    public Observable<List<DUWord>> getCurrentStation() {
        return mDbHelper.getCurrentStation(getAccount());
    }

    public Observable<List<DUPerson>> getAllPerson() {
        return mDbHelper.getAllPerson();
    }

    /*挂起、恢复、延期*/
    private Observable<DUData> applyTask(HistoryCondition condition) {
        List<ApplyInfoEntity> list = new ArrayList<>();
        ApplyInfoEntity entity = TransformUtil.transformApplyInfoEntity(condition.getDuHandle());
        list.add(entity);

        return mHttpHelper.apply(mPreferencesHelper.getUserSession().getAccount(), list)
                .concatMap(results -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null ||
                            results.getData().size() == ConstantUtil.EMPTY_DATA ||
                            !results.getData().get(0).isSuccess()) {
                        return Observable.error(new Exception("apply handleNowTask error"));
                    }

                    DUHandle duHandle = condition.getDuHandle();
                    condition.setAccount(getAccount());
                    duHandle.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                    return mDbHelper.insertHistory(condition);
                });
    }

    /*申请协助*/
    private Observable<DUData> assistTask(HistoryCondition condition) {
        List<ApplyAssistInfoEntity> list = new ArrayList<>();
        list.add(TransformUtil.transformApplyHelpInfoEntity(condition.getDuHandle()));

        return mHttpHelper.applyAssist(mPreferencesHelper.getUserSession().getAccount(), list)
                .concatMap(results -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE
                            || results.getStatusCode() != HttpURLConnection.HTTP_OK
                            || results.getData() == null
                            || results.getData().size() != 1) {
                        return Observable.error(new Exception("apply assist error"));
                    }

                    UploadWorkResultEntity entity = results.getData().get(0);
                    if (!entity.isSuccess()) {
                        return Observable.error(new Exception(entity.getMessage()));
                    }


                    condition.getDuHandle().setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                    condition.setAccount(getAccount());
                    return mDbHelper.insertHistory(condition);
                });
    }

    private Observable<List<History>> updateAllLocalTask(List<History> histories) {
        return Observable.just(histories)
                //把History 转化为上传的格式
                .map(list -> {
                    ArrayList<UploadReportInfo> arrayList = new ArrayList<>();
                    for (History history : list) {
                        UploadReportInfo entity = TransformUtil.transFormToReportInfo(history);
                        arrayList.add(entity);
                    }
                    return arrayList;
                })
                .onBackpressureBuffer(100)
                //上报
                .concatMap(reportInfoEntities -> mHttpHelper.reportLocalTask(getAccount(), reportInfoEntities))
                //更改
                .concatMap(result -> {
                    if ((result.getCode() != ConstantUtil.SUCCESS_CODE)
                            || (result.getStatusCode() != HttpURLConnection.HTTP_OK)
                            || (result.getData() == null)
                            || (result.getData().size() == ConstantUtil.EMPTY_DATA)) {
                        return Observable.error(new Throwable("upload one task failure"));
                    }

                    List<UploadReportResult> entities = result.getData();
                    Map<Long, UploadReportResult> map = new HashMap<>();
                    for (UploadReportResult entity : entities) {
                        map.put(entity.getLocalTaskId(), entity);
                    }

                    boolean isHaveSuccess = false;
                    List<MultiMedia> allMedias = new ArrayList<>();
                    for (History history : histories) {
                        UploadReportResult entity = map.get(history.getTaskId());
                        if (entity != null && entity.isSuccess()) {
                            isHaveSuccess = true;
                            List<MultiMedia> medias = history.getMultiMedias();
                            for (MultiMedia media : medias) {
                                media.setTaskId(entity.getServerTaskId());
                                media.setUploadFlag(ConstantUtil.UploadFlag.NOT_UPLOAD);
                            }
                            allMedias.addAll(medias);

                            history.setTaskId(entity.getServerTaskId());
                            history.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                            String content = history.getContent();
                            if (!TextUtil.isNullOrEmpty(content)) {
                                Gson gson = new Gson();
                                DUTask task = gson.fromJson(content, DUTask.class);
                                task.setTaskId(entity.getServerTaskId());
                                history.setContent(gson.toJson(task));
                            }
                        }
                    }

                    return isHaveSuccess ? mDbHelper.updateMultiMedia(allMedias)
                            .concatMap(multiMedia -> mDbHelper.updateHistory(histories))
                            : Observable.just(histories);
                });
    }

    private Observable<List<History>> updateAllServerTask(List<History> histories) {
        return Observable.just(histories)
                //把History 转化为上传的格式
                .map(list -> {
                    List<UploadTaskInfo> entities = new ArrayList<>();
                    for (History history : histories) {
                        UploadTaskInfo entity = TransformUtil.transformToUploadWorkInfo(history);
                        entities.add(entity);
                    }
                    return entities;
                })
                .onBackpressureBuffer(100)
                //上报
                .concatMap(entities -> mHttpHelper.uploadTasks(getAccount(), entities))
                //更改
                .concatMap(result -> {
                    if ((result.getCode() != ConstantUtil.SUCCESS_CODE)
                            || (result.getStatusCode() != HttpURLConnection.HTTP_OK)
                            || (result.getData() == null)
                            || (result.getData().size() == ConstantUtil.EMPTY_DATA)) {
                        return Observable.error(new Throwable("upload one task failure"));
                    }

                    List<UploadWorkResultEntity> entities = result.getData();
                    for (History history : histories) {
                        for (UploadWorkResultEntity entity : entities) {
                            if (entity.isSuccess() && entity.getTaskId() == history.getTaskId()
                                    && entity.getState() == history.getState()) {
                                history.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                                break;
                            }
                        }
                    }
                    return mDbHelper.updateHistory(histories);
                });
    }

    private Observable<List<MultiMedia>> uploadOneTaskMedias(List<MultiMedia> medias) {
        if (medias.size() == 0) {
            return Observable.just(medias);
        }

        return Observable.just(medias)
                .buffer(6)
                .concatMap(lists -> mHttpHelper.uploadMediaList(medias))
                .concatMap(medias1 -> {
                    List<UploadWorkRelationInfoEntity> infoEntities = new ArrayList<>();
                    for (MultiMedia uploadMultiMedia : medias1) {
                        if (TextUtil.isNullOrEmpty(uploadMultiMedia.getFileUrl()) ||
                                TextUtil.isNullOrEmpty(uploadMultiMedia.getFileHash())) {
                            LogUtil.i(TAG, "uploadOneTaskMedias call null " + uploadMultiMedia.getTaskId());
                            return Observable.error(new Exception("updateMediaList failure"));
                        }

                        UploadWorkRelationInfoEntity infoEntity = new UploadWorkRelationInfoEntity();
                        infoEntity.setUrl(uploadMultiMedia.getFileUrl());
                        infoEntity.setFileType(ConstantUtil.PICTURE_SUFFIX);
                        infoEntity.setFileSize(0);
                        infoEntity.setFileHash(uploadMultiMedia.getFileHash());
                        infoEntities.add(infoEntity);
                    }
                    return Observable.just(infoEntities);
                })
                .concatMap(entities -> mHttpHelper.uploadMediasRelations(String.valueOf(medias.get(0).getTaskId()), entities))
                .concatMap(aBoolean -> {
                    LogUtil.i(TAG, "uploadOneTaskMedias concatMap" + medias.get(0).getTaskId());
                    if (aBoolean) {
                        for (MultiMedia media : medias) {
                            media.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                        }
                        return mDbHelper.updateMultiMedia(medias);
                    }
                    return Observable.just(medias);
                });
    }

}
