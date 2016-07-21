package com.straw.lession.physical.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/15.
 */
public class LoginInfo {
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public List<Institute> getInstitutes() {
        return institutes;
    }

    public void setInstitutes(List<Institute> institutes) {
        this.institutes = institutes;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    private long teacherId;
    private String birthday;
    private int gender;
    private String lastLoginIp;
    private String lastLoginTime;
    private String mobile;
    private String personName;
    private long userId;
    private int userStatus;
    private String nowTime;
    private List<Institute> institutes = new ArrayList<Institute>();

    public class Institute{
        public String getInsName() {
            return insName;
        }

        public void setInsName(String insName) {
            this.insName = insName;
        }

        public long getInstituteId() {
            return instituteId;
        }

        public void setInstituteId(long instituteId) {
            this.instituteId = instituteId;
        }

        public int getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(int isAdmin) {
            this.isAdmin = isAdmin;
        }

        private String insName;
        private long instituteId;
        private int isAdmin;
    }
}
