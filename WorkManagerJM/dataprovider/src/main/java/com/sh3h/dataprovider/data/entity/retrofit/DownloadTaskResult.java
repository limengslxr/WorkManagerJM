package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * 工单下载结果
 */
public class DownloadTaskResult {
    private long taskId;//任务编号
    private long hotTaskId;// 热线工单编号
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    private int subType;//1.拆表，2.换表，3.复装，4.停用，5.迁表，6.验表
    private int detailType;//内部工单子类型 number 1.工地表续期, 2.楼层验收   迁表工单类型：1.有偿迁表, 2.无偿迁表
    private int state;//-1 作废； 1登记；2派单；3接单，4出发，5到场 6回填；10 审核
    private int hangUpState;// 挂起状态
    private int delayState;// 延期状态
    private int isAssist;//是否协助
    private String cardId;//表卡编号、用户编号
    private String cardName;//户名
    private String contacts;//联系人
    private String processPerson;//申请人、反映人
    private String acceptName;//接单人、主办人
    private String assistPerson;//协助人
    private String driver;//司机
    private String telephone;//电话、手机
    private String address;//地址
    private double longitude;//经度
    private double latitude;//维度
    private String barCode;//条形码
    private String station;//站点
    private String caliber;//口径
    private int lastReading;//上次抄码
    private String prepaidMoney;//预交款
    private String meterPosition;//表位
    private String meterDegree;//原表行度
    private String meterType;//表类型
    private String averageWaterQuantity;//平均水量
    private String processReason;//各种原因
    private String arrearageRange;//欠费跨度
    private String arrearageMoney;//欠费金额
    private String taskOrigin;//工单来源
    private long processDate;//发生时间、申请时间
    private long dispatchTime;//派单时间
    private long endDate;//处理时限
    private String responseType;//反映类型
    private String responseContent;//反映内容
    private String volume;//册本号
    private int volumeIndex;//册内序号
    private String supplyWater;//供水方式
    private String acceptGroup;//受理班组
    private String resolveLevel;//处理级别
    private String responseArea;//反应区块
    private String remark;//备注、受理备注
    private String extend;//扩展信息

    public DownloadTaskResult() {
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getHotTaskId() {
        return hotTaskId;
    }

    public void setHotTaskId(long hotTaskId) {
        this.hotTaskId = hotTaskId;
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

    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
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

    public int getIsAssist() {
        return isAssist;
    }

    public void setIsAssist(int isAssist) {
        this.isAssist = isAssist;
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

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getProcessPerson() {
        return processPerson;
    }

    public void setProcessPerson(String processPerson) {
        this.processPerson = processPerson;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public String getAssistPerson() {
        return assistPerson;
    }

    public void setAssistPerson(String assistPerson) {
        this.assistPerson = assistPerson;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public int getLastReading() {
        return lastReading;
    }

    public void setLastReading(int lastReading) {
        this.lastReading = lastReading;
    }

    public String getPrepaidMoney() {
        return prepaidMoney;
    }

    public void setPrepaidMoney(String prepaidMoney) {
        this.prepaidMoney = prepaidMoney;
    }

    public String getMeterPosition() {
        return meterPosition;
    }

    public void setMeterPosition(String meterPosition) {
        this.meterPosition = meterPosition;
    }

    public String getMeterDegree() {
        return meterDegree;
    }

    public void setMeterDegree(String meterDegree) {
        this.meterDegree = meterDegree;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getAverageWaterQuantity() {
        return averageWaterQuantity;
    }

    public void setAverageWaterQuantity(String averageWaterQuantity) {
        this.averageWaterQuantity = averageWaterQuantity;
    }

    public String getProcessReason() {
        return processReason;
    }

    public void setProcessReason(String processReason) {
        this.processReason = processReason;
    }

    public String getArrearageRange() {
        return arrearageRange;
    }

    public void setArrearageRange(String arrearageRange) {
        this.arrearageRange = arrearageRange;
    }

    public String getArrearageMoney() {
        return arrearageMoney;
    }

    public void setArrearageMoney(String arrearageMoney) {
        this.arrearageMoney = arrearageMoney;
    }

    public String getTaskOrigin() {
        return taskOrigin;
    }

    public void setTaskOrigin(String taskOrigin) {
        this.taskOrigin = taskOrigin;
    }

    public long getProcessDate() {
        return processDate;
    }

    public void setProcessDate(long processDate) {
        this.processDate = processDate;
    }

    public long getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(long dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getVolumeIndex() {
        return volumeIndex;
    }

    public void setVolumeIndex(int volumeIndex) {
        this.volumeIndex = volumeIndex;
    }

    public String getSupplyWater() {
        return supplyWater;
    }

    public void setSupplyWater(String supplyWater) {
        this.supplyWater = supplyWater;
    }

    public String getAcceptGroup() {
        return acceptGroup;
    }

    public void setAcceptGroup(String acceptGroup) {
        this.acceptGroup = acceptGroup;
    }

    public String getResolveLevel() {
        return resolveLevel;
    }

    public void setResolveLevel(String resolveLevel) {
        this.resolveLevel = resolveLevel;
    }

    public String getResponseArea() {
        return responseArea;
    }

    public void setResponseArea(String responseArea) {
        this.responseArea = responseArea;
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
