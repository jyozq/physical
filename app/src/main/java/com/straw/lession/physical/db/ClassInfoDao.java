package com.straw.lession.physical.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.straw.lession.physical.vo.db.ClassInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CLASS_INFO".
*/
public class ClassInfoDao extends AbstractDao<ClassInfo, Long> {

    public static final String TABLENAME = "CLASS_INFO";

    /**
     * Properties of entity ClassInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Code = new Property(1, String.class, "code", false, "CODE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Type = new Property(3, Integer.class, "type", false, "TYPE");
        public final static Property TotalNum = new Property(4, Integer.class, "totalNum", false, "TOTAL_NUM");
        public final static Property ClassIdR = new Property(5, Long.class, "classIdR", false, "CLASS_ID_R");
        public final static Property InstituteId = new Property(6, Long.class, "instituteId", false, "INSTITUTE_ID");
        public final static Property InstituteIdR = new Property(7, Long.class, "instituteIdR", false, "INSTITUTE_ID_R");
        public final static Property LoginId = new Property(8, Long.class, "loginId", false, "LOGIN_ID");
    };


    public ClassInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ClassInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CLASS_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"CODE\" TEXT," + // 1: code
                "\"NAME\" TEXT," + // 2: name
                "\"TYPE\" INTEGER," + // 3: type
                "\"TOTAL_NUM\" INTEGER," + // 4: totalNum
                "\"CLASS_ID_R\" INTEGER," + // 5: classIdR
                "\"INSTITUTE_ID\" INTEGER," + // 6: instituteId
                "\"INSTITUTE_ID_R\" INTEGER," + // 7: instituteIdR
                "\"LOGIN_ID\" INTEGER);"); // 8: loginId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CLASS_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ClassInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(4, type);
        }
 
        Integer totalNum = entity.getTotalNum();
        if (totalNum != null) {
            stmt.bindLong(5, totalNum);
        }
 
        Long classIdR = entity.getClassIdR();
        if (classIdR != null) {
            stmt.bindLong(6, classIdR);
        }
 
        Long instituteId = entity.getInstituteId();
        if (instituteId != null) {
            stmt.bindLong(7, instituteId);
        }
 
        Long instituteIdR = entity.getInstituteIdR();
        if (instituteIdR != null) {
            stmt.bindLong(8, instituteIdR);
        }
 
        Long loginId = entity.getLoginId();
        if (loginId != null) {
            stmt.bindLong(9, loginId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ClassInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(4, type);
        }
 
        Integer totalNum = entity.getTotalNum();
        if (totalNum != null) {
            stmt.bindLong(5, totalNum);
        }
 
        Long classIdR = entity.getClassIdR();
        if (classIdR != null) {
            stmt.bindLong(6, classIdR);
        }
 
        Long instituteId = entity.getInstituteId();
        if (instituteId != null) {
            stmt.bindLong(7, instituteId);
        }
 
        Long instituteIdR = entity.getInstituteIdR();
        if (instituteIdR != null) {
            stmt.bindLong(8, instituteIdR);
        }
 
        Long loginId = entity.getLoginId();
        if (loginId != null) {
            stmt.bindLong(9, loginId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ClassInfo readEntity(Cursor cursor, int offset) {
        ClassInfo entity = new ClassInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // type
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // totalNum
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // classIdR
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // instituteId
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // instituteIdR
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // loginId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ClassInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setTotalNum(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setClassIdR(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setInstituteId(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setInstituteIdR(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setLoginId(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ClassInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ClassInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
