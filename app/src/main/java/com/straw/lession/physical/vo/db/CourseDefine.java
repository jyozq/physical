package com.straw.lession.physical.vo.db;

import java.util.Date;

/**
 * Created by straw on 2016/7/15.
 */
public class CourseDefine {
    private long courseDefineId;
    private String code;
    private String type;
    private String name;
    private long instituteId;
    private long classId;
    private long teacherId;
    private int weekDay;
    private Date date;
    private int seq;
    private String location;
    private Date startTime;
    private Date endTime;
    private int useOnce;

    public long getCourseDefineId() {
        return courseDefineId;
    }

    public void setCourseDefineId(long courseDefineId) {
        this.courseDefineId = courseDefineId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(long instituteId) {
        this.instituteId = instituteId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getUseOnce() {
        return useOnce;
    }

    public void setUseOnce(int useOnce) {
        this.useOnce = useOnce;
    }
}
