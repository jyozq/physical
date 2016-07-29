package com.straw.lession.physical.vo;

import java.util.List;

/**
 * Created by straw on 2016/7/29.
 */
public class UploadCourseDataVo {
    public String getLocalCourseSeq() {
        return localCourseSeq;
    }

    public void setLocalCourseSeq(String localCourseSeq) {
        this.localCourseSeq = localCourseSeq;
    }

    public String getCourseDefineId() {
        return courseDefineId;
    }

    public void setCourseDefineId(String courseDefineId) {
        this.courseDefineId = courseDefineId;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<UploadStudentDataVo> getStudents() {
        return students;
    }

    public void setStudents(List<UploadStudentDataVo> students) {
        this.students = students;
    }

    private String localCourseSeq;
    private String courseDefineId;
    private String courseDate;
    private String startTime;
    private String endTime;
    private List<UploadStudentDataVo> students;
}
