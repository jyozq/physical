package com.straw.lession.physical.vo.item;

import java.io.Serializable;

/**
 * Created by straw on 2016/7/11.
 */
public class StudentItemInfo implements Serializable{
    private long id;
    private String name;
    private String code;
    private int gender;
    private long studentIdR;
    private long classIdR;
    private String className;
    private long courseDefindIdR;
    private boolean hasBinded;
    private String deviceNo;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getStudentIdR() {
        return studentIdR;
    }

    public void setStudentIdR(long studentIdR) {
        this.studentIdR = studentIdR;
    }

    public long getClassIdR() {
        return classIdR;
    }

    public void setClassIdR(long classIdR) {
        this.classIdR = classIdR;
    }

    public long getCourseDefindIdR() {
        return courseDefindIdR;
    }

    public void setCourseDefindIdR(long courseDefindIdR) {
        this.courseDefindIdR = courseDefindIdR;
    }

    public boolean isHasBinded() {
        return hasBinded;
    }

    public void setHasBinded(boolean hasBinded) {
        this.hasBinded = hasBinded;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
}
