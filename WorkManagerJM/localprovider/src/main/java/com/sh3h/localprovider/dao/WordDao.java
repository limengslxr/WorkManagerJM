package com.sh3h.localprovider.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.localprovider.entity.Word;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WORD".
*/
public class WordDao extends AbstractDao<Word, Long> {

    public static final String TABLENAME = "WORD";

    /**
     * Properties of entity Word.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ServerId = new Property(1, long.class, "serverId", false, "SERVER_ID");
        public final static Property ParentId = new Property(2, long.class, "parentId", false, "PARENT_ID");
        public final static Property Group = new Property(3, String.class, "group", false, "GROUP");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property Value = new Property(5, String.class, "value", false, "VALUE");
        public final static Property Remark = new Property(6, String.class, "remark", false, "REMARK");
    }


    public WordDao(DaoConfig config) {
        super(config);
    }
    
    public WordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SERVER_ID\" INTEGER NOT NULL ," + // 1: serverId
                "\"PARENT_ID\" INTEGER NOT NULL ," + // 2: parentId
                "\"GROUP\" TEXT," + // 3: group
                "\"NAME\" TEXT," + // 4: name
                "\"VALUE\" TEXT," + // 5: value
                "\"REMARK\" TEXT);"); // 6: remark
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WORD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Word entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getServerId());
        stmt.bindLong(3, entity.getParentId());
 
        String group = entity.getGroup();
        if (group != null) {
            stmt.bindString(4, group);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(6, value);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(7, remark);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Word entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getServerId());
        stmt.bindLong(3, entity.getParentId());
 
        String group = entity.getGroup();
        if (group != null) {
            stmt.bindString(4, group);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(6, value);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(7, remark);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Word readEntity(Cursor cursor, int offset) {
        Word entity = new Word( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // serverId
            cursor.getLong(offset + 2), // parentId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // group
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // value
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // remark
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Word entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setServerId(cursor.getLong(offset + 1));
        entity.setParentId(cursor.getLong(offset + 2));
        entity.setGroup(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setValue(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRemark(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Word entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Word entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Word entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
