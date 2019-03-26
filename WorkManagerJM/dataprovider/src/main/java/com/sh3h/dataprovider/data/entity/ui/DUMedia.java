package com.sh3h.dataprovider.data.entity.ui;


import android.os.Parcel;
import android.os.Parcelable;

import com.sh3h.dataprovider.util.ConstantUtil;

import java.util.Locale;

public class DUMedia implements Parcelable {
    private Long id;
    private String account;//用户Id
    private long taskId;//任务号
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    private int subType;//1.拆表，2.换表，3.复装，4.停用，5.迁表，6.验表
    private int state;//状态    -1 作废； 1登记；2派单；3接单，4出发，5到场 6回填；10 审核
    private int fileType;//文件类型      0:图片, 1:语音, 2:视频, 3:视频截屏
    private String fileName;//文件名
    private String fileHash;//文件Hash
    private String fileUrl;//文件Url
    private int uploadFlag;//上传标志      -1:无效值, 0:未上传, 1:正在上传, 2:已上传
    private String extend;//扩展信息

    public DUMedia() {
        id = ConstantUtil.INVALID_ID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "account:%s    taskId:%d   fileName:%s", account, taskId, fileName);
    }

    public static final Parcelable.Creator<DUMedia> CREATOR = new Parcelable.Creator<DUMedia>() {
        public DUMedia createFromParcel(Parcel in) {
            DUMedia media = new DUMedia();
            media.setId(in.readLong());
            media.setAccount(in.readString());
            media.setTaskId(in.readLong());
            media.setType(in.readInt());
            media.setSubType(in.readInt());
            media.setState(in.readInt());
            media.setFileType(in.readInt());
            media.setFileName(in.readString());
            media.setFileHash(in.readString());
            media.setFileUrl(in.readString());
            media.setUploadFlag(in.readInt());
            media.setExtend(in.readString());
            return media;
        }

        public DUMedia[] newArray(int size) {
            return new DUMedia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(account);
        parcel.writeLong(taskId);
        parcel.writeInt(type);
        parcel.writeInt(subType);
        parcel.writeInt(state);
        parcel.writeInt(fileType);
        parcel.writeString(fileName);
        parcel.writeString(fileHash);
        parcel.writeString(fileUrl);
        parcel.writeInt(uploadFlag);
        parcel.writeString(extend);
    }
}
