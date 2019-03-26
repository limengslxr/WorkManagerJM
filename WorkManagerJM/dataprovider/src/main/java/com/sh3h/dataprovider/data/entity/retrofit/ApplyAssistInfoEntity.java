package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * 申请协助
 */
public class ApplyAssistInfoEntity {
    private long taskId;//任务编号
    private int type;//类型
    private int subType;//子类型
    private int state;// 状态
    private long assistTime;// 协助时间
    private int assistPersonCount;//协助人数
    private String remark;// 备注
    private String extend;// 'json string' // 扩展信息, 可为空

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
