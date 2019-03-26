package com.sh3h.localprovider;

import android.content.Context;

import com.sh3h.localprovider.condition.Constant;
import com.sh3h.localprovider.condition.HistoryConditionDb;
import com.sh3h.localprovider.condition.MaterialConditionDb;
import com.sh3h.localprovider.condition.MultiMediaConditionDb;
import com.sh3h.localprovider.condition.TaskConditionDb;
import com.sh3h.localprovider.condition.TemporaryConditionDb;
import com.sh3h.localprovider.condition.WordConditionDb;
import com.sh3h.localprovider.dao.DaoMaster;
import com.sh3h.localprovider.dao.DaoSession;
import com.sh3h.localprovider.dao.HistoryDao;
import com.sh3h.localprovider.dao.MaterialDao;
import com.sh3h.localprovider.dao.MultiMediaDao;
import com.sh3h.localprovider.dao.PersonDao;
import com.sh3h.localprovider.dao.TaskDao;
import com.sh3h.localprovider.dao.TemporaryDao;
import com.sh3h.localprovider.dao.WordDao;
import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.Material;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.localprovider.entity.Person;
import com.sh3h.localprovider.entity.Task;
import com.sh3h.localprovider.entity.Temporary;
import com.sh3h.localprovider.entity.Word;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;


class DataProviderImpl implements IDataProvider {
    private static final String TAG = "DataProviderImpl";
    private final static String MY_PWD = "3h";
    private static final int MAX_NOT_IN_SIZE = 800;
    private Database database;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private HistoryDao historyDao;
    private MultiMediaDao multiMediaDao;
    private WordDao wordDao;
    private MaterialDao materialDao;
    private TemporaryDao temporaryDao;
    private PersonDao personDao;

    @Override
    public synchronized boolean init(String path, Context context) {
        DaoMaster.DevOpenHelper openHelper = new MyDevOpenHelper(context, path, null);
        database = openHelper.getWritableDb();
        //database = openHelper.getEncryptedWritableDb(MY_PWD);
        if (database == null) {
            return false;
        }

        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        daoSession.clear();

        taskDao = daoSession.getTaskDao();
        historyDao = daoSession.getHistoryDao();
        multiMediaDao = daoSession.getMultiMediaDao();
        wordDao = daoSession.getWordDao();
        materialDao = daoSession.getMaterialDao();
        temporaryDao = daoSession.getTemporaryDao();
        personDao = daoSession.getPersonDao();
        return true;
    }

    @Override
    public synchronized void destroy() {
        if (database != null) {
            database.close();
            database = null;
        }
    }

    @Override
    public synchronized void clearAllTables() {
        daoSession.clear();
    }

