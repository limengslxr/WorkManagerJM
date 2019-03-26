package com.sh3h.localprovider.condition;


/**
 * Created by limeng on 2016/10/25.
 * 多媒体
 */

public class MultiMediaConditionDb {

    //无效值
    public static final int INVALID = 0;
    public static final int GET_BY_TASK_ID = 1;
    //获取所有未上传的工单
    public static final int GET_ALL_NOT_UPLOAD_MEDIAS = 2;
    //获取一个工单的所有未上传记录
    public static final int GET_NOT_UPLOAD_MEDIAS_OF_ONE_TASK = 3;
    public static final int GET_ONLY_BY_TASK_ID = 10;
    //上报未上传的图片，并修改上传标志
    public static final int GET_NOT_UPLOAD_UPDATE_FLAG = 40;

    //执行哪种操作
    private int operate;
    //任务号
    private long taskId;
    private String workId;
    private int workType;
    private int workSubType;
    private int workState;
    private int uploadFlag;
    private long replayTime;
    private int offset;
    private int limit;
    private String account;

    public MultiMediaConditionDb() {
        operate = INVALID;
    }

    public MultiMediaConditionDb(int operate) {
        this.operate = operate;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public int getWorkSubType() {
        return workSubType;
    }

    public void setWorkSubType(int workSubType) {
        this.workSubType = workSubType;
    }

    public int getWorkState() {
        return workState;
    }

    public void setWorkState(int workState) {
        this.workState = workState;
    }

    public int getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(long replayTime) {
        this.replayTime = replayTime;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
