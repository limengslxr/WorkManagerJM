package com.sh3h.dataprovider.data.entity.retrofit;


public class DownLoadWorkInfoEntity {
    private String account;
    private int type;
    private int since;
    private int count;

    public DownLoadWorkInfoEntity() {
    }

    public DownLoadWorkInfoEntity(String account, int type, int since, int count) {
        this.account = account;
        this.type = type;
        this.since = since;
        this.count = count;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSince() {
        return since;
    }

    public void setSince(int since) {
        this.since = since;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
