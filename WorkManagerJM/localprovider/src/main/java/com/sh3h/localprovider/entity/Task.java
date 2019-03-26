package com.sh3h.localprovider.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.sh3h.localprovider.dao.DaoSession;
import com.sh3h.localprovider.dao.MultiMediaDao;
import com.sh3h.localprovider.dao.HistoryDao;
import com.sh3h.localprovider.dao.TemporaryDao;
import org.greenrobot.greendao.annotation.NotNull;
import com.sh3h.localprovider.dao.TaskDao;

/**
 * Created by limeng on 2016/12/2.
 * 工单信息
 */
@Entity
public class Task {
    @Id(autoincrement = true)
    private Long id;//主键
    private String account;//用户账号
    private long taskId;//任务编号
    private long hotTaskId;// 热线工单编号
    private int type;//2.表务，3.催缴，5.报装，8.内部，9.热线
    private int subType;//1.拆表，2.换表，3.复装，4.停用，5.迁表，6.验表
    private int detailType;//内部工单子类型 number 1.工地表续期, 2.楼层验收   迁表工单类型：1.有偿迁表, 2.无偿迁表
    private int state;//-1 作废； 1登记；2派单；3接单，4出发，5到场 6回填；10 审核
    private int hangUpState;// 挂起状态
    private int delayState;// 延期状态
    private int isAssist;//是否协助
    private String cardId;//表卡编号
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

    //获取一个任务所有的历史纪录
    @ToMany(joinProperties = {@JoinProperty(name = "taskId", referencedName = "taskId")})
    @OrderBy("replyTime DESC")
    private List<History> histories;//建立关联，一条任务可对应多条历史记录

    //获取一个任务所有的多媒体
    @ToMany(joinProperties = {@JoinProperty(name = "taskId", referencedName = "taskId")})
    private List<MultiMedia> multiMedias;//建立关联，一条任务可对应多条历史记录

    @ToOne(joinProperty = "taskId")
    private Temporary temporaries;//建立关联，一条任务对应一条暂存记录
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    @Generated(hash = 1899384045)
    public Task(Long id, String account, long taskId, long hotTaskId, int type, int subType,
            int detailType, int state, int hangUpState, int delayState, int isAssist,
            String cardId, String cardName, String contacts, String processPerson,
            String acceptName, String assistPerson, String driver, String telephone,
            String address, double longitude, double latitude, String barCode, String station,
            String caliber, int lastReading, String prepaidMoney, String meterPosition,
            String meterDegree, String meterType, String averageWaterQuantity,
            String processReason, String arrearageRange, String arrearageMoney,
            String taskOrigin, long processDate, long dispatchTime, long endDate,
            String responseType, String responseContent, String volume, int volumeIndex,
            String supplyWater, String acceptGroup, String resolveLevel, String responseArea,
            String remark, String extend) {
        this.id = id;
        this.account = account;
        this.taskId = taskId;
        this.hotTaskId = hotTaskId;
        this.type = type;
        this.subType = subType;
        this.detailType = detailType;
        this.state = state;
        this.hangUpState = hangUpState;
        this.delayState = delayState;
        this.isAssist = isAssist;
        this.cardId = cardId;
        this.cardName = cardName;
        this.contacts = contacts;
        this.processPerson = processPerson;
        this.acceptName = acceptName;
        this.assistPerson = assistPerson;
        this.driver = driver;
        this.telephone = telephone;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.barCode = barCode;
        this.station = station;
        this.caliber = caliber;
        this.lastReading = lastReading;
        this.prepaidMoney = prepaidMoney;
        this.meterPosition = meterPosition;
        this.meterDegree = meterDegree;
        this.meterType = meterType;
        this.averageWaterQuantity = averageWaterQuantity;
        this.processReason = processReason;
        this.arrearageRange = arrearageRange;
        this.arrearageMoney = arrearageMoney;
        this.taskOrigin = taskOrigin;
        this.processDate = processDate;
        this.dispatchTime = dispatchTime;
        this.endDate = endDate;
        this.responseType = responseType;
        this.responseContent = responseContent;
        this.volume = volume;
        this.volumeIndex = volumeIndex;
        this.supplyWater = supplyWater;
        this.acceptGroup = acceptGroup;
        this.resolveLevel = resolveLevel;
        this.responseArea = responseArea;
        this.remark = remark;
        this.extend = extend;
    }

