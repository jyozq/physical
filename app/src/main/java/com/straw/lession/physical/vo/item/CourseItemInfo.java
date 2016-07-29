package com.straw.lession.physical.vo.item;

import java.io.Serializable;

/**
 * Created by straw on 2016/7/6.
 */
public class CourseItemInfo implements Serializable{
    private long courseId;
    private long courseDefineId;
    private String name;
    private String date;
    private int weekDay;
    private int seq;
    private String type;
    private String location;
    private String className;
    private long classId;
    private int status;
    private int bindedStudentNum;
    private int totalStudentNum;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public int getBindedStudentNum() {
        return bindedStudentNum;
    }

    public void setBindedStudentNum(int bindedStudentNum) {
        this.bindedStudentNum = bindedStudentNum;
    }

    public int getTotalStudentNum() {
        return totalStudentNum;
    }

    public void setTotalStudentNum(int totalStudentNum) {
        this.totalStudentNum = totalStudentNum;
    }
}
