package com.sh3h.dataprovider.data.entity.ui;

import com.google.gson.annotations.Expose;

/**
 * Created by LiMeng on 2018/2/27.
 */

public class DUTransformCancelHandle {
    @Expose
    private String account;//用户账号
    @Expose
    private String ids;//任务编号  id拼接， 拼接符@
    @Expose
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    @Expose
    private String station;//站点
    @Expose
    private String reason;//原因
    @Expose
    private int operateType;// 1.转站 2.作废
    @Expose
    private String extend;//扩展信息, 可为空

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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
