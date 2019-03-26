package com.sh3h.localprovider.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.sh3h.localprovider.dao.DaoSession;
import com.sh3h.localprovider.dao.MultiMediaDao;
import com.sh3h.localprovider.dao.TemporaryDao;

/**
 * Created by LiMeng on 2018/1/17.
 */
@Entity
public class Temporary {
    @Id
    private Long id;//任务号
    private String account;//用户号
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
    //获取一个任务的所有多媒体
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "taskId"),
            @JoinProperty(name = "state", referencedName = "state"),
            @JoinProperty(name = "replyTime", referencedName = "replyTime")})
    private List<MultiMedia> multiMedias;//建立关联，一条任务可对应多条历史记录
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 871667726)
    private transient TemporaryDao myDao;
    @Generated(hash = 1773234137)
    public Temporary(Long id, String account, int type, int subType, int state,
            int hangUpState, int delayState, int reportType, int assist,
            String content, String reply, long replyTime, int uploadFlag,
            String extend) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.subType = subType;
        this.state = state;
        this.hangUpState = hangUpState;
        this.delayState = delayState;
        this.reportType = reportType;
        this.assist = assist;
        this.content = content;
        this.reply = reply;
        this.replyTime = replyTime;
        this.uploadFlag = uploadFlag;
        this.extend = extend;
    }
    @Generated(hash = 1069031444)
    public Temporary() {
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
    public int getReportType() {
        return this.reportType;
    }
    public void setReportType(int reportType) {
        this.reportType = reportType;
    }
    public int getAssist() {
        return this.assist;
    }
    public void setAssist(int assist) {
        this.assist = assist;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getReply() {
        return this.reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }
    public long getReplyTime() {
        return this.replyTime;
    }
    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
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
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1697588516)
    public List<MultiMedia> getMultiMedias() {
        if (multiMedias == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MultiMediaDao targetDao = daoSession.getMultiMediaDao();
            List<MultiMedia> multiMediasNew = targetDao
                    ._queryTemporary_MultiMedias(id, state, replyTime);
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
    @Generated(hash = 1039005661)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTemporaryDao() : null;
    }

}
