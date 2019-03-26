package com.sh3h.localprovider.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by LiMeng on 2018/2/6.
 */
@Entity
public class Person {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String account;
    private String roles;
    private String station;
    private String platformRoles;
    private String extend;
    @Generated(hash = 420184388)
    public Person(Long id, String name, String account, String roles,
            String station, String platformRoles, String extend) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.roles = roles;
        this.station = station;
        this.platformRoles = platformRoles;
        this.extend = extend;
    }
    @Generated(hash = 1024547259)
    public Person() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getRoles() {
        return this.roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    public String getStation() {
        return this.station;
    }
    public void setStation(String station) {
        this.station = station;
    }
    public String getPlatformRoles() {
        return this.platformRoles;
    }
    public void setPlatformRoles(String platformRoles) {
        this.platformRoles = platformRoles;
    }
    public String getExtend() {
        return this.extend;
    }
    public void setExtend(String extend) {
        this.extend = extend;
    }

}
