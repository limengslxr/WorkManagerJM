package com.sh3h.dataprovider.data.entity.ui;

import com.google.gson.annotations.Expose;

/**
 * Created by LiMeng on 2018/2/27.
 */

public class DUDispatchHandle {
    @Expose
    private String account;//用户账号
    @Expose
    private String ids;//任务编号 id拼接 拼接符@
    @Expose
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    @Expose
    private String acceptPerson;//接单人
    @Expose
    private String driver;//司机
    @Expose
    private String assistPerson;//协助人 拼接符@
    @Expose
    private String difficultIndex;//困难系数
    @Expose
    private long completeTime;//完成时间 utc
    @Expose
    private String extend;// 扩展信息, 可为空

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
