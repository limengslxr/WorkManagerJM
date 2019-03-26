package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2018/1/4.
 */

public class PersonResult {
    private String name;
    private String account;
    private String roles;
    private String station;
    private String platformRoles;

//    var $sel = $('#user_field_role');
//    var vts = [
//    { text: '抄表员', value: 1 },
//    { text: '催缴员', value: 2 },
//    { text: '表务员', value: 3 },
//    { text: '远传表普查', value: 4 },
//    { text: '协调', value: 5 },
//    //{ text: '收费员', value: 6 },
//    //{ text: '值班长', value: 7 },
//    //{ text: '校表员', value: 8 },
//    //{ text: '维修员', value: 9 },
//    { text: '司机', value: 6 },
//    {text:'热线处理员',value:7},
//    { text: '班长', value: 10 },
//    { text: '经理', value: 11 },
//    //{ text: '工单管理员', value: 99 }

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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getPlatformRoles() {
        return platformRoles;
    }

    public void setPlatformRoles(String platformRoles) {
        this.platformRoles = platformRoles;
    }
}
