package com.sh3h.localprovider.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.localprovider.entity.Temporary;

import com.sh3h.localprovider.entity.Task;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TASK".
*/
public class TaskDao extends AbstractDao<Task, Long> {

    public static final String TABLENAME = "TASK";

    /**
     * Properties of entity Task.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Account = new Property(1, String.class, "account", false, "ACCOUNT");
        public final static Property TaskId = new Property(2, long.class, "taskId", false, "TASK_ID");
        public final static Property HotTaskId = new Property(3, long.class, "hotTaskId", false, "HOT_TASK_ID");
        public final static Property Type = new Property(4, int.class, "type", false, "TYPE");
        public final static Property SubType = new Property(5, int.class, "subType", false, "SUB_TYPE");
        public final static Property DetailType = new Property(6, int.class, "detailType", false, "DETAIL_TYPE");
        public final static Property State = new Property(7, int.class, "state", false, "STATE");
        public final static Property HangUpState = new Property(8, int.class, "hangUpState", false, "HANG_UP_STATE");
        public final static Property DelayState = new Property(9, int.class, "delayState", false, "DELAY_STATE");
        public final static Property IsAssist = new Property(10, int.class, "isAssist", false, "IS_ASSIST");
        public final static Property CardId = new Property(11, String.class, "cardId", false, "CARD_ID");
        public final static Property CardName = new Property(12, String.class, "cardName", false, "CARD_NAME");
        public final static Property Contacts = new Property(13, String.class, "contacts", false, "CONTACTS");
        public final static Property ProcessPerson = new Property(14, String.class, "processPerson", false, "PROCESS_PERSON");
        public final static Property AcceptName = new Property(15, String.class, "acceptName", false, "ACCEPT_NAME");
        public final static Property AssistPerson = new Property(16, String.class, "assistPerson", false, "ASSIST_PERSON");
        public final static Property Driver = new Property(17, String.class, "driver", false, "DRIVER");
        public final static Property Telephone = new Property(18, String.class, "telephone", false, "TELEPHONE");
        public final static Property Address = new Property(19, String.class, "address", false, "ADDRESS");
        public final static Property Longitude = new Property(20, double.class, "longitude", false, "LONGITUDE");
        public final static Property Latitude = new Property(21, double.class, "latitude", false, "LATITUDE");
        public final static Property BarCode = new Property(22, String.class, "barCode", false, "BAR_CODE");
        public final static Property Station = new Property(23, String.class, "station", false, "STATION");
        public final static Property Caliber = new Property(24, String.class, "caliber", false, "CALIBER");
        public final static Property LastReading = new Property(25, int.class, "lastReading", false, "LAST_READING");
        public final static Property PrepaidMoney = new Property(26, String.class, "prepaidMoney", false, "PREPAID_MONEY");
        public final static Property MeterPosition = new Property(27, String.class, "meterPosition", false, "METER_POSITION");
        public final static Property MeterDegree = new Property(28, String.class, "meterDegree", false, "METER_DEGREE");
        public final static Property MeterType = new Property(29, String.class, "meterType", false, "METER_TYPE");
        public final static Property AverageWaterQuantity = new Property(30, String.class, "averageWaterQuantity", false, "AVERAGE_WATER_QUANTITY");
        public final static Property ProcessReason = new Property(31, String.class, "processReason", false, "PROCESS_REASON");
        public final static Property ArrearageRange = new Property(32, String.class, "arrearageRange", false, "ARREARAGE_RANGE");
        public final static Property ArrearageMoney = new Property(33, String.class, "arrearageMoney", false, "ARREARAGE_MONEY");
        public final static Property TaskOrigin = new Property(34, String.class, "taskOrigin", false, "TASK_ORIGIN");
        public final static Property ProcessDate = new Property(35, long.class, "processDate", false, "PROCESS_DATE");
        public final static Property DispatchTime = new Property(36, long.class, "dispatchTime", false, "DISPATCH_TIME");
        public final static Property EndDate = new Property(37, long.class, "endDate", false, "END_DATE");
        public final static Property ResponseType = new Property(38, String.class, "responseType", false, "RESPONSE_TYPE");
        public final static Property ResponseContent = new Property(39, String.class, "responseContent", false, "RESPONSE_CONTENT");
        public final static Property Volume = new Property(40, String.class, "volume", false, "VOLUME");
        public final static Property VolumeIndex = new Property(41, int.class, "volumeIndex", false, "VOLUME_INDEX");
        public final static Property SupplyWater = new Property(42, String.class, "supplyWater", false, "SUPPLY_WATER");
        public final static Property AcceptGroup = new Property(43, String.class, "acceptGroup", false, "ACCEPT_GROUP");
        public final static Property ResolveLevel = new Property(44, String.class, "resolveLevel", false, "RESOLVE_LEVEL");
        public final static Property ResponseArea = new Property(45, String.class, "responseArea", false, "RESPONSE_AREA");
        public final static Property Remark = new Property(46, String.class, "remark", false, "REMARK");
        public final static Property Extend = new Property(47, String.class, "extend", false, "EXTEND");
    }

    private DaoSession daoSession;


    public TaskDao(DaoConfig config) {
        super(config);
    }
    
    public TaskDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TASK\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ACCOUNT\" TEXT," + // 1: account
                "\"TASK_ID\" INTEGER NOT NULL ," + // 2: taskId
                "\"HOT_TASK_ID\" INTEGER NOT NULL ," + // 3: hotTaskId
                "\"TYPE\" INTEGER NOT NULL ," + // 4: type
                "\"SUB_TYPE\" INTEGER NOT NULL ," + // 5: subType
                "\"DETAIL_TYPE\" INTEGER NOT NULL ," + // 6: detailType
                "\"STATE\" INTEGER NOT NULL ," + // 7: state
                "\"HANG_UP_STATE\" INTEGER NOT NULL ," + // 8: hangUpState
                "\"DELAY_STATE\" INTEGER NOT NULL ," + // 9: delayState
                "\"IS_ASSIST\" INTEGER NOT NULL ," + // 10: isAssist
                "\"CARD_ID\" TEXT," + // 11: cardId
                "\"CARD_NAME\" TEXT," + // 12: cardName
                "\"CONTACTS\" TEXT," + // 13: contacts
                "\"PROCESS_PERSON\" TEXT," + // 14: processPerson
                "\"ACCEPT_NAME\" TEXT," + // 15: acceptName
                "\"ASSIST_PERSON\" TEXT," + // 16: assistPerson
                "\"DRIVER\" TEXT," + // 17: driver
                "\"TELEPHONE\" TEXT," + // 18: telephone
                "\"ADDRESS\" TEXT," + // 19: address
                "\"LONGITUDE\" REAL NOT NULL ," + // 20: longitude
                "\"LATITUDE\" REAL NOT NULL ," + // 21: latitude
                "\"BAR_CODE\" TEXT," + // 22: barCode
                "\"STATION\" TEXT," + // 23: station
                "\"CALIBER\" TEXT," + // 24: caliber
                "\"LAST_READING\" INTEGER NOT NULL ," + // 25: lastReading
                "\"PREPAID_MONEY\" TEXT," + // 26: prepaidMoney
                "\"METER_POSITION\" TEXT," + // 27: meterPosition
                "\"METER_DEGREE\" TEXT," + // 28: meterDegree
                "\"METER_TYPE\" TEXT," + // 29: meterType
                "\"AVERAGE_WATER_QUANTITY\" TEXT," + // 30: averageWaterQuantity
                "\"PROCESS_REASON\" TEXT," + // 31: processReason
                "\"ARREARAGE_RANGE\" TEXT," + // 32: arrearageRange
                "\"ARREARAGE_MONEY\" TEXT," + // 33: arrearageMoney
                "\"TASK_ORIGIN\" TEXT," + // 34: taskOrigin
                "\"PROCESS_DATE\" INTEGER NOT NULL ," + // 35: processDate
                "\"DISPATCH_TIME\" INTEGER NOT NULL ," + // 36: dispatchTime
                "\"END_DATE\" INTEGER NOT NULL ," + // 37: endDate
                "\"RESPONSE_TYPE\" TEXT," + // 38: responseType
                "\"RESPONSE_CONTENT\" TEXT," + // 39: responseContent
                "\"VOLUME\" TEXT," + // 40: volume
                "\"VOLUME_INDEX\" INTEGER NOT NULL ," + // 41: volumeIndex
                "\"SUPPLY_WATER\" TEXT," + // 42: supplyWater
                "\"ACCEPT_GROUP\" TEXT," + // 43: acceptGroup
                "\"RESOLVE_LEVEL\" TEXT," + // 44: resolveLevel
                "\"RESPONSE_AREA\" TEXT," + // 45: responseArea
                "\"REMARK\" TEXT," + // 46: remark
                "\"EXTEND\" TEXT);"); // 47: extend
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TASK\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Task entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
        stmt.bindLong(3, entity.getTaskId());
        stmt.bindLong(4, entity.getHotTaskId());
        stmt.bindLong(5, entity.getType());
        stmt.bindLong(6, entity.getSubType());
        stmt.bindLong(7, entity.getDetailType());
        stmt.bindLong(8, entity.getState());
        stmt.bindLong(9, entity.getHangUpState());
        stmt.bindLong(10, entity.getDelayState());
        stmt.bindLong(11, entity.getIsAssist());
 
        String cardId = entity.getCardId();
        if (cardId != null) {
            stmt.bindString(12, cardId);
        }
 
        String cardName = entity.getCardName();
        if (cardName != null) {
            stmt.bindString(13, cardName);
        }
 
        String contacts = entity.getContacts();
        if (contacts != null) {
            stmt.bindString(14, contacts);
        }
 
        String processPerson = entity.getProcessPerson();
        if (processPerson != null) {
            stmt.bindString(15, processPerson);
        }
 
        String acceptName = entity.getAcceptName();
        if (acceptName != null) {
            stmt.bindString(16, acceptName);
        }
 
        String assistPerson = entity.getAssistPerson();
        if (assistPerson != null) {
            stmt.bindString(17, assistPerson);
        }
 
        String driver = entity.getDriver();
        if (driver != null) {
            stmt.bindString(18, driver);
        }
 
        String telephone = entity.getTelephone();
        if (telephone != null) {
            stmt.bindString(19, telephone);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(20, address);
        }
        stmt.bindDouble(21, entity.getLongitude());
        stmt.bindDouble(22, entity.getLatitude());
 
        String barCode = entity.getBarCode();
        if (barCode != null) {
            stmt.bindString(23, barCode);
        }
 
        String station = entity.getStation();
        if (station != null) {
            stmt.bindString(24, station);
        }
 
        String caliber = entity.getCaliber();
        if (caliber != null) {
            stmt.bindString(25, caliber);
        }
        stmt.bindLong(26, entity.getLastReading());
 
        String prepaidMoney = entity.getPrepaidMoney();
        if (prepaidMoney != null) {
            stmt.bindString(27, prepaidMoney);
        }
 
        String meterPosition = entity.getMeterPosition();
        if (meterPosition != null) {
            stmt.bindString(28, meterPosition);
        }
 
        String meterDegree = entity.getMeterDegree();
        if (meterDegree != null) {
            stmt.bindString(29, meterDegree);
        }
 
        String meterType = entity.getMeterType();
        if (meterType != null) {
            stmt.bindString(30, meterType);
        }
 
        String averageWaterQuantity = entity.getAverageWaterQuantity();
        if (averageWaterQuantity != null) {
            stmt.bindString(31, averageWaterQuantity);
        }
 
        String processReason = entity.getProcessReason();
        if (processReason != null) {
            stmt.bindString(32, processReason);
        }
 
        String arrearageRange = entity.getArrearageRange();
        if (arrearageRange != null) {
            stmt.bindString(33, arrearageRange);
        }
 
        String arrearageMoney = entity.getArrearageMoney();
        if (arrearageMoney != null) {
            stmt.bindString(34, arrearageMoney);
        }
 
        String taskOrigin = entity.getTaskOrigin();
        if (taskOrigin != null) {
            stmt.bindString(35, taskOrigin);
        }
        stmt.bindLong(36, entity.getProcessDate());
        stmt.bindLong(37, entity.getDispatchTime());
        stmt.bindLong(38, entity.getEndDate());
 
        String responseType = entity.getResponseType();
        if (responseType != null) {
            stmt.bindString(39, responseType);
        }
 
        String responseContent = entity.getResponseContent();
        if (responseContent != null) {
            stmt.bindString(40, responseContent);
        }
 
        String volume = entity.getVolume();
        if (volume != null) {
            stmt.bindString(41, volume);
        }
        stmt.bindLong(42, entity.getVolumeIndex());
 
        String supplyWater = entity.getSupplyWater();
        if (supplyWater != null) {
            stmt.bindString(43, supplyWater);
        }
 
        String acceptGroup = entity.getAcceptGroup();
        if (acceptGroup != null) {
            stmt.bindString(44, acceptGroup);
        }
 
        String resolveLevel = entity.getResolveLevel();
        if (resolveLevel != null) {
            stmt.bindString(45, resolveLevel);
        }
 
        String responseArea = entity.getResponseArea();
        if (responseArea != null) {
            stmt.bindString(46, responseArea);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(47, remark);
        }
 
        String extend = entity.getExtend();
        if (extend != null) {
            stmt.bindString(48, extend);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Task entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
        stmt.bindLong(3, entity.getTaskId());
        stmt.bindLong(4, entity.getHotTaskId());
        stmt.bindLong(5, entity.getType());
        stmt.bindLong(6, entity.getSubType());
        stmt.bindLong(7, entity.getDetailType());
        stmt.bindLong(8, entity.getState());
        stmt.bindLong(9, entity.getHangUpState());
        stmt.bindLong(10, entity.getDelayState());
        stmt.bindLong(11, entity.getIsAssist());
 
        String cardId = entity.getCardId();
        if (cardId != null) {
            stmt.bindString(12, cardId);
        }
 
        String cardName = entity.getCardName();
        if (cardName != null) {
            stmt.bindString(13, cardName);
        }
 
        String contacts = entity.getContacts();
        if (contacts != null) {
            stmt.bindString(14, contacts);
        }
 
        String processPerson = entity.getProcessPerson();
        if (processPerson != null) {
            stmt.bindString(15, processPerson);
        }
 
        String acceptName = entity.getAcceptName();
        if (acceptName != null) {
            stmt.bindString(16, acceptName);
        }
 
        String assistPerson = entity.getAssistPerson();
        if (assistPerson != null) {
            stmt.bindString(17, assistPerson);
        }
 
        String driver = entity.getDriver();
        if (driver != null) {
            stmt.bindString(18, driver);
        }
 
        String telephone = entity.getTelephone();
        if (telephone != null) {
            stmt.bindString(19, telephone);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(20, address);
        }
        stmt.bindDouble(21, entity.getLongitude());
        stmt.bindDouble(22, entity.getLatitude());
 
        String barCode = entity.getBarCode();
        if (barCode != null) {
            stmt.bindString(23, barCode);
        }
 
        String station = entity.getStation();
        if (station != null) {
            stmt.bindString(24, station);
        }
 
        String caliber = entity.getCaliber();
        if (caliber != null) {
            stmt.bindString(25, caliber);
        }
        stmt.bindLong(26, entity.getLastReading());
 
        String prepaidMoney = entity.getPrepaidMoney();
        if (prepaidMoney != null) {
            stmt.bindString(27, prepaidMoney);
        }
 
        String meterPosition = entity.getMeterPosition();
        if (meterPosition != null) {
            stmt.bindString(28, meterPosition);
        }
 
        String meterDegree = entity.getMeterDegree();
        if (meterDegree != null) {
            stmt.bindString(29, meterDegree);
        }
 
        String meterType = entity.getMeterType();
        if (meterType != null) {
            stmt.bindString(30, meterType);
        }
 
        String averageWaterQuantity = entity.getAverageWaterQuantity();
        if (averageWaterQuantity != null) {
            stmt.bindString(31, averageWaterQuantity);
        }
 
        String processReason = entity.getProcessReason();
        if (processReason != null) {
            stmt.bindString(32, processReason);
        }
 
        String arrearageRange = entity.getArrearageRange();
        if (arrearageRange != null) {
            stmt.bindString(33, arrearageRange);
        }
 
        String arrearageMoney = entity.getArrearageMoney();
        if (arrearageMoney != null) {
            stmt.bindString(34, arrearageMoney);
        }
 
        String taskOrigin = entity.getTaskOrigin();
        if (taskOrigin != null) {
            stmt.bindString(35, taskOrigin);
        }
        stmt.bindLong(36, entity.getProcessDate());
        stmt.bindLong(37, entity.getDispatchTime());
        stmt.bindLong(38, entity.getEndDate());
 
        String responseType = entity.getResponseType();
        if (responseType != null) {
            stmt.bindString(39, responseType);
        }
 
        String responseContent = entity.getResponseContent();
        if (responseContent != null) {
            stmt.bindString(40, responseContent);
        }
 
        String volume = entity.getVolume();
        if (volume != null) {
            stmt.bindString(41, volume);
        }
        stmt.bindLong(42, entity.getVolumeIndex());
 
        String supplyWater = entity.getSupplyWater();
        if (supplyWater != null) {
            stmt.bindString(43, supplyWater);
        }
 
        String acceptGroup = entity.getAcceptGroup();
        if (acceptGroup != null) {
            stmt.bindString(44, acceptGroup);
        }
 
        String resolveLevel = entity.getResolveLevel();
        if (resolveLevel != null) {
            stmt.bindString(45, resolveLevel);
        }
 
        String responseArea = entity.getResponseArea();
        if (responseArea != null) {
            stmt.bindString(46, responseArea);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(47, remark);
        }
 
        String extend = entity.getExtend();
        if (extend != null) {
            stmt.bindString(48, extend);
        }
    }

    @Override
    protected final void attachEntity(Task entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Task readEntity(Cursor cursor, int offset) {
        Task entity = new Task( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // account
            cursor.getLong(offset + 2), // taskId
            cursor.getLong(offset + 3), // hotTaskId
            cursor.getInt(offset + 4), // type
            cursor.getInt(offset + 5), // subType
            cursor.getInt(offset + 6), // detailType
            cursor.getInt(offset + 7), // state
            cursor.getInt(offset + 8), // hangUpState
            cursor.getInt(offset + 9), // delayState
            cursor.getInt(offset + 10), // isAssist
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // cardId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // cardName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // contacts
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // processPerson
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // acceptName
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // assistPerson
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // driver
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // telephone
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // address
            cursor.getDouble(offset + 20), // longitude
            cursor.getDouble(offset + 21), // latitude
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // barCode
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // station
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // caliber
            cursor.getInt(offset + 25), // lastReading
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // prepaidMoney
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // meterPosition
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // meterDegree
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // meterType
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // averageWaterQuantity
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // processReason
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // arrearageRange
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // arrearageMoney
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34), // taskOrigin
            cursor.getLong(offset + 35), // processDate
            cursor.getLong(offset + 36), // dispatchTime
            cursor.getLong(offset + 37), // endDate
            cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38), // responseType
            cursor.isNull(offset + 39) ? null : cursor.getString(offset + 39), // responseContent
            cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40), // volume
            cursor.getInt(offset + 41), // volumeIndex
            cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42), // supplyWater
            cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43), // acceptGroup
            cursor.isNull(offset + 44) ? null : cursor.getString(offset + 44), // resolveLevel
            cursor.isNull(offset + 45) ? null : cursor.getString(offset + 45), // responseArea
            cursor.isNull(offset + 46) ? null : cursor.getString(offset + 46), // remark
            cursor.isNull(offset + 47) ? null : cursor.getString(offset + 47) // extend
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Task entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccount(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTaskId(cursor.getLong(offset + 2));
        entity.setHotTaskId(cursor.getLong(offset + 3));
        entity.setType(cursor.getInt(offset + 4));
        entity.setSubType(cursor.getInt(offset + 5));
        entity.setDetailType(cursor.getInt(offset + 6));
        entity.setState(cursor.getInt(offset + 7));
        entity.setHangUpState(cursor.getInt(offset + 8));
        entity.setDelayState(cursor.getInt(offset + 9));
        entity.setIsAssist(cursor.getInt(offset + 10));
        entity.setCardId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCardName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setContacts(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setProcessPerson(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setAcceptName(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setAssistPerson(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setDriver(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setTelephone(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setAddress(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setLongitude(cursor.getDouble(offset + 20));
        entity.setLatitude(cursor.getDouble(offset + 21));
        entity.setBarCode(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setStation(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setCaliber(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setLastReading(cursor.getInt(offset + 25));
        entity.setPrepaidMoney(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setMeterPosition(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setMeterDegree(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setMeterType(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setAverageWaterQuantity(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setProcessReason(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setArrearageRange(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setArrearageMoney(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setTaskOrigin(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
        entity.setProcessDate(cursor.getLong(offset + 35));
        entity.setDispatchTime(cursor.getLong(offset + 36));
        entity.setEndDate(cursor.getLong(offset + 37));
        entity.setResponseType(cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38));
        entity.setResponseContent(cursor.isNull(offset + 39) ? null : cursor.getString(offset + 39));
        entity.setVolume(cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40));
        entity.setVolumeIndex(cursor.getInt(offset + 41));
        entity.setSupplyWater(cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42));
        entity.setAcceptGroup(cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43));
        entity.setResolveLevel(cursor.isNull(offset + 44) ? null : cursor.getString(offset + 44));
        entity.setResponseArea(cursor.isNull(offset + 45) ? null : cursor.getString(offset + 45));
        entity.setRemark(cursor.isNull(offset + 46) ? null : cursor.getString(offset + 46));
        entity.setExtend(cursor.isNull(offset + 47) ? null : cursor.getString(offset + 47));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Task entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Task entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Task entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getTemporaryDao().getAllColumns());
            builder.append(" FROM TASK T");
            builder.append(" LEFT JOIN TEMPORARY T0 ON T.\"TASK_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Task loadCurrentDeep(Cursor cursor, boolean lock) {
        Task entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Temporary temporaries = loadCurrentOther(daoSession.getTemporaryDao(), cursor, offset);
         if(temporaries != null) {
            entity.setTemporaries(temporaries);
        }

        return entity;    
    }

    public Task loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Task> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Task> list = new ArrayList<Task>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Task> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Task> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
