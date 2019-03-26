package com.sh3h.dataprovider.data.entity.retrofit;

import java.util.List;

/**
 * Created by LiMeng on 2018/2/7.
 */

public class StatisticsResult {
    private String name;//成员姓名
    private String account;//账号
    private List<Statistics> info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<Statistics> getInfo() {
        return info;
    }

    public void setInfo(List<Statistics> info) {
        this.info = info;
    }
}
