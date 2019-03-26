package com.sh3h.dataprovider.data.entity.ui;

/**
 * Created by limeng on 2016/12/21.
 * 协助
 */
public class DUAssistHandle{
    private long taskId;
    private int type;
    private int subType;
    private int state;
    private long assistTime;
    private int assistPersonCount;
    private String remark;
    private String extend;

    public DUAssistHandle() {
    }

    public DUAssistHandle(long taskId, int type, int subType, int state,
                          long assistTime, int assistPersonCount, String remark) {
        this.taskId = taskId;
        this.type = type;
        this.subType = subType;
        this.state = state;
        this.assistTime = assistTime;
        this.assistPersonCount = assistPersonCount;
        this.remark = remark;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getAssistTime() {
        return assistTime;
    }

    public void setAssistTime(long assistTime) {
        this.assistTime = assistTime;
    }

    public int getAssistPersonCount() {
        return assistPersonCount;
    }

    public void setAssistPersonCount(int assistPersonCount) {
        this.assistPersonCount = assistPersonCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

}
