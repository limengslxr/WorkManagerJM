package com.sh3h.localprovider.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by limeng on 2016/12/2.
 * 词语
 */
@Entity
public class Word {
    @Id(autoincrement = true)
    private Long id;
    private long serverId;//主键 词语id
    private long parentId;//父级编号
    private String group;//词语组
    private String name;//词语文本
    private String value;//词语值
    private String remark;//备注
    @Generated(hash = 629049162)
    public Word(Long id, long serverId, long parentId, String group, String name,
            String value, String remark) {
        this.id = id;
        this.serverId = serverId;
        this.parentId = parentId;
        this.group = group;
        this.name = name;
        this.value = value;
        this.remark = remark;
    }
    @Generated(hash = 3342184)
    public Word() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getServerId() {
        return this.serverId;
    }
    public void setServerId(long serverId) {
        this.serverId = serverId;
    }
    public long getParentId() {
        return this.parentId;
    }
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
    public String getGroup() {
        return this.group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