    @Generated(hash = 733837707)
    public Task() {
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

    public long getHotTaskId() {
        return this.hotTaskId;
    }

    public void setHotTaskId(long hotTaskId) {
        this.hotTaskId = hotTaskId;
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

    public int getDetailType() {
        return this.detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHangUpState() {
        return this.hangUpState;
    }

    public void setHangUpState(int hangUpState) {
        this.hangUpState = hangUpState;
    }

    public int getDelayState() {
        return this.delayState;
    }

    public void setDelayState(int delayState) {
        this.delayState = delayState;
    }

    public int getIsAssist() {
        return this.isAssist;
    }

    public void setIsAssist(int isAssist) {
        this.isAssist = isAssist;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return this.cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getContacts() {
        return this.contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getProcessPerson() {
        return this.processPerson;
    }

    public void setProcessPerson(String processPerson) {
        this.processPerson = processPerson;
    }

    public String getAcceptName() {
        return this.acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public String getAssistPerson() {
        return this.assistPerson;
    }

    public void setAssistPerson(String assistPerson) {
        this.assistPerson = assistPerson;
    }

    public String getDriver() {
        return this.driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStation() {
        return this.station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCaliber() {
        return this.caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public int getLastReading() {
        return this.lastReading;
    }

    public void setLastReading(int lastReading) {
        this.lastReading = lastReading;
    }

    public String getPrepaidMoney() {
        return this.prepaidMoney;
    }

    public void setPrepaidMoney(String prepaidMoney) {
        this.prepaidMoney = prepaidMoney;
    }

    public String getMeterPosition() {
        return this.meterPosition;
    }

    public void setMeterPosition(String meterPosition) {
        this.meterPosition = meterPosition;
    }

    public String getMeterDegree() {
        return this.meterDegree;
    }

    public void setMeterDegree(String meterDegree) {
        this.meterDegree = meterDegree;
    }

    public String getMeterType() {
        return this.meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getAverageWaterQuantity() {
        return this.averageWaterQuantity;
    }

    public void setAverageWaterQuantity(String averageWaterQuantity) {
        this.averageWaterQuantity = averageWaterQuantity;
    }

    public String getProcessReason() {
        return this.processReason;
    }

    public void setProcessReason(String processReason) {
        this.processReason = processReason;
    }

    public String getArrearageRange() {
        return this.arrearageRange;
    }

    public void setArrearageRange(String arrearageRange) {
        this.arrearageRange = arrearageRange;
    }

    public String getArrearageMoney() {
        return this.arrearageMoney;
    }

    public void setArrearageMoney(String arrearageMoney) {
        this.arrearageMoney = arrearageMoney;
    }

    public String getTaskOrigin() {
        return this.taskOrigin;
    }

    public void setTaskOrigin(String taskOrigin) {
        this.taskOrigin = taskOrigin;
    }

    public long getProcessDate() {
        return this.processDate;
    }

    public void setProcessDate(long processDate) {
        this.processDate = processDate;
    }

    public long getDispatchTime() {
        return this.dispatchTime;
    }

    public void setDispatchTime(long dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public long getEndDate() {
        return this.endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getResponseType() {
        return this.responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseContent() {
        return this.responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getVolume() {
        return this.volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getVolumeIndex() {
        return this.volumeIndex;
    }

    public void setVolumeIndex(int volumeIndex) {
        this.volumeIndex = volumeIndex;
    }

    public String getSupplyWater() {
        return this.supplyWater;
    }

    public void setSupplyWater(String supplyWater) {
        this.supplyWater = supplyWater;
    }

    public String getAcceptGroup() {
        return this.acceptGroup;
    }

    public void setAcceptGroup(String acceptGroup) {
        this.acceptGroup = acceptGroup;
    }

    public String getResolveLevel() {
        return this.resolveLevel;
    }

    public void setResolveLevel(String resolveLevel) {
        this.resolveLevel = resolveLevel;
    }

    public String getResponseArea() {
        return this.responseArea;
    }

    public void setResponseArea(String responseArea) {
        this.responseArea = responseArea;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtend() {
        return this.extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Generated(hash = 960456950)
    private transient Long temporaries__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1584586293)
    public Temporary getTemporaries() {
        long __key = this.taskId;
        if (temporaries__resolvedKey == null || !temporaries__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TemporaryDao targetDao = daoSession.getTemporaryDao();
            Temporary temporariesNew = targetDao.load(__key);
            synchronized (this) {
                temporaries = temporariesNew;
                temporaries__resolvedKey = __key;
            }
        }
        return temporaries;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1277680968)
    public void setTemporaries(@NotNull Temporary temporaries) {
        if (temporaries == null) {
            throw new DaoException(
                    "To-one property 'taskId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.temporaries = temporaries;
            taskId = temporaries.getId();
            temporaries__resolvedKey = taskId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2021455464)
    public List<History> getHistories() {
        if (histories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HistoryDao targetDao = daoSession.getHistoryDao();
            List<History> historiesNew = targetDao._queryTask_Histories(taskId);
            synchronized (this) {
                if (histories == null) {
                    histories = historiesNew;
                }
            }
        }
        return histories;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 36099026)
    public synchronized void resetHistories() {
        histories = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 417790925)
    public List<MultiMedia> getMultiMedias() {
        if (multiMedias == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MultiMediaDao targetDao = daoSession.getMultiMediaDao();
            List<MultiMedia> multiMediasNew = targetDao._queryTask_MultiMedias(taskId);
            synchronized (this) {
                if (multiMedias == null) {
                    multiMedias = multiMediasNew;
                }
            }
        }
        return multiMedias;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1720946766)
    public synchronized void resetMultiMedias() {
        multiMedias = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

}
