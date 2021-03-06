package com.straw.lession.physical.vo.db;

import org.greenrobot.greendao.annotation.*;

import com.straw.lession.physical.db.DaoSession;
import org.greenrobot.greendao.DaoException;

import com.straw.lession.physical.db.InstituteDao;
import com.straw.lession.physical.db.TeacherInstituteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "TEACHER_INSTITUTE".
 */
@Entity(active = true)
public class TeacherInstitute {

    @Id
    private Long id;
    private Long instituteIdR;
    private Long teacherIdR;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient TeacherInstituteDao myDao;

    @ToOne(joinProperty = "instituteIdR")
    private Institute institute;

    @Generated
    private transient Long institute__resolvedKey;

    @Generated
    public TeacherInstitute() {
    }

    public TeacherInstitute(Long id) {
        this.id = id;
    }

    @Generated
    public TeacherInstitute(Long id, Long instituteIdR, Long teacherIdR) {
        this.id = id;
        this.instituteIdR = instituteIdR;
        this.teacherIdR = teacherIdR;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTeacherInstituteDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInstituteIdR() {
        return instituteIdR;
    }

    public void setInstituteIdR(Long instituteIdR) {
        this.instituteIdR = instituteIdR;
    }

    public Long getTeacherIdR() {
        return teacherIdR;
    }

    public void setTeacherIdR(Long teacherIdR) {
        this.teacherIdR = teacherIdR;
    }

    /** To-one relationship, resolved on first access. */
    @Generated
    public Institute getInstitute() {
        Long __key = this.instituteIdR;
        if (institute__resolvedKey == null || !institute__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InstituteDao targetDao = daoSession.getInstituteDao();
            Institute instituteNew = targetDao.load(__key);
            synchronized (this) {
                institute = instituteNew;
            	institute__resolvedKey = __key;
            }
        }
        return institute;
    }

    @Generated
    public void setInstitute(Institute institute) {
        synchronized (this) {
            this.institute = institute;
            instituteIdR = institute == null ? null : institute.getInstituteIdR();
            institute__resolvedKey = instituteIdR;
        }
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
