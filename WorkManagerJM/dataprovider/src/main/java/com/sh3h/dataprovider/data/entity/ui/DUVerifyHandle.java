package com.sh3h.dataprovider.data.entity.ui;

import com.google.gson.annotations.Expose;

/**
 * Created by LiMeng on 2018/2/28.
 */

public class DUVerifyHandle {
    @Expose
    private String taskIds;//taskId拼接， 拼接符@
    @Expose
    private String verifyPerson;//审批人
    @Expose
    private long verifyTime;//审批时间
    @Expose
    private int verifyResult;//审批结果  1.通过；2.不通过
    @Expose
    private int applyType;//上报类型  -1：默认；0: 延期, 1: 挂起, 2: 恢复,3:协助, 4: 自开单 ...
    @Expose
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    @Expose
    private int subType;//
    @Expose
    private String acceptPerson;//接单人
    @Expose
    private String driver;//司机
    @Expose
    private String assistPerson;//协助人  拼接符@
    @Expose
    private int personCount;//审批人数
    @Expose
    private String difficultIndex;//困难系数
    @Expose
    private long completeTime;//完成时间
    @Expose
    private String remark;//备注
    @Expose
    private String extend;//备注

    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    public String getVerifyPerson() {
        return verifyPerson;
    }

    public void setVerifyPerson(String verifyPerson) {
        this.verifyPerson = verifyPerson;
    }

    public long getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public int getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(int verifyResult) {
        this.verifyResult = verifyResult;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
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

    public String getAcceptPerson() {
        return acceptPerson;
    }

    public void setAcceptPerson(String acceptPerson) {
        this.acceptPerson = acceptPerson;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getAssistPerson() {
        return assistPerson;
    }

    public void setAssistPerson(String assistPerson) {
        this.assistPerson = assistPerson;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public String getDifficultIndex() {
        return difficultIndex;
    }

    public void setDifficultIndex(String difficultIndex) {
        this.difficultIndex = difficultIndex;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
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
