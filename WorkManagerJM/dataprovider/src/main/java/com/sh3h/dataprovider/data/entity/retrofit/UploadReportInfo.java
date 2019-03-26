package com.sh3h.dataprovider.data.entity.retrofit;


import com.google.gson.annotations.Expose;

public class UploadReportInfo {
    @Expose
    private long localTaskId;//本地任务编号
    @Expose
    private int type;//工单类型
    @Expose
    private String cardId;//表卡编号
    @Expose
    private String cardName;//户名
    @Expose
    private String address;//地址
    @Expose
    private String telephone;//联系电话
    @Expose
    private int meterReading;//最新抄码
    @Expose
    private String barCode;//条形码
    @Expose
    private long reportTime;//上报时间
    @Expose
    private double longitude;
    @Expose
    private double latitude;
    @Expose
    private String remark;//备注
    @Expose
    private String extend;// 扩展信息, 可为空

    public UploadReportInfo() {
    }

    public long getLocalTaskId() {
        return localTaskId;
    }

    public void setLocalTaskId(long localTaskId) {
        this.localTaskId = localTaskId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(int meterReading) {
        this.meterReading = meterReading;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public long getReportTime() {
        return reportTime;
    }

    public void setReportTime(long reportTime) {
        this.reportTime = reportTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
