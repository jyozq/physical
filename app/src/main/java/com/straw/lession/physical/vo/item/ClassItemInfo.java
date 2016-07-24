package com.straw.lession.physical.vo.item;

import java.io.Serializable;

/**
 * Created by straw on 2016/7/24.
 */
public class ClassItemInfo implements Serializable{
    private long classId;
    private String className;

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
}
