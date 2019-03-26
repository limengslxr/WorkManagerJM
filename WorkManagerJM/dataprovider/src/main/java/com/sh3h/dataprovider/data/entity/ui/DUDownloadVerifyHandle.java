package com.sh3h.dataprovider.data.entity.ui;

import com.sh3h.mobileutil.util.TextUtil;

/**
 * Created by LiMeng on 2018/2/9.
 */

public class DUDownloadVerifyHandle {
    private int applyType;//上报类型 number,-1：默认；0: 延期, 1: 挂起, 2: 恢复,3:协助, 4: 自开单 ...
    private String applyPerson; //接单人、上报人、退单人
    private int applyPersonCount; //协助人数
    private long applyTime;// 回复时间、施工时间
    private long delayTime; //延期时间
    private String applyReason; //各种原因
    private String applyRemark; //备注

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public int getApplyPersonCount() {
        return applyPersonCount;
    }

    public void setApplyPersonCount(int applyPersonCount) {
        this.applyPersonCount = applyPersonCount;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getStrApplyTime(){
        return TextUtil.format(applyTime, TextUtil.FORMAT_DATE_NO_SECOND);
    }

    public String getStrDelayTime(){
        return TextUtil.format(delayTime, TextUtil.FORMAT_DATE_NO_SECOND);
    }

}
