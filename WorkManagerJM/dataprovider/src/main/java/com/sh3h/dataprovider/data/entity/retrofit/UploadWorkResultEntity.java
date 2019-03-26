package com.sh3h.dataprovider.data.entity.retrofit;


public class UploadWorkResultEntity {
    private boolean isSuccess;// 'true',
    private String message;
    private int taskId;// '任务编号', // integer
    private int state;// '状态', // integer
    private String extend;// 'json string' // 扩展信息

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
