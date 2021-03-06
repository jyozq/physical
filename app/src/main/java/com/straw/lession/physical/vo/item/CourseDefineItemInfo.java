package com.straw.lession.physical.vo.item;

import java.io.Serializable;

/**
 * Created by straw on 2016/7/30.
 */
public class CourseDefineItemInfo implements Serializable{
    public long getCourseDefineId() {
        return courseDefineId;
    }

    public void setCourseDefineId(long courseDefineId) {
        this.courseDefineId = courseDefineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private long courseDefineId = -1l;
    private String name;
    private String date;
    private int weekDay = -1;
    private int seq = -1;
    private String type;
    private String location;
    private String className;
    private long classId = -1l;
    private boolean isChecked;
}
