package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2018/1/31.
 */

public class PushLeader {
    private int taskId;
    private int applyType; //  number,-1：默认；0: 延期, 1: 挂起, 2: 恢复,3:协助...
    private long processTime;
    private String extend;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

}
