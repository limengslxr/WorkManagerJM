package com.sh3h.dataprovider.condition;

/**
 * Created by limeng on 2016/10/25.
 * 多媒体
 */

public class MultiMediaCondition {
    //无效值
    public static final int INVALID = 0;
    public static final int GET_BY_TASK_ID = 1;
    public static final int GET_REPORT_MEDIA = 2;
    //执行哪种操作
    private int operate;
    //任务号
    private long taskId;
    //時間
    private long replayTime;
    //账号
    private String account;

    public MultiMediaCondition() {
        operate = INVALID;
    }

    public MultiMediaCondition(int operate) {
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

    public long getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(long replayTime) {
        this.replayTime = replayTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
