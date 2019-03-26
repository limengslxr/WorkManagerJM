package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2018/1/31.
 */

public class DeleteVerifyTask {
    private long taskId;
    private int applyType;//上报类型 ,-1：默认；0: 延期, 1: 挂起, 2: 恢复,3:协助...

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
