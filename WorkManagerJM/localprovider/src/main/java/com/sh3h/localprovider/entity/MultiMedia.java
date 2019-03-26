package com.sh3h.localprovider.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Locale;

/**
 * Created by limeng on 2016/12/2.
 * 多媒体
 */
@Entity
public class MultiMedia {
    @Id(autoincrement = true)
    private Long id;//主键
    private String account;//用户Id
    private long taskId;//任务号
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    private int subType;//1.拆表，2.换表，3.复装，4.停用，5.迁表，6.验表
    private int state;//状态    -1 作废； 1登记；2派单；3接单，4出发，5到场 6回填；10 审核
    private int fileType;//文件类型      0:图片, 1:语音, 2:视频, 3:视频截屏
    @NotNull
    private String fileName;//文件名
    private long replyTime;//处理时间
    private String fileHash;//文件Hash
    private String fileUrl;//文件Url
    private int uploadFlag;//上传标志      -1:无效值, 0:未上传, 1:正在上传, 2:已上传
    //扩展信息
    private String extend;

    @Generated(hash = 1933216507)
    public MultiMedia(Long id, String account, long taskId, int type, int subType,
                      int state, int fileType, @NotNull String fileName, long replyTime,
                      String fileHash, String fileUrl, int uploadFlag, String extend) {
        this.id = id;
        this.account = account;
        this.taskId = taskId;
        this.type = type;
        this.subType = subType;
        this.state = state;
        this.fileType = fileType;
        this.fileName = fileName;
        this.replyTime = replyTime;
        this.fileHash = fileHash;
        this.fileUrl = fileUrl;
        this.uploadFlag = uploadFlag;
        this.extend = extend;
    }

    @Generated(hash = 704612124)
    public MultiMedia() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return this.subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getReplyTime() {
        return this.replyTime;
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
    }

    public String getFileHash() {
        return this.fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getUploadFlag() {
        return this.uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getExtend() {
        return this.extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "account:%s    taskId:%d   fileName:%s", account, taskId, fileName);
    }
}