    @Override
    public Observable<Boolean> updateUploadFlag() {
        List<History> histories = historyDao.queryBuilder()
                .where(HistoryDao.Properties.UploadFlag.eq(Constant.UploadFlag.UPLOADING))
                .list();
        for (History history : histories) {
            history.setUploadFlag(Constant.UploadFlag.NOT_UPLOAD);
        }
        historyDao.updateInTx(histories);

        List<MultiMedia> multiMedias = multiMediaDao.queryBuilder()
                .where(MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.UPLOADING))
                .list();
        for (MultiMedia media : multiMedias) {
            media.setUploadFlag(Constant.UploadFlag.NOT_UPLOAD);
        }
        multiMediaDao.updateInTx(multiMedias);
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> insertTasks(List<Task> downloadList, boolean downloadOrPush,
                                           int type, String account) {
        if (downloadList == null || TextUtil.isNullOrEmpty(account)) {
            return Observable.error(new Throwable("the param is null;"));
        }

        try {
            database.beginTransaction();
            /*把本地的多余的数据删除掉*/
            int downloadSize = downloadList.size();
            if (downloadOrPush && downloadSize <= MAX_NOT_IN_SIZE) {
                List<Long> taskIds = new ArrayList<>();
                for (Task task : downloadList) {
                    taskIds.add(task.getTaskId());
                }

                List<Task> deleteList = taskDao.queryBuilder().
                        where(TaskDao.Properties.Type.eq(type),
                                TaskDao.Properties.Account.eq(account),
                                TaskDao.Properties.TaskId.notIn(taskIds)).list();
                taskDao.deleteInTx(deleteList);
            }

            for (Task insertTask : downloadList) {
                QueryBuilder<Task> builder = taskDao.queryBuilder()
                        .where(TaskDao.Properties.TaskId.eq(insertTask.getTaskId()),
                                TaskDao.Properties.Account.eq(account));
                if (builder.count() < 1) {
                    taskDao.insert(insertTask);
                } else {
                    //服务器为准
                    Task task = builder.unique();
                    insertTask.setId(task.getId());
                    int localState = task.getState();
                    if (localState == Constant.WorkState.BACK ||
                            localState > insertTask.getState()) {
                        insertTask.setState(localState);
                    }
                    taskDao.update(insertTask);
                }
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            return Observable.error(new Throwable("insert taskOrder error;"));
        } finally {
            database.endTransaction();
        }
        return Observable.just(true);
    }

    @Override
    public Observable<List<Task>> getTaskList(TaskConditionDb condition) {
        if (taskDao == null || condition == null || condition.getOperate() == TaskConditionDb.INVALID) {
            return Observable.error(new Throwable("param is null"));
        }

        QueryBuilder<Task> queryBuilder;
        switch (condition.getOperate()) {
            case TaskConditionDb.GET_PAGE_WORK_BY_KEY:
                queryBuilder = taskDao.queryBuilder()
                        .where(TaskDao.Properties.Account.eq(condition.getAccount()),
                                TaskDao.Properties.Type.eq(condition.getType()),
                                TaskDao.Properties.State.in(Constant.WorkState.DISPACHORDER,
                                        Constant.WorkState.RECEIVEORDER, Constant.WorkState.ONSPOT));
                handleTaskLike(queryBuilder, condition.getKey());
                break;
            case TaskConditionDb.GET_PAGE_WORK_BY_TYPE:
                queryBuilder = taskDao.queryBuilder()
                        .where(TaskDao.Properties.Account.eq(condition.getAccount()),
                                TaskDao.Properties.Type.eq(condition.getType()),
                                TaskDao.Properties.State.in(Constant.WorkState.DISPACHORDER,
                                        Constant.WorkState.RECEIVEORDER, Constant.WorkState.ONSPOT));
                break;
            case TaskConditionDb.GET_PAGE_WORK_BY_SUB_TYPE:
                queryBuilder = taskDao.queryBuilder()
                        .where(TaskDao.Properties.Account.eq(condition.getAccount()),
                                TaskDao.Properties.Type.eq(condition.getType()),
                                TaskDao.Properties.SubType.eq(condition.getSubType()),
                                TaskDao.Properties.State.in(Constant.WorkState.DISPACHORDER,
                                        Constant.WorkState.RECEIVEORDER, Constant.WorkState.ONSPOT));
                break;
            case TaskConditionDb.GET_ALREADY_CALL_PAY:
                queryBuilder = taskDao.queryBuilder().where(
                        TaskDao.Properties.Account.eq(condition.getAccount()),
                        TaskDao.Properties.Type.eq(condition.getType()),
                        TaskDao.Properties.State.in(Constant.WorkState.DISPACHORDER,
                                Constant.WorkState.RECEIVEORDER, Constant.WorkState.ONSPOT));
                handleTaskLike(queryBuilder, condition.getKey());
                queryBuilder.join(TaskDao.Properties.TaskId, History.class, HistoryDao.Properties.TaskId)
                        .where(new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case TaskConditionDb.GET_NO_CALL_PAY:
                List<History> allHistories = historyDao.queryBuilder().list();
                List<Long> taskIds = new ArrayList<>();
                for (History history : allHistories) {
                    if (!taskIds.contains(history.getTaskId())) {
                        taskIds.add(history.getTaskId());
                    }
                }
                taskIds = taskIds.size() > MAX_NOT_IN_SIZE
                        ? taskIds.subList(0, MAX_NOT_IN_SIZE) : taskIds;
                queryBuilder = taskDao.queryBuilder().where(
                        TaskDao.Properties.Account.eq(condition.getAccount()),
                        TaskDao.Properties.Type.eq(condition.getType()),
                        TaskDao.Properties.State.in(Constant.WorkState.DISPACHORDER,
                                Constant.WorkState.RECEIVEORDER, Constant.WorkState.ONSPOT));
                handleTaskLike(queryBuilder, condition.getKey());
                queryBuilder.where(TaskDao.Properties.TaskId.notIn(taskIds));
                break;
            case TaskConditionDb.GET_ALL_TASK:
                return taskDao.queryBuilder().rx().list();
            default:
                return Observable.error(new Throwable("param is null"));
        }

        handleSpecialTypeTask(queryBuilder, condition.getType());
        return queryBuilder.offset(condition.getOffset())
                .limit(condition.getLimit())
                .rx()
                .list();
    }

    @Override
    public long getTaskCount(TaskConditionDb condition) {
        if (taskDao == null || condition == null || condition.getOperate() == TaskConditionDb.INVALID) {
            return 0;
        }

        QueryBuilder<Task> queryBuilder = taskDao.queryBuilder()
                .where(TaskDao.Properties.Account.eq(condition.getAccount()),
                        TaskDao.Properties.Type.eq(condition.getType()),
                        TaskDao.Properties.State.in(Constant.WorkState.DISPACHORDER,
                                Constant.WorkState.RECEIVEORDER, Constant.WorkState.ONSPOT));
        handleSpecialTypeTask(queryBuilder, condition.getType());
        return queryBuilder.count();
    }

    @Override
    public Observable<Boolean> updateTaskState(TaskConditionDb condition) {
        if (condition == null) {
            return Observable.error(new Throwable("param is null"));
        }

        if (condition.getOperate() != TaskConditionDb.UPDATE_TASK_LIST_STATE) {
            return Observable.error(new Throwable("param operate is error"));
        }

        List<Task> tasks = condition.getList();
        if (tasks == null || tasks.size() == 0) {
            return Observable.error(new Throwable("param tasks is null"));
        }

        for (Task task : tasks) {
            Task dbTask = taskDao.queryBuilder()
                    .where(TaskDao.Properties.TaskId.eq(task.getTaskId()),
                            TaskDao.Properties.Account.eq(condition.getAccount())).build().unique();
            if (dbTask != null) {
                dbTask.setState(dbTask.getIsAssist() == Constant.Assist.OK
                        ? task.getState() : dbTask.getState());
                dbTask.setHangUpState(task.getHangUpState());
                dbTask.setDelayState(task.getDelayState());
                dbTask.setEndDate(task.getEndDate());
                taskDao.update(dbTask);
            }
        }
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> deleteTask(List<Long> taskIds) {
        if (taskIds == null || taskIds.size() == 0) {
            return Observable.just(false);
        }

        int number = 0;
        int size = taskIds.size();
        int max;
        List<Long> deleteList;
        while (size > MAX_NOT_IN_SIZE * number) {
            max = MAX_NOT_IN_SIZE * (number + 1);
            deleteList = taskIds.subList(number * MAX_NOT_IN_SIZE, size > max ? max : size);
            deleteData(deleteList);
            number++;
        }

        return Observable.just(true);
    }

    @Override
    public Observable<List<History>> getHistoryList(HistoryConditionDb condition) {
        if (condition == null || condition.getOperate() == HistoryConditionDb.INVALID) {
            return Observable.error(new Throwable("the param is null"));
        }

        QueryBuilder<History> queryBuilder;
        List<History> histories;
        switch (condition.getOperate()) {
            //历史界面加载
            case HistoryConditionDb.GET_PAGE_WORK:
                queryBuilder = historyDao.queryBuilder()
                        .where(HistoryDao.Properties.Account.eq(condition.getAccount()),
                                HistoryDao.Properties.UploadFlag.in(Constant.UploadFlag.NOT_UPLOAD,
                                        Constant.UploadFlag.UPLOADING, Constant.UploadFlag.UPLOADED));
                operateHandleType(queryBuilder, condition.getHandleType());
                break;
            //历史界面搜索
            case HistoryConditionDb.GET_PAGE_WORK_BY_KEY:
                queryBuilder = historyDao.queryBuilder()
                        .where(HistoryDao.Properties.Account.eq(condition.getAccount()),
                                HistoryDao.Properties.UploadFlag.in(Constant.UploadFlag.NOT_UPLOAD,
                                        Constant.UploadFlag.UPLOADING, Constant.UploadFlag.UPLOADED))
                        .whereOr(HistoryDao.Properties.Content.like("%" + condition.getKey() + "%"),
                                HistoryDao.Properties.Reply.like("%" + condition.getKey() + "%"));
                queryBuilder.where(new WhereCondition.StringCondition("1 group by " + HistoryDao.Properties.TaskId.columnName));
                break;
            //历史界面筛选
            case HistoryConditionDb.GET_PAGE_WORK_BY_TYPE:
                queryBuilder = historyDao.queryBuilder()
                        .where(HistoryDao.Properties.Account.eq(condition.getAccount()),
                                HistoryDao.Properties.Type.eq(condition.getType()),
                                HistoryDao.Properties.UploadFlag.in(Constant.UploadFlag.NOT_UPLOAD,
                                        Constant.UploadFlag.UPLOADING, Constant.UploadFlag.UPLOADED));
                operateHandleType(queryBuilder, condition.getHandleType());
                break;
            default:
                queryBuilder = historyDao.queryBuilder()
                        .where(HistoryDao.Properties.Account.eq(condition.getAccount()));
                operateHandleType(queryBuilder, condition.getHandleType());
                break;
        }

        histories = queryBuilder.offset(condition.getOffset()).limit(condition.getLimit())
                .orderDesc(HistoryDao.Properties.ReplyTime,
                        HistoryDao.Properties.Type,
                        HistoryDao.Properties.SubType)
                .build().list();
        return Observable.just(histories);
    }

    @Override
    public boolean deleteHistory(History condition) {
        if (condition == null) {
            return false;
        }

        historyDao.delete(condition);
        return true;
    }

    @Override
    public Observable<History> insertHistory(HistoryConditionDb condition) {
        if (condition == null) {
            return Observable.error(new Throwable("params is null"));
        }

        try {
            database.beginTransaction();
            History history = condition.getHistory();
            Task task = taskDao.queryBuilder()
                    .where(TaskDao.Properties.TaskId.eq(history.getTaskId()),
                            TaskDao.Properties.Account.eq(history.getAccount()))
                    .unique();
            if (task != null) {
                if (task.getType() != Constant.WorkType.TASK_CALL_PAY) {
                    task.setState(history.getState());
                }
                task.setHangUpState(history.getHangUpState());
                task.setDelayState(history.getDelayState());
                taskDao.update(task);
            }

            int state = history.getState();
            if (state == Constant.WorkState.HADLER || state == Constant.WorkState.BACK) {
                List<MultiMedia> medias = multiMediaDao.queryBuilder().where(
                        MultiMediaDao.Properties.Account.eq(condition.getAccount()),
                        MultiMediaDao.Properties.TaskId.eq(history.getTaskId()),
                        MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.INVAILD)).list();
                for (MultiMedia media : medias) {
                    media.setUploadFlag(Constant.UploadFlag.NOT_UPLOAD);
                    media.setReplyTime(history.getReplyTime());
                    media.setState(state);
                }
                multiMediaDao.updateInTx(medias);

                temporaryDao.deleteByKey(history.getTaskId());
            }
            historyDao.insertOrReplace(history);
            database.setTransactionSuccessful();
            return Observable.just(history);
        } catch (Exception e) {
            return Observable.error(new Throwable("insertHistory error"));
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public Observable<History> insertReportHistory(HistoryConditionDb condition) {
        if (condition == null) {
            return Observable.error(new Throwable("params is null"));
        }

        try {
            database.beginTransaction();
            History history = condition.getHistory();
            if (history.getState() == Constant.WorkState.HADLER) {
                List<MultiMedia> medias = multiMediaDao.queryBuilder().where(
                        MultiMediaDao.Properties.Account.eq(condition.getAccount()),
                        MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.INVAILD),
                        MultiMediaDao.Properties.TaskId.eq(history.getTaskId())).list();
                for (MultiMedia media : medias) {
                    media.setUploadFlag(Constant.UploadFlag.NOT_UPLOAD);
                    media.setReplyTime(history.getReplyTime());
                }
                multiMediaDao.updateInTx(medias);

                temporaryDao.deleteByKey(history.getTaskId());
            }
            historyDao.insertOrReplace(history);
            database.setTransactionSuccessful();
            return Observable.just(history);
        } catch (Exception e) {
            return Observable.error(new Throwable("insertHistory error"));
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public Observable<Task> insertTaskTemporary(Temporary temporary) {
        if (temporary == null || temporary.getId() == null) {
            return Observable.error(new Exception("insertTaskTemporary param is error"));
        }

        int state = temporary.getState();
        if (state == Constant.WorkState.HADLER || state == Constant.WorkState.BACK) {
            List<MultiMedia> medias = multiMediaDao.queryBuilder().where(
                    MultiMediaDao.Properties.Account.eq(temporary.getAccount()),
                    MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.INVAILD),
                    MultiMediaDao.Properties.TaskId.eq(temporary.getId())).list();
            for (MultiMedia media : medias) {
                media.setReplyTime(temporary.getReplyTime());
                media.setState(state);
            }
            multiMediaDao.updateInTx(medias);
        }

        long result = temporaryDao.insertOrReplace(temporary);
        if (result == -1) {
            return Observable.error(new Exception("insertTaskTemporary insert is error"));
        }

        return taskDao.queryBuilder()
                .where(TaskDao.Properties.TaskId.eq(temporary.getId()),
                        TaskDao.Properties.Account.eq(temporary.getAccount()))
                .rx().unique()
                .map(task -> {
                    taskDao.loadDeep(task.getId());
                    return task;
                });
    }

    @Override
    public Observable<Temporary> insertReportTemporary(Temporary temporary) {
        if (temporary == null || temporary.getId() == null) {
            return Observable.error(new Exception("insertReportTemporary param is error"));
        }
        if (temporary.getState() == Constant.WorkState.HADLER) {
            List<MultiMedia> medias = multiMediaDao.queryBuilder().where(
                    MultiMediaDao.Properties.Account.eq(temporary.getAccount()),
                    MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.INVAILD),
                    MultiMediaDao.Properties.TaskId.eq(temporary.getId())).list();
            for (MultiMedia media : medias) {
                media.setReplyTime(temporary.getReplyTime());
            }
            multiMediaDao.updateInTx(medias);
        }
        long result = temporaryDao.insertOrReplace(temporary);
        if (result == -1) {
            return Observable.error(new Exception("insertTaskTemporary insert is error"));
        }
        return Observable.just(temporary);
    }

    @Override
    public Observable<List<History>> operateHistoryTask(HistoryConditionDb conditionDb) {
        if (conditionDb == null || historyDao == null) {
            return Observable.error(new Throwable("the param is null"));
        }

        switch (conditionDb.getOperate()) {
            case HistoryConditionDb.GET_TASKS_BY_TASK_ID:
                return historyDao.queryBuilder().where(
                        HistoryDao.Properties.Account.eq(conditionDb.getAccount()),
                        HistoryDao.Properties.TaskId.eq(conditionDb.getTaskId())).
                        orderAsc(HistoryDao.Properties.State).
                        rx().list();
            case HistoryConditionDb.GET_UOT_UPLOAD_TASK_ID:
                return historyDao.queryBuilder()
                        .where(HistoryDao.Properties.UploadFlag.eq(conditionDb.getUploadFlag()),
                                HistoryDao.Properties.Account.eq(conditionDb.getAccount()))
                        .orderDesc(HistoryDao.Properties.ReportType)
                        .offset(conditionDb.getOffset())
                        .limit(conditionDb.getLimit())
                        .rx().list();
            case HistoryConditionDb.GET_TASKS_BY_TASK_ID_SET_UPLOADING:
                List<History> histories = historyDao.queryBuilder()
                        .where(HistoryDao.Properties.UploadFlag.eq(Constant.UploadFlag.NOT_UPLOAD),
                                HistoryDao.Properties.TaskId.eq(conditionDb.getTaskId()),
                                HistoryDao.Properties.Account.eq(conditionDb.getAccount()))
                        .orderDesc(HistoryDao.Properties.ReportType)
                        .list();
                for (History history : histories) {
                    history.setUploadFlag(Constant.UploadFlag.UPLOADING);
                }
                historyDao.updateInTx(histories);
                return Observable.just(histories);
            case HistoryConditionDb.GET_NOT_UPLOAD_UPDATE_FALG:
                return historyDao.queryBuilder()
                        .where(HistoryDao.Properties.UploadFlag.eq(conditionDb.getUploadFlag()),
                                HistoryDao.Properties.Account.eq(conditionDb.getAccount()))
                        .orderDesc(HistoryDao.Properties.ReportType)
                        .offset(conditionDb.getOffset())
                        .limit(conditionDb.getLimit())
                        .rx()
                        .list()
                        .concatMap(histories1 -> {
                            for (History history : histories1) {
                                history.setUploadFlag(Constant.UploadFlag.UPLOADING);
                            }
                            historyDao.updateInTx(histories1);
                            return Observable.just(histories1);
                        });
            default:
                return null;
        }
    }

    @Override
    public Observable<List<History>> updateHistory(List<History> histories) {
        if (histories == null || histories.size() == 0 || historyDao == null) {
            Observable.error(new Exception("param is null"));
        }
        return historyDao.rx().updateInTx(histories)
                .map(histories1 -> {
                    List<History> list = new ArrayList<>();
                    for (History history : histories1) {
                        list.add(history);
                    }
                    return list;
                });
    }

    @Override
    public Observable<List<MultiMedia>> getMultiMedias(MultiMediaConditionDb conditionDb) {
        if (conditionDb == null || multiMediaDao == null) {
            return Observable.error(new Exception("param is null"));
        }

        switch (conditionDb.getOperate()) {
            case MultiMediaConditionDb.GET_BY_TASK_ID:
                return multiMediaDao.queryBuilder().where(
                        MultiMediaDao.Properties.Account.eq(conditionDb.getAccount()),
                        MultiMediaDao.Properties.TaskId.eq(conditionDb.getTaskId()),
                        MultiMediaDao.Properties.ReplyTime.eq(conditionDb.getReplayTime())).rx().list();
            case MultiMediaConditionDb.GET_NOT_UPLOAD_MEDIAS_OF_ONE_TASK:
                return multiMediaDao.queryBuilder().where(
                        MultiMediaDao.Properties.Account.eq(conditionDb.getAccount()),
                        MultiMediaDao.Properties.TaskId.eq(conditionDb.getTaskId())).
                        where(MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.NOT_UPLOAD)).
                        rx().list();
            case MultiMediaConditionDb.GET_ALL_NOT_UPLOAD_MEDIAS:
                QueryBuilder<MultiMedia> builder = multiMediaDao.queryBuilder()
                        .where(MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.NOT_UPLOAD),
                                MultiMediaDao.Properties.Account.eq(conditionDb.getAccount()));
                builder.join(MultiMediaDao.Properties.ReplyTime, History.class, HistoryDao.Properties.ReplyTime)
                        .where(HistoryDao.Properties.UploadFlag.eq(Constant.UploadFlag.UPLOADED));
                return builder.orderAsc(MultiMediaDao.Properties.TaskId)
                        .offset(conditionDb.getOffset())
                        .limit(conditionDb.getLimit()).rx().list();
            case MultiMediaConditionDb.GET_ONLY_BY_TASK_ID:
                return multiMediaDao.queryBuilder().where(
                        MultiMediaDao.Properties.Account.eq(conditionDb.getAccount()),
                        MultiMediaDao.Properties.TaskId.eq(conditionDb.getTaskId())).rx().list();
            case MultiMediaConditionDb.GET_NOT_UPLOAD_UPDATE_FLAG:
                return multiMediaDao.queryBuilder()
                        .where(MultiMediaDao.Properties.UploadFlag.eq(Constant.UploadFlag.NOT_UPLOAD),
                                MultiMediaDao.Properties.Account.eq(conditionDb.getAccount()))
                        .orderAsc(MultiMediaDao.Properties.TaskId)
                        .offset(conditionDb.getOffset())
                        .limit(conditionDb.getLimit())
                        .rx()
                        .list()
                        .concatMap(multiMedias -> {
                            for (MultiMedia media : multiMedias) {
                                media.setUploadFlag(Constant.UploadFlag.UPLOADING);
                            }
                            multiMediaDao.updateInTx(multiMedias);
                            return Observable.just(multiMedias);
                        });
            default:
                return Observable.error(new Exception("no data"));
        }
    }

    @Override
    public Observable<List<MultiMedia>> updateMultiMedias(List<MultiMedia> multiMedias) {
        if (multiMedias == null || multiMediaDao == null) {
            return Observable.error(new Exception("param is null"));
        }

        if (multiMedias.size() == 0) {
            return Observable.just(multiMedias);
        }

        return multiMediaDao.rx().updateInTx(multiMedias)
                .map(multiMedias1 -> {
                    List<MultiMedia> list = new ArrayList<>();
                    for (MultiMedia media : multiMedias1) {
                        list.add(media);
                    }
                    return list;
                });
    }

    @Override
    public Observable<History> getHistory(HistoryConditionDb condition) {
        if (condition == null || historyDao == null) {
            return Observable.error(new Exception("param is null"));
        }
        switch (condition.getOperate()) {
            case HistoryConditionDb.GET_BY_TASK_ID:
                return historyDao.queryBuilder()
                        .where(HistoryDao.Properties.TaskId.eq(condition.getTaskId()),
                                HistoryDao.Properties.Account.eq(condition.getAccount())).rx().unique();
            case HistoryConditionDb.GET_ALL_HISTORY:
                return historyDao.queryBuilder()
                        .where(HistoryDao.Properties.Account.eq(condition.getAccount()))
                        .offset(condition.getOffset())
                        .limit(condition.getLimit())
                        .orderDesc(HistoryDao.Properties.ReplyTime).rx().oneByOne();
            case HistoryConditionDb.GET_REPORT_HISTORY:
                return historyDao.queryBuilder()
                        .where(HistoryDao.Properties.Type.eq(Constant.WorkType.TASK_REPORT),
                                HistoryDao.Properties.Account.eq(condition.getAccount()),
                                HistoryDao.Properties.UploadFlag.eq(Constant.UploadFlag.TEMPORARY_STORAGE))
                        .rx().unique();
            default:
                return Observable.error(new Exception("operate is null"));
        }
    }

    @Override
    public Observable<Temporary> getReportTemporary(TemporaryConditionDb condition) {
        if (condition == null || condition.getOperate() != TemporaryConditionDb.GET_REPORT_TEMPORARY) {
            return Observable.error(new Exception("getReportTemporary parm error"));
        }
        return temporaryDao.queryBuilder()
                .where(TemporaryDao.Properties.Account.eq(condition.getAccount()),
                        TemporaryDao.Properties.Type.eq(Constant.WorkType.TASK_REPORT))
                .rx().unique();
    }

    @Override
    public Observable<Boolean> deleteAllData() {
        wordDao.deleteAll();
        historyDao.deleteAll();
        multiMediaDao.deleteAll();
        taskDao.deleteAll();
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> insertWords(List<Word> words) {
        if (words == null) {
            return Observable.error(new Throwable("the param is null;"));
        }

        return wordDao.rx().deleteAll()
                .concatMap(aVoid -> wordDao.rx().insertInTx(words))
                .concatMap(tasks -> Observable.just(true));
    }

    @Override
    public Observable<Boolean> insertMaterials(List<Material> materials) {
        if (materials == null) {
            return Observable.error(new Throwable("the param is null;"));
        }

        return materialDao.rx().deleteAll()
                .concatMap(aVoid -> materialDao.rx().insertInTx(materials))
                .concatMap(tasks -> Observable.just(true));
    }

    @Override
    public Observable<Boolean> savePersons(List<Person> people) {
        if (people == null) {
            return Observable.error(new Throwable("the param is null;"));
        }

        return personDao.rx().deleteAll()
                .concatMap(aVoid -> personDao.rx().insertInTx(people))
                .concatMap(person -> Observable.just(true));
    }

    @Override
    public Observable<MultiMedia> insertMedia(MultiMedia multiMedia) {
        if (multiMediaDao == null || multiMedia == null) {
            return Observable.error(new Throwable("params is error"));
        }

        String fileName = multiMedia.getFileName();
        if (TextUtil.isNullOrEmpty(fileName)) {
            return Observable.error(new Throwable("fileName cannot null"));
        }

        MultiMedia media = multiMediaDao.queryBuilder()
                .where(MultiMediaDao.Properties.FileName.eq(fileName)).unique();
        if (media != null) {
            return Observable.error(new Throwable("fileName is exist"));
        }

        LogUtil.i(TAG, "insertMedia:%s", multiMedia.toString());
        multiMediaDao.insert(multiMedia);
        multiMedia = multiMediaDao.queryBuilder()
                .where(MultiMediaDao.Properties.FileName.eq(fileName)).unique();
        return Observable.just(multiMedia);
    }

    @Override
    public Observable<Void> deleteMedia(MultiMedia multiMedia) {
        if (multiMediaDao == null || multiMedia == null) {
            return Observable.error(new Throwable("params is error"));
        }

        LogUtil.i(TAG, "deleteMedia:%s", multiMedia.toString());
        return multiMediaDao.rx().delete(multiMedia);
    }

    @Override
    public boolean deleteMedia(List<MultiMedia> condition) {
        if (condition == null || condition.size() == 0) {
            return false;
        }

        for (MultiMedia multiMedia : condition) {
            LogUtil.i(TAG, "deleteMedias:%s", multiMedia.toString());
        }
        multiMediaDao.deleteInTx(condition);
        return true;
    }

    @Override
    public Observable<List<Material>> getMaterial(MaterialConditionDb condition) {
        if (condition == null || condition.getMaterial() == null) {
            return Observable.error(new Throwable("getMaterial param is error"));
        }

        Material material = condition.getMaterial();
        switch (condition.getOperate()) {
            case MaterialConditionDb.GET_TYPE:
                return materialDao.queryBuilder()
                        .where(new WhereCondition.StringCondition("1 group by " + MaterialDao.Properties.Type.columnName))
                        .rx().list();
            case MaterialConditionDb.GET_NAME:
                return materialDao.queryBuilder().where(MaterialDao.Properties.Type.eq(material.getType()),
                        new WhereCondition.StringCondition("1 group by " + MaterialDao.Properties.Name.columnName))
                        .orderDesc(MaterialDao.Properties.Price).rx().list();
            case MaterialConditionDb.GET_STANDARD:
                return materialDao.queryBuilder().where(MaterialDao.Properties.Type.eq(material.getType()),
                        MaterialDao.Properties.Name.eq(material.getName()),
                        new WhereCondition.StringCondition("1 group by " + MaterialDao.Properties.Spec.columnName))
                        .orderAsc(MaterialDao.Properties.Price).rx().list();
            case MaterialConditionDb.GET_UNIT:
                return materialDao.queryBuilder().where(MaterialDao.Properties.Type.eq(material.getType()),
                        MaterialDao.Properties.Name.eq(material.getName()),
                        MaterialDao.Properties.Spec.eq(material.getSpec()),
                        new WhereCondition.StringCondition("1 group by " + MaterialDao.Properties.Unit.columnName))
                        .orderDesc(MaterialDao.Properties.Price).rx().list();
            case MaterialConditionDb.GET_PRICE:
                return materialDao.queryBuilder().distinct().where(MaterialDao.Properties.Type.eq(material.getType()),
                        MaterialDao.Properties.Name.eq(material.getName()),
                        MaterialDao.Properties.Spec.eq(material.getSpec()),
                        MaterialDao.Properties.Unit.eq(material.getUnit()))
                        .orderDesc(MaterialDao.Properties.Price).rx().list();
            default:
                return Observable.error(new Throwable("getMaterial operate is error"));
        }
    }

    @Override
    public Observable<List<Word>> getWord(WordConditionDb condition) {
        if (condition == null) {
            return Observable.error(new Exception("getWord condition is null;"));
        }

        return Observable.error(new Exception("getWord operate error"));
    }

    @Override
    public Observable<List<Word>> getWord(long parentId, String group) {
        if (parentId <= 0 || TextUtil.isNullOrEmpty(group)) {
            return Observable.error(new Exception("parentId error or group null."));
        }
        return wordDao.queryBuilder()
                .where(WordDao.Properties.ParentId.eq(parentId),
                        WordDao.Properties.Group.eq(group))
                .orderAsc(WordDao.Properties.Id).rx().list();
    }

    @Override
    public Observable<List<Word>> getOneGroup(String group) {
        if (TextUtil.isNullOrEmpty(group)) {
            return Observable.error(new Exception("group null."));
        }
        return wordDao.queryBuilder()
                .where(WordDao.Properties.Group.eq(group))
                .orderAsc(WordDao.Properties.Id)
                .rx().list();
    }

    @Override
    public Observable<List<Word>> getCurrentStation(String account, String stationGroup) {
        if (TextUtil.isNullOrEmpty(account)) {
            return Observable.error(new Exception("getCurrentStation account is null"));
        }

        if (personDao == null || wordDao == null) {
            return Observable.error(new Exception("getCurrentStation personDao or wordDao is null"));
        }

        return Observable.create(subscriber -> {
            Person person = personDao.queryBuilder().where(PersonDao.Properties.Account.eq(account)).unique();
            if (person == null || TextUtil.isNullOrEmpty(person.getStation())) {
                subscriber.onError(new Exception("getCurrentStation person is null"));
                return;
            }

            List<Word> stationList = wordDao.queryBuilder().where(WordDao.Properties.Group.eq(stationGroup)).list();
            if (stationList == null || stationList.size() == 0) {
                subscriber.onError(new Exception("getCurrentStation stationList is null"));
                return;
            }

            String station = person.getStation();
            Word stationWord = null;
            for (Word word : stationList) {
                if (station.equals(word.getValue())) {
                    stationWord = word;
                    break;
                }
            }

            if (stationWord == null) {
                subscriber.onError(new Exception("getCurrentStation stationWord is null"));
                return;
            }

            List<Word> filterWords = new ArrayList<>();
            filterWords.add(stationWord);

            List<Long> parentIdList = new ArrayList<>();
            parentIdList.add(stationWord.getServerId());
            List<Long> tempList;
            do {
                tempList = new ArrayList<>();
                for (Word word : stationList) {
                    if (parentIdList.contains(word.getParentId())) {
                        tempList.add(word.getServerId());
                        filterWords.add(word);
                    }
                }
                parentIdList = tempList;
            } while (parentIdList.size() > 0);

            subscriber.onNext(filterWords);
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<List<Person>> getAllPerson() {
        return personDao.queryBuilder().rx().list();
    }

    private void operateHandleType(QueryBuilder<History> queryBuilder, int handleType) {
        switch (handleType) {
            case Constant.HandleType.ALL:
                queryBuilder.where(new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.HANDLE:
                queryBuilder.where(HistoryDao.Properties.State.eq(Constant.WorkState.HADLER),
                        new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.REJECT:
                queryBuilder.where(HistoryDao.Properties.State.eq(Constant.WorkState.BACK),
                        new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.DELAY:
                queryBuilder.where(HistoryDao.Properties.ReportType.eq(Constant.ReportType.Apply),
                        HistoryDao.Properties.DelayState.eq(Constant.DelayState.REPORT_DELAY),
                        new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.HANG_UP:
                queryBuilder.where(HistoryDao.Properties.ReportType.eq(Constant.ReportType.Apply),
                        HistoryDao.Properties.HangUpState.eq(Constant.HangUpState.WAIT_HANG_UP),
                        new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.RESTORE:
                queryBuilder.where(HistoryDao.Properties.ReportType.eq(Constant.ReportType.Apply),
                        HistoryDao.Properties.HangUpState.eq(Constant.HangUpState.WAIT_RECOVERY),
                        new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.ASSIST:
                queryBuilder.where(HistoryDao.Properties.ReportType.eq(Constant.ReportType.Assist),
                        new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.SPOT:
                queryBuilder.where(HistoryDao.Properties.ReportType.eq(Constant.ReportType.Handle));
                queryBuilder.join(HistoryDao.Properties.TaskId, Task.class, TaskDao.Properties.TaskId)
                        .where(TaskDao.Properties.State.eq(Constant.WorkState.ONSPOT),
                                new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            case Constant.HandleType.ACCEPT:
                queryBuilder.where(HistoryDao.Properties.ReportType.eq(Constant.ReportType.Handle));
                queryBuilder.join(HistoryDao.Properties.TaskId, Task.class, TaskDao.Properties.TaskId)
                        .where(TaskDao.Properties.State.eq(Constant.WorkState.RECEIVEORDER),
                                new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
            default:
                queryBuilder.where(new WhereCondition.StringCondition("1 group by T." + HistoryDao.Properties.TaskId.columnName));
                break;
        }
    }

    private void handleTaskLike(QueryBuilder<Task> queryBuilder, String key) {
        if (TextUtil.isNullOrEmpty(key)) {
            return;
        }

        queryBuilder.whereOr(TaskDao.Properties.TaskId.like("%" + key + "%"),
                TaskDao.Properties.Address.like("%" + key + "%"),
                TaskDao.Properties.BarCode.like("%" + key + "%"),
                TaskDao.Properties.CardId.like("%" + key + "%"),
                TaskDao.Properties.CardName.like("%" + key + "%"));
    }

    private void handleSpecialTypeTask(QueryBuilder<Task> queryBuilder, int type) {
        switch (type) {
            case Constant.WorkType.TASK_CALL_PAY:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                queryBuilder.where(TaskDao.Properties.DispatchTime.ge(calendar.getTimeInMillis()));
                queryBuilder.orderAsc(TaskDao.Properties.Volume, TaskDao.Properties.VolumeIndex);
                break;
            case Constant.WorkType.TASK_BIAOWU:
                queryBuilder.orderAsc(TaskDao.Properties.Type, TaskDao.Properties.SubType)
                        .orderDesc(TaskDao.Properties.DispatchTime)
                        .orderAsc(TaskDao.Properties.Address);
                break;
            default:
                queryBuilder.orderDesc(TaskDao.Properties.DispatchTime);
                break;
        }
    }

    private void deleteData(List<Long> list) {
        taskDao.queryBuilder()
                .where(TaskDao.Properties.TaskId.in(list))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        historyDao.queryBuilder()
                .where(HistoryDao.Properties.TaskId.in(list))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        multiMediaDao.queryBuilder()
                .where(MultiMediaDao.Properties.TaskId.in(list))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

}
