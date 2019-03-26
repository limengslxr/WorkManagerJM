package com.sh3h.dataprovider.condition;

/**
 * Created by LiMeng on 2018/1/17.
 */

public class TemporaryCondition {
    public static final int GET_REPORT_TEMPORARY = 1;
    //执行哪种操作
    private int operate;
    private String account;

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
