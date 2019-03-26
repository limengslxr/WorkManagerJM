package com.sh3h.dataprovider.data.entity.retrofit;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;


/**
 * 任务处理信息
 */
public class UploadTaskInfo implements Serializable {
    @Expose
    private long taskId;//任务编号
    @Expose
    private int type;//类型
    @Expose
    private int subType;//子类型
    @Expose
    private int state;//状态
    @Expose
    private long processTime;//回复时间/施工时间
    @Expose
    private String processReason;//退单原因、发生原因
    @Expose
    private String barCode;//新表条形码
    @Expose
    private  int meterType;//水表类型
    @Expose
    private int meterProducer;//水表厂商
    @Expose
    private int meterReading;//新表底码
    @Expose
    private String caliber;//新表口径
    @Expose
    private int meterRange;//新表量程
    @Expose
    private int oldMeterReading; //旧表读数
    @Expose
    private int resolverResult;//处理结果
    @Expose
    private String address;//地址
    @Expose
    private int urgingPaymentMethod; //催缴方式
    @Expose
    private int stopWater;// 是否停水  0: 否, 1: 是
    @Expose
    private int replaceMeter;//是否换表, number, 0: 否, 1: 是
    @Expose
    private double longitude;// 经度
    @Expose
    private double latitude; //纬度
    @Expose
    private String meterPosition; //表位
    @Expose
    private String resolveType; //处理类别
    @Expose
    private String resolveContent; //处理内容
    @Expose
    private String resolveDepartment;//处理部门
    @Expose
    private String valveType;//表阀门分类
    @Expose
    private int stopWaterMethod;//停水方式  1.停用；2.拆表
    @Expose
    private int stopWaterType;//停水类型  1.报停；2.欠费
    @Expose
    private int resolveMethod; //处理方式  1.现场处理；2.电话处理
    @Expose
    private String remark;//备注
    @Expose
    private String extend;// 'json string' // 扩展信息, 可为空
    @Expose
    private List<MaterialInfo> materials;//物料

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

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public String getProcessReason() {
        return processReason;
    }

    public void setProcessReason(String processReason) {
        this.processReason = processReason;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getMeterType() {
        return meterType;
    }

    public void setMeterType(int meterType) {
        this.meterType = meterType;
    }

    public int getMeterProducer() {
        return meterProducer;
    }

    public void setMeterProducer(int meterProducer) {
        this.meterProducer = meterProducer;
    }

    public int getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(int meterReading) {
        this.meterReading = meterReading;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public int getMeterRange() {
        return meterRange;
    }

    public void setMeterRange(int meterRange) {
        this.meterRange = meterRange;
    }

    public int getOldMeterReading() {
        return oldMeterReading;
    }

    public void setOldMeterReading(int oldMeterReading) {
        this.oldMeterReading = oldMeterReading;
    }

    public int getResolverResult() {
        return resolverResult;
    }

    public void setResolverResult(int resolverResult) {
        this.resolverResult = resolverResult;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUrgingPaymentMethod() {
        return urgingPaymentMethod;
    }

    public void setUrgingPaymentMethod(int urgingPaymentMethod) {
        this.urgingPaymentMethod = urgingPaymentMethod;
    }

    public int getStopWater() {
        return stopWater;
    }

    public void setStopWater(int stopWater) {
        this.stopWater = stopWater;
    }

    public int getReplaceMeter() {
        return replaceMeter;
    }

    public void setReplaceMeter(int replaceMeter) {
        this.replaceMeter = replaceMeter;
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

    public String getMeterPosition() {
        return meterPosition;
    }

    public void setMeterPosition(String meterPosition) {
        this.meterPosition = meterPosition;
    }

    public String getResolveContent() {
        return resolveContent;
    }

    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
    }

    public void setResolveContent(String resolveContent) {
        this.resolveContent = resolveContent;
    }

    public String getResolveDepartment() {
        return resolveDepartment;
    }

    public void setResolveDepartment(String resolveDepartment) {
        this.resolveDepartment = resolveDepartment;
    }

    public String getValveType() {
        return valveType;
    }

    public void setValveType(String valveType) {
        this.valveType = valveType;
    }

    public int getStopWaterMethod() {
        return stopWaterMethod;
    }

    public void setStopWaterMethod(int stopWaterMethod) {
        this.stopWaterMethod = stopWaterMethod;
    }

    public int getStopWaterType() {
        return stopWaterType;
    }

    public void setStopWaterType(int stopWaterType) {
        this.stopWaterType = stopWaterType;
    }

    public int getResolveMethod() {
        return resolveMethod;
    }

    public void setResolveMethod(int resolveMethod) {
        this.resolveMethod = resolveMethod;
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

    public List<MaterialInfo> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialInfo> materials) {
        this.materials = materials;
    }
}
