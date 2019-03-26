package com.sh3h.dataprovider.data.entity.ui;

import java.util.ArrayList;

/**
 * Created by limeng on 2016/12/22.
 * 处理
 */
public class DUTaskHandle {
    private long taskId;//任务号
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    private int subType;//1.拆表，2.换表，3.复装，4.停用，5.迁表，6.验表
    private int state;//状态    -1 作废； 1登记；2派单；3接单，4出发，5到场 6回填；10 审核
    private long processTime;//回复时间、施工时间
    private String processReason;//退单原因、发生原因
    private String barCode;// 新表条形码
    private int meterType;//水表类型
    private int meterProducer;//水表厂商
    private int meterReading;//新表底码
    private String caliber;//新表口径
    private int meterRange;//新表量程
    private int oldMeterReading;//旧表读数
    private int resolverResult;//处理结果
    private String address;//地址
    private int urgingPaymentMethod; //催缴方式
    private int stopWater;// 是否停水, number, 0: 否, 1: 是
    private int replaceMeter;// 是否换表, number, 0: 否, 1: 是
    private double longitude;// 经度
    private double latitude; //纬度
    private String meterPosition; //表位
    private String resolveType; //处理类别
    private String resolveContent; //处理内容
    private String resolveDepartment; //处理部门
    private String valveType;//表阀门分类
    private int stopWaterMethod; // '停水方式', number 1.停用；2.拆表
    private int stopWaterType; //停水类型', number 1.报停；2.欠费
    private int resolveMethod; //'处理方式', number 1.现场处理；2.电话处理
    private String remark;//备注
    private String extend;//扩展信息, 可为空
    private ArrayList<DUMaterial> materials;//物料

    public DUTaskHandle() {
    }

    /*接单、到场*/
    public DUTaskHandle(long taskId, int type, int subType, int state, long processTime) {
        this.taskId = taskId;
        this.type = type;
        this.subType = subType;
        this.state = state;
        this.processTime = processTime;
    }

    /*退单、挂起*/
    public DUTaskHandle(long taskId, int type, int subType, int state,
                        long processTime, String processReason) {
        this.taskId = taskId;
        this.type = type;
        this.subType = subType;
        this.state = state;
        this.processTime = processTime;
        this.processReason = processReason;
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

    public void setResolveContent(String resolveContent) {
        this.resolveContent = resolveContent;
    }

    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
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

    public ArrayList<DUMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<DUMaterial> materials) {
        this.materials = materials;
    }
}
