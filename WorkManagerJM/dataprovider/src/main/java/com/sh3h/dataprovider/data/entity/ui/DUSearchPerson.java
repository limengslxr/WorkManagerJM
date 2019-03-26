package com.sh3h.dataprovider.data.entity.ui;

/**
 * Created by LiMeng on 2018/2/27.
 */

public class DUSearchPerson {
    private String name;
    private String account;
    private boolean isSelected;

    public DUSearchPerson(String name, String account) {
        this.name = name;
        this.account = account;
        isSelected = false;
    }

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
