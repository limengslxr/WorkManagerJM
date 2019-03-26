package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2018/2/8.
 */

public class StatisticsInfo {
    private String account;
    private long beginTime;
    private long endTime;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
