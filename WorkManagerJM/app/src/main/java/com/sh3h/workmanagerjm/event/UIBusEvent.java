package com.sh3h.workmanagerjm.event;

import com.sh3h.dataprovider.data.entity.retrofit.DeleteTask;
import com.sh3h.dataprovider.data.entity.retrofit.DeleteVerifyTask;
import com.sh3h.dataprovider.data.entity.retrofit.MessageUpdate;
import com.sh3h.dataprovider.data.entity.retrofit.MeterCard;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsResult;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;

import java.util.List;

/**
 * Created by 李猛 on 2016/9/9.
 * 传递数据
 */
public class UIBusEvent {

    //申请协助
    public static class ApplyAssist {
        private DUHandle handle;

        public ApplyAssist(DUHandle handle) {
            this.handle = handle;
        }

        public DUHandle getHandle() {
            return handle;
        }

        public void setHandle(DUHandle handle) {
            this.handle = handle;
        }
    }

    public static class HandleTask {
        private long taskId;

        public long getTaskId() {
            return taskId;
        }

        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }
    }

    public static class UploadOneTaskResult {
        private boolean isSuccess;
        private long taskId;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public long getTaskId() {
            return taskId;
        }

        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }
    }

    public static class UploadOneTaskMediaResult {
        private boolean isSuccess;
        private long taskId;
        private int mediaNumber;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public long getTaskId() {
            return taskId;
        }

        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }

        public int getMediaNumber() {
            return mediaNumber;
        }

        public void setMediaNumber(int mediaNumber) {
            this.mediaNumber = mediaNumber;
        }
    }

    public static class SyncTask {
        private boolean isSuccess;
        private int message;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public int getMessage() {
            return message;
        }

        public void setMessage(int message) {
            this.message = message;
        }
    }

    public static class DownloadTask {
        private boolean isSuccess;
        private int message;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public int getMessage() {
            return message;
        }

        public void setMessage(int message) {
            this.message = message;
        }
    }

    public static class SearchTask {
        private boolean isSuccess;
        private int message;
        private List<DUData> list;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public int getMessage() {
            return message;
        }

        public void setMessage(int message) {
            this.message = message;
        }

        public List<DUData> getList() {
            return list;
        }

        public void setList(List<DUData> list) {
            this.list = list;
        }
    }

    public static class DispatchTask {
        private boolean isSuccess;
        private int message;
        private List<DUData> list;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public int getMessage() {
            return message;
        }

        public void setMessage(int message) {
            this.message = message;
        }

        public List<DUData> getList() {
            return list;
        }

        public void setList(List<DUData> list) {
            this.list = list;
        }
    }

    public static class VerifyTask {
        private boolean isSuccess;
        private int message;
        private List<DUData> list;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public int getMessage() {
            return message;
        }

        public void setMessage(int message) {
            this.message = message;
        }

        public List<DUData> getList() {
            return list;
        }

        public void setList(List<DUData> list) {
            this.list = list;
        }
    }

    public static class StatisticsTask {
        private boolean isSuccess;
        private int message;
        private List<StatisticsResult> list;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public int getMessage() {
            return message;
        }

        public void setMessage(int message) {
            this.message = message;
        }

        public List<StatisticsResult> getList() {
            return list;
        }

        public void setList(List<StatisticsResult> list) {
            this.list = list;
        }
    }

    public static class NetworkNotConnect {
        private int operate;

        public int getOperate() {
            return operate;
        }

        public void setOperate(int operate) {
            this.operate = operate;
        }
    }

    public static class DownloadMeterCard {
        private boolean isSuccess;
        private String cardId;
        private MeterCard meterCard;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public MeterCard getMeterCard() {
            return meterCard;
        }

        public void setMeterCard(MeterCard meterCard) {
            this.meterCard = meterCard;
        }
    }

    //保存数据
    public static class TemporaryResult {

    }

    public static class MultipleTask {
        private List<Integer> types;

        public List<Integer> getTypes() {
            return types;
        }

        public void setTypes(List<Integer> types) {
            this.types = types;
        }
    }

    public static class UpdateTaskState {
        private List<MessageUpdate> updateStates;

        public List<MessageUpdate> getUpdateStates() {
            return updateStates;
        }

        public void setUpdateStates(List<MessageUpdate> updateStates) {
            this.updateStates = updateStates;
        }
    }

    public static class DeleteTasks {
        private String entrance;
        private List<DeleteTask> deleteTasks;

        public DeleteTasks(List<DeleteTask> deleteTasks) {
            this.deleteTasks = deleteTasks;
        }

        public DeleteTasks(String entrance, List<DeleteTask> deleteTasks) {
            this.entrance = entrance;
            this.deleteTasks = deleteTasks;
        }

        public List<DeleteTask> getDeleteTasks() {
            return deleteTasks;
        }

        public void setDeleteTasks(List<DeleteTask> deleteTasks) {
            this.deleteTasks = deleteTasks;
        }

        public String getEntrance() {
            return entrance;
        }

        public void setEntrance(String entrance) {
            this.entrance = entrance;
        }
    }

    public static class DeleteVerifyTasks {
        private List<DeleteVerifyTask> deleteTasks;

        public DeleteVerifyTasks(List<DeleteVerifyTask> deleteTasks) {
            this.deleteTasks = deleteTasks;
        }

        public List<DeleteVerifyTask> getDeleteTasks() {
            return deleteTasks;
        }

        public void setDeleteTasks(List<DeleteVerifyTask> deleteTasks) {
            this.deleteTasks = deleteTasks;
        }
    }

    public static class ApplyVerify {

    }

    public static class TransformStation {

    }

    public static class SingleDispatch {
        private long taskId;

        public SingleDispatch(long taskId) {
            this.taskId = taskId;
        }

        public long getTaskId() {
            return taskId;
        }

        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }
    }

    public static class SingleVerify {
        private long taskId;
        private int applyType;

        public SingleVerify(long taskId, int applyType) {
            this.taskId = taskId;
            this.applyType = applyType;
        }

        public long getTaskId() {
            return taskId;
        }

        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }

        public int getApplyType() {
            return applyType;
        }

        public void setApplyType(int applyType) {
            this.applyType = applyType;
        }
    }

}
