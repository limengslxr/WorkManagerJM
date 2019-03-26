package com.sh3h.localprovider;

import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.sh3h.localprovider.condition.Constant;
import com.sh3h.localprovider.condition.HistoryConditionDb;
import com.sh3h.localprovider.condition.TaskConditionDb;
import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.localprovider.entity.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotNull;

/**
 * Created by LiMeng on 2017/8/18.
 * 单元测试
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DataProviderImplTest {
    private DataProviderImpl provider;
    private static final String TAG = "DataProviderImplTest";
    private Random random;

    @Before
    public void setUp() throws Exception {
        provider = new DataProviderImpl();
        random = new Random();
        File file = new File(Environment.getExternalStorageDirectory(), "sh3h/workmanagerjm/data/main.cbj");
        Log.i(TAG, "数据库存在：" + file.exists());
        provider.init(file.getPath(), InstrumentationRegistry.getTargetContext());
        provider.deleteAllData().subscribe(aBoolean -> Log.i(TAG, "清空数据库：" + aBoolean));
    }

    @Test
    public void init() throws Exception {
        assertNotNull(provider);
    }

    @Test
    public void destroy() throws Exception {
        Log.i(TAG, "销毁数据库");
    }

    @Test
    public void clearAllTables() throws Exception {
        Log.i(TAG, "清空数据库");
    }

    @Test
    public void getAllNotUploadHistoryNumber() throws Exception {
        History history = createHistory();
        HistoryConditionDb conditionDb = new HistoryConditionDb();
        conditionDb.setOperate(HistoryConditionDb.INSERT);
        conditionDb.setHistory(history);
        provider.insertHistory(conditionDb).subscribe(aBoolean -> {
            Log.i(TAG, "插入一条未上传的数据：" + aBoolean);
            assertEquals(provider.getAllNotUploadHistoryNumber(), 1L);
        });
    }

    @Test
    public void getAllNotUploadMediaNumber() throws Exception {
        MultiMedia media = createMultiMedia();
        provider.insertMedia(media).subscribe(aBoolean -> {
            Log.i(TAG, "插入一条未上传的数据：" + aBoolean);
            assertEquals(provider.getAllNotUploadMediaNumber(), 1L);
        });
    }

    @Test
    public void updateUploadFlag() throws Exception {
        History history = createHistory();
        history.setUploadFlag(Constant.UploadFlag.UPLOADING);
        HistoryConditionDb conditionDb = new HistoryConditionDb();
        conditionDb.setOperate(HistoryConditionDb.INSERT);
        conditionDb.setHistory(history);
        provider.insertHistory(conditionDb)
                .concatMap(new Func1<Task, Observable<MultiMedia>>() {
                    @Override
                    public Observable<MultiMedia> call(Task task) {
                        MultiMedia media = createMultiMedia();
                        media.setUploadFlag(Constant.UploadFlag.UPLOADING);
                        return provider.insertMedia(media);
                    }
                })
                .concatMap(new Func1<MultiMedia, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(MultiMedia multiMedia) {
                        return provider.updateUploadFlag();
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fail("更新未上传标志错误");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        assertTrue(aBoolean);
                    }
                });
    }

    @Test
    public void insertTasks() throws Exception {
        provider.insertTasks(createTasks(), random.nextBoolean(), Constant.WorkType.TASK_BIAOWU)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fail("批量插入任务失败");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        assertTrue(aBoolean);
                    }
                });
    }

    @Test
    public void getTaskList() throws Exception {
        provider.insertTasks(createTasks(), random.nextBoolean(), Constant.WorkType.TASK_BIAOWU)
                .concatMap(aBoolean -> {
                    TaskConditionDb conditionDb = new TaskConditionDb();
                    conditionDb.setOperate(TaskConditionDb.GET_ALL_TASK);
                    return provider.getTaskList(conditionDb);
                })
                .subscribe(new Subscriber<List<Task>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fail("获取任务列表失败");
                    }

                    @Override
                    public void onNext(List<Task> tasks) {
                        assertTrue(tasks.size() > 0);
                    }
                });
    }

    @Test
    public void updateTaskState() throws Exception {
//        List<Task> list = createTasks();
//        final int state = random.nextInt();
//        provider.insertTasks(list, random.nextBoolean(), Constant.WorkType.TASK_BIAOWU)
//                .concatMap((Func1<Boolean, Observable<?>>) aBoolean -> {
//                    TaskConditionDb conditionDb = new TaskConditionDb();
//                    conditionDb.setOperate(TaskConditionDb.UPDATE_TASK_LIST_STATE);
//                    conditionDb.setList(list);
//                    for (Task task : list){
//                        task.setState(state);
//                    }
//                    return provider.updateTaskState(conditionDb);
//                })
//                .concatMap(new Func1<Object, Observable<?>>() {
//                })

    }

    @Test
    public void getHistoryList() throws Exception {
        Log.i(TAG, "获取历史记录");
    }

    @Test
    public void deleteHistory() throws Exception {
        Log.i(TAG, "删除历史记录");
    }

    @Test
    public void insertHistory() throws Exception {
        Log.i(TAG, "插入历史记录");
    }

    @Test
    public void insertReportHistory() throws Exception {
        Log.i(TAG, "插入上报历史记录");
    }

    @Test
    public void operateHistoryTask() throws Exception {
        Log.i(TAG, "操作任务历史记录");
    }

    @Test
    public void updateHistory() throws Exception {
        Log.i(TAG, "更新历史表");
    }

    @Test
    public void getMultiMedias() throws Exception {
        Log.i(TAG, "获取多媒体");
    }

    @Test
    public void updateMultiMedias() throws Exception {
        Log.i(TAG, "更新多媒体");
    }

    @Test
    public void getHistory() throws Exception {
        Log.i(TAG, "获取历史记录");
    }

    @Test
    public void updateSingleHistory() throws Exception {
        Log.i(TAG, "更新一条历史记录");
    }

    @Test
    public void updateUploadingFlag() throws Exception {
        Log.i(TAG, "更新上传标志");
    }

    @Test
    public void deleteAllData() throws Exception {
        Log.i(TAG, "删除所有数据");
    }

    @Test
    public void insertWords() throws Exception {
        Log.i(TAG, "插入词语");
    }

    @Test
    public void insertMaterials() throws Exception {
        Log.i(TAG, "插入材料");
    }

    @Test
    public void insertMedia() throws Exception {
        Log.i(TAG, "插入多媒体");
    }

    @Test
    public void deleteMedia() throws Exception {
        Log.i(TAG, "删除多媒体");
    }

    @Test
    public void deleteMedia1() throws Exception {
        Log.i(TAG, "删除多媒体1");
    }

    @Test
    public void getMaterial() throws Exception {
        Log.i(TAG, "获取材料");
    }

    @Test
    public void getWord() throws Exception {
        Log.i(TAG, "获取词语");
    }

    private History createHistory(){
        return  new History(random.nextLong(), "289", random.nextLong(), 1, 2, 5, 0, 0, 1,
                0, "", "", random.nextLong(), Constant.UploadFlag.NOT_UPLOAD, "");
    }

    private MultiMedia createMultiMedia(){
        return new MultiMedia(random.nextLong(), "289", random.nextLong(), 1, 2, 5, 1,
                "picture.jpg", random.nextLong(), "qwer", "12wqewe", Constant.UploadFlag.NOT_UPLOAD, "");
    }

    private Task createTask(){
        return null;
    }

    private List<Task> createTasks(){
        List<Task> list = new ArrayList<>();
        Task one = createTask();
        Task two = createTask();
        two.setTaskId(one.getTaskId() + 1);
        list.add(one);
        list.add(two);
        return list;
    }

}