package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * 自开单接口下载结果
 */
public class UploadReportResult {
    private boolean isSuccess;//true
    private String message;//
    private long localTaskId;//本地任务编号
    private long serverTaskId;//服务器上任务编号
    private String extend; //扩展信息, 可为空

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

    public long getLocalTaskId() {
        return localTaskId;
    }

    public void setLocalTaskId(long localTaskId) {
        this.localTaskId = localTaskId;
    }

    public long getServerTaskId() {
        return serverTaskId;
    }

    public void setServerTaskId(long serverTaskId) {
        this.serverTaskId = serverTaskId;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
