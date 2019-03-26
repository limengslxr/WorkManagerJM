package com.sh3h.dataprovider.data.entity.retrofit;


public class MessageUpdate {
    private long taskId;//任务编号
    private int state;//状态
    private int hangUpState;//挂起状态
    private int delayState;//延期状态
    private long processTime;//回复时间/施工时间
    private String extend;//可为空

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHangUpState() {
        return hangUpState;
    }

    public void setHangUpState(int hangUpState) {
        this.hangUpState = hangUpState;
    }

    public int getDelayState() {
        return delayState;
    }

    public void setDelayState(int delayState) {
        this.delayState = delayState;
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
