package com.sh3h.dataprovider.data.entity.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LiMeng on 2017/6/16.
 */

public class DUWord implements Parcelable{
    private long id;
    private long parentId;
    private String group;
    private String name;
    private String value;
    private String remark;

    public DUWord() {
    }

    public DUWord(String name) {
        this.name = name;
        this.value = name;
    }

    public DUWord(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private DUWord(Parcel in) {
        id = in.readLong();
        parentId = in.readLong();
        group = in.readString();
        name = in.readString();
        value = in.readString();
        remark = in.readString();
    }

    public static final Creator<DUWord> CREATOR = new Creator<DUWord>() {
        @Override
        public DUWord createFromParcel(Parcel in) {
            return new DUWord(in);
        }

        @Override
        public DUWord[] newArray(int size) {
            return new DUWord[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(parentId);
        dest.writeString(group);
        dest.writeString(name);
        dest.writeString(value);
        dest.writeString(remark);
    }
}
