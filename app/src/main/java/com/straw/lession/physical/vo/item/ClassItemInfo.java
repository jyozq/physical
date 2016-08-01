package com.straw.lession.physical.vo.item;

import java.io.Serializable;

/**
 * Created by straw on 2016/7/26.
 */
public class ClassItemInfo implements Serializable{
    private long classId;
    private long classIdR;
    private long instituteId;
    private long instituteIdR;
    private String className;
    private int totalNum;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getClassIdR() {
        return classIdR;
    }

    public void setClassIdR(long classIdR) {
        this.classIdR = classIdR;
    }

    public long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(long instituteId) {
        this.instituteId = instituteId;
    }

    public long getInstituteIdR() {
        return instituteIdR;
    }

    public void setInstituteIdR(long instituteIdR) {
        this.instituteIdR = instituteIdR;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
