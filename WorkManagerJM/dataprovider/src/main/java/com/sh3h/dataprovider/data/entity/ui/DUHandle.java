package com.sh3h.dataprovider.data.entity.ui;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.util.ArrayList;

/**
 * Created by limeng on 2016/12/21.
 * 处理的内容
 */

public class DUHandle implements Parcelable {
    private static Gson gson = new Gson();
    private Long id;
    private String account;//用户号
    private long taskId;//任务号
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    private int subType;//1.拆表，2.换表，3.复装，4.停用，5.迁表，6.验表
    private int state;//状态    -1 作废； 1登记；2派单；3接单，4出发，5到场 6回填；10 审核
    private int hangUpState;
    private int delayState;
    private int reportType;
    private int assist; //是否已经申请协助
    private String content;//工单内容      Json字符串表示任务信息
    private String reply; //工单处理      Json字符串表示任务信息
    private long replyTime;//处理时间
    private int uploadFlag;//上传标志
    private String extend;//扩展信息      0:未上传, 1:正在上传, 2:已上传
    private ArrayList<DUMedia> medias;

    public DUHandle() {
    }

    public DUHandle(Parcel in) {
        id = in.readLong();
        account = in.readString();
        taskId = in.readLong();
        type = in.readInt();
        subType = in.readInt();
        state = in.readInt();
        hangUpState = in.readInt();
        delayState = in.readInt();
        reportType = in.readInt();
        assist = in.readInt();
        content = in.readString();
        reply = in.readString();
        replyTime = in.readLong();
        uploadFlag = in.readInt();
        extend = in.readString();
        medias = new ArrayList<>();
        in.readTypedList(medias, DUMedia.CREATOR);
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

    public int getHangUpState() {
        return hangUpState;
    }

    public void setHangUpState(int hangUpState) {
        this.hangUpState = hangUpState;
    }

    public int getDelayState() {
        return delayState;
    }

    public void setDelayState(int delayState) {
        this.delayState = delayState;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public int getAssist() {
        return assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setReply(Object reply) {
        this.reply = gson.toJson(reply);
    }

    public long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
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

    public ArrayList<DUMedia> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<DUMedia> medias) {
        this.medias = medias;
    }

    @Override
    public String toString() {
        return id + ":" + taskId + ":" + state + ":" + reportType + ":" +
                content + ":" + reply + ":" + replyTime + ":" + uploadFlag;
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }

    public String getFormatTime() {
        return TextUtil.format(replyTime, TextUtil.FORMAT_FULL_DATETIME);
    }

    public static DUHandle initDUHandle(DUTask task) {
        DUHandle handle = new DUHandle();
        handle.setAccount(task.getAccount());
        handle.setTaskId(task.getTaskId());
        handle.setType(task.getType());
        handle.setSubType(task.getSubType());
        handle.setState(task.getState());
        handle.setDelayState(task.getDelayState());
        handle.setHangUpState(task.getHangUpState());
        handle.setContent(gson.toJson(task));
        handle.setUploadFlag(ConstantUtil.UploadFlag.NOT_UPLOAD);
        handle.setReplyTime(System.currentTimeMillis());
        return handle;
    }

    /*applyType*/
    public DUApplyHandle toDUApplyHandle() {
        if (TextUtil.isNullOrEmpty(reply)) {
            reportType = ConstantUtil.ReportType.Apply;
            DUApplyHandle handle = new DUApplyHandle();
            handle.setTaskId(taskId);
            handle.setType(type);
            handle.setSubType(subType);
            handle.setState(state);
            handle.setApplyTime(replyTime);
            return handle;
        }
        return gson.fromJson(reply, DUApplyHandle.class);
    }

    public DUAssistHandle toDUAssistHandle() {
        if (TextUtil.isNullOrEmpty(reply)) {
            reportType = ConstantUtil.ReportType.Assist;
            DUAssistHandle handle = new DUAssistHandle();
            handle.setAssistTime(replyTime);
            handle.setTaskId(taskId);
            handle.setType(type);
            handle.setSubType(subType);
            handle.setState(state);
            return handle;
        }
        return gson.fromJson(reply, DUAssistHandle.class);
    }

    public DUReportHandle toDUReportHandle() {
        if (TextUtil.isNullOrEmpty(reply)) {
            reportType = ConstantUtil.ReportType.Report;
            DUReportHandle handle = new DUReportHandle();
            handle.setReportTime(replyTime);
            handle.setLocalTaskId(taskId);
            handle.setType(type);
            return handle;
        }
        return gson.fromJson(reply, DUReportHandle.class);
    }

    public DUTaskHandle toDUTaskHandle() {
        if (TextUtil.isNullOrEmpty(reply)) {
            reportType = ConstantUtil.ReportType.Handle;
            DUTaskHandle handle = new DUTaskHandle();
            handle.setProcessTime(replyTime);
            handle.setTaskId(taskId);
            handle.setState(state);
            handle.setSubType(subType);
            handle.setType(type);
            return handle;
        }
        return gson.fromJson(reply, DUTaskHandle.class);
    }

    public DUDownloadVerifyHandle toDUDownloadVerifyHandle() {
        if (TextUtil.isNullOrEmpty(reply)) {
            reportType = ConstantUtil.ReportType.VERIFY;
            DUDownloadVerifyHandle handle = new DUDownloadVerifyHandle();
            handle.setApplyType(ConstantUtil.ApplyType.DEFAULT);
            handle.setApplyPerson(TextUtil.EMPTY);
            handle.setApplyPersonCount(0);
            handle.setApplyTime(System.currentTimeMillis());
            handle.setDelayTime(System.currentTimeMillis());
            handle.setApplyReason(TextUtil.EMPTY);
            handle.setApplyRemark(TextUtil.EMPTY);
            return handle;
        }
        return gson.fromJson(reply, DUDownloadVerifyHandle.class);
    }

    public static final Parcelable.Creator<DUHandle> CREATOR = new Parcelable.Creator<DUHandle>() {
        @Override
        public DUHandle createFromParcel(Parcel in) {
            return new DUHandle(in);
        }

        public DUHandle[] newArray(int size) {
            return new DUHandle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            id = ConstantUtil.INVALID_ID;
        }
        parcel.writeLong(id);
        parcel.writeString(account);
        parcel.writeLong(taskId);
        parcel.writeInt(type);
        parcel.writeInt(subType);
        parcel.writeInt(state);
        parcel.writeInt(hangUpState);
        parcel.writeInt(delayState);
        parcel.writeInt(reportType);
        parcel.writeInt(assist);
        parcel.writeString(content);
        parcel.writeString(reply);
        parcel.writeLong(replyTime);
        parcel.writeInt(uploadFlag);
        parcel.writeString(extend);
        parcel.writeTypedList(medias);
    }
}
