package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2017/4/7.
 */

public class ApplyInfoEntity {
    private long taskId;//任务编号
    private int type;//类型
    private int subType;//子类型
    private int state;//状态
    private int applyType;// 0: 延期, 1: 挂起, 2: 恢复
    private long applyTime;//申请时间
    private long applyTimeEx;//延期时间
    private String reason;//延期原因、挂起原因
    private String remark;//备注
    private String extend;// 扩展信息, 可为空

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

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getApplyTimeEx() {
        return applyTimeEx;
    }

    public void setApplyTimeEx(long applyTimeEx) {
        this.applyTimeEx = applyTimeEx;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
