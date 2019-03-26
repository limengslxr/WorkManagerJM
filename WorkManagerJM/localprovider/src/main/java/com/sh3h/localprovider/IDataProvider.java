/**
 * @author qiweiwei
 */
package com.sh3h.localprovider;

import android.content.Context;

import com.sh3h.localprovider.condition.HistoryConditionDb;
import com.sh3h.localprovider.condition.MaterialConditionDb;
import com.sh3h.localprovider.condition.MultiMediaConditionDb;
import com.sh3h.localprovider.condition.TaskConditionDb;
import com.sh3h.localprovider.condition.TemporaryConditionDb;
import com.sh3h.localprovider.condition.WordConditionDb;
import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.Material;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.localprovider.entity.Person;
import com.sh3h.localprovider.entity.Task;
import com.sh3h.localprovider.entity.Temporary;
import com.sh3h.localprovider.entity.Word;

import java.util.List;

import rx.Observable;


/**
 * IMeterReadingDataProvider
 */
public interface IDataProvider {
    boolean init(String path, Context context);

    void destroy();

    void clearAllTables();

    Observable<Boolean> updateUploadFlag();

    /**
     * @param list 任务集
     * @return 插入任务
     */
    Observable<Boolean> insertTasks(List<Task> list, boolean downloadOrPush, int type, String account);

    /**
     * @param condition 条件
     * @return 根据条件获取任务
     */
    Observable<List<Task>> getTaskList(TaskConditionDb condition);

    long getTaskCount(TaskConditionDb condition);

    Observable<Boolean> updateTaskState(TaskConditionDb condition);

    Observable<Boolean> deleteTask(List<Long> taskIds);

    /**
     * @param condition 条件
     * @return 根据条件获取历史纪录
     */
    Observable<List<History>> getHistoryList(HistoryConditionDb condition);

    boolean deleteHistory(History condition);

    Observable<History> insertHistory(HistoryConditionDb condition);

    Observable<History> insertReportHistory(HistoryConditionDb condition);

    Observable<Task> insertTaskTemporary(Temporary temporary);

    Observable<Temporary> insertReportTemporary(Temporary temporary);

    Observable<Boolean> insertWords(List<Word> words);

    Observable<Boolean> insertMaterials(List<Material> materials);

    Observable<Boolean> savePersons(List<Person> people);

    Observable<MultiMedia> insertMedia(MultiMedia condition);

    Observable<Void> deleteMedia(MultiMedia condition);

    boolean deleteMedia(List<MultiMedia> condition);

    Observable<List<History>> operateHistoryTask(HistoryConditionDb conditionDb);

    Observable<List<History>> updateHistory(List<History> history);

    Observable<List<MultiMedia>> getMultiMedias(MultiMediaConditionDb conditionDb);

    Observable<List<MultiMedia>> updateMultiMedias(List<MultiMedia> multiMedias);

    Observable<History> getHistory(HistoryConditionDb condition);

    Observable<Temporary> getReportTemporary(TemporaryConditionDb condition);

    Observable<Boolean> deleteAllData();

    Observable<List<Material>> getMaterial(MaterialConditionDb condition);

    Observable<List<Word>> getWord(WordConditionDb condition);

    Observable<List<Word>> getWord(long parentId, String group);

    Observable<List<Word>> getOneGroup(String group);

    Observable<List<Word>> getCurrentStation(String account, String stationGroup);

    Observable<List<Person>> getAllPerson();
}
