package com.straw.lession.physical.vo.db;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import com.straw.lession.physical.db.DaoSession;
import org.greenrobot.greendao.DaoException;

import com.straw.lession.physical.db.TeacherDao;
import com.straw.lession.physical.db.TeacherInstituteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "TEACHER".
 */
@Entity(active = true)
public class Teacher {
    private String name;
    private String mobile;
    private java.util.Date last_login_time;

    @Id
    private Long teacherIdR;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient TeacherDao myDao;

    @ToMany(joinProperties = {
        @JoinProperty(name = "teacherIdR", referencedName = "teacherIdR")
    })
    private List<TeacherInstitute> teacherInstitutes;

    @Generated
    public Teacher() {
    }

    public Teacher(Long teacherIdR) {
        this.teacherIdR = teacherIdR;
    }

    @Generated
    public Teacher(String name, String mobile, java.util.Date last_login_time, Long teacherIdR) {
        this.name = name;
        this.mobile = mobile;
        this.last_login_time = last_login_time;
        this.teacherIdR = teacherIdR;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTeacherDao() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public java.util.Date getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(java.util.Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public Long getTeacherIdR() {
        return teacherIdR;
    }

    public void setTeacherIdR(Long teacherIdR) {
        this.teacherIdR = teacherIdR;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    @Generated
    public List<TeacherInstitute> getTeacherInstitutes() {
        if (teacherInstitutes == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeacherInstituteDao targetDao = daoSession.getTeacherInstituteDao();
            List<TeacherInstitute> teacherInstitutesNew = targetDao._queryTeacher_TeacherInstitutes(teacherIdR);
            synchronized (this) {
                if(teacherInstitutes == null) {
                    teacherInstitutes = teacherInstitutesNew;
                }
            }
        }
        return teacherInstitutes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated
    public synchronized void resetTeacherInstitutes() {
        teacherInstitutes = null;
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
