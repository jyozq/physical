package com.straw.lession.physical.vo.db;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import com.straw.lession.physical.db.DaoSession;
import org.greenrobot.greendao.DaoException;

import com.straw.lession.physical.db.ClassInfoDao;
import com.straw.lession.physical.db.InstituteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "INSTITUTE".
 */
@Entity(active = true)
public class Institute {
    private String code;
    private String name;

    @Id
    private Long instituteIdR;
    private Integer isDel;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient InstituteDao myDao;

    @ToMany(joinProperties = {
        @JoinProperty(name = "instituteIdR", referencedName = "instituteIdR")
    })
    private List<ClassInfo> classes;

    @Generated
    public Institute() {
    }

    public Institute(Long instituteIdR) {
        this.instituteIdR = instituteIdR;
    }

    @Generated
    public Institute(String code, String name, Long instituteIdR, Integer isDel) {
        this.code = code;
        this.name = name;
        this.instituteIdR = instituteIdR;
        this.isDel = isDel;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInstituteDao() : null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInstituteIdR() {
        return instituteIdR;
    }

    public void setInstituteIdR(Long instituteIdR) {
        this.instituteIdR = instituteIdR;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    @Generated
    public List<ClassInfo> getClasses() {
        if (classes == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ClassInfoDao targetDao = daoSession.getClassInfoDao();
            List<ClassInfo> classesNew = targetDao._queryInstitute_Classes(instituteIdR);
            synchronized (this) {
                if(classes == null) {
                    classes = classesNew;
                }
            }
        }
        return classes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated
    public synchronized void resetClasses() {
        classes = null;
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
