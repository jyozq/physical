package com.straw.lession.physical.vo.db;

import java.util.Date;

/**
 * Created by straw on 2016/7/15.
 */
public class Course {
    private long courseDefineId;
    private String courseCode;
    private String courseType;
    private String courseName;
    private long instituteId;
    private long classId;
    private long teacherId;
    private int weekDay;
    private Date courseDate;
    private int courseSeq;
    private String courseLocation;
    private Date startTime;
    private Date endTime;
    private int useOnce;

    public long getCourseDefineId() {
        return courseDefineId;
    }

    public void setCourseDefineId(long courseDefineId) {
        this.courseDefineId = courseDefineId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public Date getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Date courseDate) {
        this.courseDate = courseDate;
    }

    public int getCourseSeq() {
        return courseSeq;
    }

    public void setCourseSeq(int courseSeq) {
        this.courseSeq = courseSeq;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
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
