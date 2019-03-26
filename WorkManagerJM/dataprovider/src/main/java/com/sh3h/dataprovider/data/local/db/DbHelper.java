package com.sh3h.dataprovider.data.local.db;

import android.content.Context;

import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.condition.MaterialCondition;
import com.sh3h.dataprovider.condition.TaskCondition;
import com.sh3h.dataprovider.condition.TemporaryCondition;
import com.sh3h.dataprovider.condition.WordCondition;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.TransformUtil;
import com.sh3h.localprovider.DBManager;
import com.sh3h.localprovider.condition.HistoryConditionDb;
import com.sh3h.localprovider.condition.MultiMediaConditionDb;
import com.sh3h.localprovider.condition.TaskConditionDb;
import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.Material;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.localprovider.entity.Person;
import com.sh3h.localprovider.entity.Task;
import com.sh3h.localprovider.entity.Word;
import com.sh3h.mobileutil.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DbHelper {
    private static final String TAG = "DbHelper";

    private final Context mContext;
    private final ConfigHelper mConfigHelper;
    private boolean mIsInit;

    @Inject
    DbHelper(@ApplicationContext Context context,
             ConfigHelper configHelper) {
        mContext = context;
        mConfigHelper = configHelper;
        mIsInit = false;
    }

    /**
     * initialize
     */
    public synchronized boolean init() {
        if (!mIsInit) {
            mIsInit = true;
            return DBManager.getInstance().init(mConfigHelper.getDBFilePath().getPath(), mContext);
        }
        return true;
    }

    /**
     * destroy
     */
    public synchronized void destroy() {
        if (mIsInit) {
            mIsInit = false;
            DBManager.getInstance().destroy();
        }
    }

    /**
     * clear all tables
     */
    public synchronized boolean clearData() {
        try {
            // if db is opened failure, return also
            if (!init()) {
                return false;
            }

            DBManager.getInstance().clearAllTables();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i(TAG, String.format("---clearData---%s", e.getMessage()));
        }
        return false;
    }

    /**
     * @param list           数据源
     * @param downloadOrPush 推送还是下载
     * @return 下载的要删除无用的任务数据，推送的不需要
     */
    public Observable<Boolean> insertTasks(List<Task> list, boolean downloadOrPush, int type, String account) {
        init();
        return DBManager.getInstance().insertTasks(list, downloadOrPush, type, account);
    }

    public Observable<List<DUData>> getTaskList(TaskCondition condition) {
        init();

        return DBManager.getInstance().getTaskList(TransformUtil.transform(condition))
                .map(tasks -> {
                    if (tasks == null || tasks.size() <= 0) {
                        return new ArrayList<>();
                    }
                    List<DUData> duTaskList = new ArrayList<>();
                    for (Task task : tasks) {
                        duTaskList.add(TransformUtil.transformDUData(task));
                    }
                    return duTaskList;
                });
    }

    public Observable<Long> getTaskCount(TaskCondition condition) {
        init();

        return Observable.create(subscriber -> {
            long number;
            try {
                number = DBManager.getInstance().getTaskCount(TransformUtil.transform(condition));
                subscriber.onNext(number);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(new Exception("getTaskCount error"));
            }
        });
    }

    public Observable<Boolean> updateTaskState(TaskCondition condition) {
        init();

        return DBManager.getInstance().updateTaskState(TransformUtil.transform(condition));
    }

    public Observable<Boolean> deleteTask(List<Long> taskIds) {
        init();

        return DBManager.getInstance().deleteTask(taskIds);
    }

    public Observable<List<DUData>> getHistoryList(HistoryCondition condition) {
        init();
        return DBManager.getInstance().getHistoryList(TransformUtil.transform(condition))
                .map(histories -> {
                    if (histories == null || histories.size() <= 0) {
                        return new ArrayList<>();
                    }
                    List<DUData> list = new ArrayList<>();
                    for (History history : histories) {
                        DUData duData = TransformUtil.transformDUData(history);
                        list.add(duData);
                    }
                    return list;
                });
    }

    public Observable<DUData> getReportTemporary(TemporaryCondition condition) {
        init();
        return DBManager.getInstance().getReportTemporary(TransformUtil.transform(condition))
                .concatMap(temporary -> {
                    if (temporary == null) {
                        return Observable.error(new Exception("temporary is null."));
                    }

                    DUData duData = new DUData();
                    DUTask task = new DUTask();
                    task.setTaskId(temporary.getId());
                    task.setAccount(temporary.getAccount());
                    task.setType(ConstantUtil.WorkType.TASK_REPORT);
                    ArrayList<DUHandle> handles = new ArrayList<>();
                    DUHandle handle = TransformUtil.transformDUHandle(temporary);
                    temporary.resetMultiMedias();
                    List<MultiMedia> medias = temporary.getMultiMedias();
                    if (medias != null && medias.size() > 0) {
                        ArrayList<DUMedia> duMedias = new ArrayList<>();
                        for (MultiMedia media : medias) {
                            duMedias.add(TransformUtil.transformDUMedia(media));
                        }
                        handle.setMedias(duMedias);
                    }
                    handles.add(handle);
                    duData.setHandles(handles);
                    duData.setDuTask(task);
                    return Observable.just(duData);
                });
    }

    public Observable<? extends Boolean> saveWords(List<Word> words) {
        init();
        return DBManager.getInstance().insertWords(words);
    }

    public Observable<? extends Boolean> saveMaterials(List<Material> materials) {
        init();
        return DBManager.getInstance().insertMaterials(materials);
    }

    public Observable<? extends Boolean> savePersons(List<Person> people) {
        init();
        return DBManager.getInstance().savePersons(people);
    }

    public Observable<DUData> insertHistory(HistoryCondition condition) {
        init();
        return DBManager.getInstance().insertHistory(TransformUtil.transform(condition))
                .map(TransformUtil::transformDUData);
    }

    public Observable<DUData> insertTemporary(DUHandle duHandle) {
        init();
        if (duHandle.getType() != ConstantUtil.WorkType.TASK_REPORT) {
            return DBManager.getInstance().insertTaskTemporary(TransformUtil.transformTemporary(duHandle))
                    .map(TransformUtil::transformDUData);
        }
        return DBManager.getInstance().insertReportTemporary(TransformUtil.transformTemporary(duHandle))
                .map(temporary -> {
                    DUData duData = new DUData();
                    DUTask task = new DUTask();
                    task.setTaskId(temporary.getId());
                    task.setAccount(temporary.getAccount());
                    task.setType(ConstantUtil.WorkType.TASK_REPORT);
                    ArrayList<DUHandle> handles = new ArrayList<>();
                    DUHandle handle = TransformUtil.transformDUHandle(temporary);
                    temporary.resetMultiMedias();
                    List<MultiMedia> medias = temporary.getMultiMedias();
                    if (medias != null && medias.size() > 0) {
                        ArrayList<DUMedia> duMedias = new ArrayList<>();
                        for (MultiMedia media : medias) {
                            duMedias.add(TransformUtil.transformDUMedia(media));
                        }
                        handle.setMedias(duMedias);
                    }
                    handles.add(handle);
                    duData.setHandles(handles);
                    duData.setDuTask(task);
                    return duData;
                });
    }

    public Observable<DUData> insertReportHistory(HistoryCondition condition) {
        init();
        return DBManager.getInstance().insertReportHistory(TransformUtil.transform(condition))
                .map(history -> {
                    DUData duData = new DUData();
                    DUTask task = new DUTask();
                    task.setTaskId(history.getTaskId());
                    task.setAccount(history.getAccount());
                    task.setType(ConstantUtil.WorkType.TASK_REPORT);
                    ArrayList<DUHandle> handles = new ArrayList<>();
                    DUHandle handle = TransformUtil.transformDUHandle(history);
                    history.resetMultiMedias();
                    List<MultiMedia> medias = history.getMultiMedias();
                    if (medias != null && medias.size() > 0) {
                        ArrayList<DUMedia> duMedias = new ArrayList<>();
                        for (MultiMedia media : medias) {
                            duMedias.add(TransformUtil.transformDUMedia(media));
                        }
                        handle.setMedias(duMedias);
                    }
                    handles.add(handle);
                    duData.setHandles(handles);
                    duData.setDuTask(task);
                    return duData;
                });
    }

    public Observable<DUMedia> insertMedia(DUMedia duMedia) {
        init();
        return DBManager.getInstance().insertMedia(TransformUtil.transformMultiMedia(duMedia))
                .map(TransformUtil::transformDUMedia);
    }

    public Observable<Void> deleteMedia(DUMedia duMedia) {
        init();
        return DBManager.getInstance().deleteMedia(TransformUtil.transformMultiMedia(duMedia));
    }

    public Observable<List<History>> getHistoryTasks(HistoryConditionDb conditionDb) {
        init();
        return DBManager.getInstance().operateHistoryTask(conditionDb);
    }

    public Observable<List<History>> updateHistory(List<History> histories) {
        init();
        return DBManager.getInstance().updateHistory(histories);
    }

    public Observable<List<MultiMedia>> getMultiMedias(MultiMediaConditionDb conditionDb) {
        init();
        return DBManager.getInstance().getMultiMedias(conditionDb);
    }

    public Observable<List<MultiMedia>> updateMultiMedia(List<MultiMedia> multiMedias) {
        init();
        return DBManager.getInstance().updateMultiMedias(multiMedias);
    }

    public Observable<Boolean> deleteAllData() {
        init();
        return DBManager.getInstance().deleteAllData();
    }

    public Observable<Boolean> updateUploadFlag() {
        init();
        return DBManager.getInstance().updateUploadFlag();
    }

    public Observable<Boolean> cleanData(String account) {
        init();
        TaskConditionDb conditionDb = new TaskConditionDb();
        conditionDb.setOperate(TaskConditionDb.GET_ALL_TASK);
        conditionDb.setAccount(account);
        return DBManager.getInstance().getTaskList(conditionDb)
                .flatMap(tasks -> {
                    HistoryConditionDb condition = new HistoryConditionDb();
                    condition.setOperate(HistoryConditionDb.GET_ALL_HISTORY);
                    condition.setAccount(account);
                    return DBManager.getInstance().getHistory(condition)
                            .concatMap(history -> {
                                if (isHaveTask(tasks, history)) {
                                    return Observable.just(true);
                                }

                                List<MultiMedia> medias = history.getMultiMedias();
                                if (medias != null && medias.size() > 0) {
                                    for (MultiMedia media : medias) {
                                        File file;
                                        switch (media.getType()) {
                                            case ConstantUtil.FileType.FILE_PICTURE:
                                                file = new File(mConfigHelper.getImageFolderPath(), media.getFileName());
                                                break;
                                            case ConstantUtil.FileType.FILE_VOICE:
                                                file = new File(mConfigHelper.getSoundFolderPath(), media.getFileName());
                                                break;
                                            default:
                                                file = null;
                                        }
                                        if (file != null && file.exists()) {
                                            file.delete();
                                        }
                                    }
                                    DBManager.getInstance().deleteMedia(medias);
                                }
                                DBManager.getInstance().deleteHistory(history);
                                return Observable.just(true);
                            });
                });
    }

    public Observable<List<DUMaterial>> getMaterial(MaterialCondition condition) {
        init();
        return DBManager.getInstance().getMaterial(TransformUtil.transformMaterialConditionDb(condition))
                .concatMap(materials -> {
                    if (materials == null) {
                        return Observable.error(new Exception("getMaterial materials null."));
                    }

                    List<DUMaterial> list = new ArrayList<>();
                    for (Material material : materials) {
                        list.add(TransformUtil.transformDUMaterial(material));
                    }

                    return Observable.just(list);
                });
    }

    public Observable<List<DUWord>> getWord(WordCondition condition) {
        init();
        return DBManager.getInstance().getWord(TransformUtil.transformWordConditionDb(condition))
                .concatMap(words -> {
                    if (words == null) {
                        return Observable.error(new Exception("getWord condition words null."));
                    }

                    List<DUWord> list = new ArrayList<>();
                    for (Word word : words) {
                        list.add(TransformUtil.transformDUWord(word));
                    }

                    return Observable.just(list);
                });
    }

    public Observable<List<DUWord>> getWord(long parentId, String group) {
        init();
        return DBManager.getInstance().getWord(parentId, group)
                .concatMap(words -> {
                    if (words == null) {
                        return Observable.error(new Exception("getWord words null."));
                    }

                    List<DUWord> list = new ArrayList<>();
                    for (Word word : words) {
                        list.add(TransformUtil.transformDUWord(word));
                    }
                    return Observable.just(list);
                });
    }

    public Observable<List<DUPerson>> getAllPerson() {
        init();
        return DBManager.getInstance().getAllPerson()
                .concatMap(peoples -> {
                    if (peoples == null) {
                        return Observable.error(new Exception("getAllPerson person null."));
                    }

                    List<DUPerson> list = new ArrayList<>();
                    for (Person person : peoples) {
                        list.add(TransformUtil.transformDUPerson(person));
                    }
                    return Observable.just(list);
                });
    }

    public Observable<List<DUWord>> getOneGroup(String group) {
        init();
        return DBManager.getInstance().getOneGroup(group)
                .concatMap(words -> {
                    if (words == null) {
                        return Observable.error(new Exception("getOneGroup words null."));
                    }

                    List<DUWord> list = new ArrayList<>();
                    for (Word word : words) {
                        list.add(TransformUtil.transformDUWord(word));
                    }
                    return Observable.just(list);
                });
    }

    public Observable<List<DUWord>> getCurrentStation(String account) {
        init();

        return DBManager.getInstance().getCurrentStation(account, ConstantUtil.Group.STATION)
                .concatMap(words -> {
                    if (words == null) {
                        return Observable.error(new Exception("getCurrentStation words null."));
                    }

                    List<DUWord> list = new ArrayList<>();
                    for (Word word : words) {
                        list.add(TransformUtil.transformDUWord(word));
                    }
                    return Observable.just(list);
                });
    }

    private boolean isHaveTask(List<Task> tasks, History history) {
        for (Task task : tasks) {
            if (task.getTaskId() == history.getTaskId()
                    && history.getReplyTime() < (System.currentTimeMillis() - ConstantUtil.DELETE_DATA_INTERVAL)) {
                return true;
            }
        }
        return false;
    }

}
