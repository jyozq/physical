package com.straw.lession.physical.vo;

/**
 * Created by straw on 2016/7/24.
 */
public class CourseDefineVo {
    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public long getCourseDefineId() {
        return courseDefineId;
    }

    public void setCourseDefineId(long courseDefineId) {
        this.courseDefineId = courseDefineId;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseSeq() {
        return courseSeq;
    }

    public void setCourseSeq(int courseSeq) {
        this.courseSeq = courseSeq;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(long instituteId) {
        this.instituteId = instituteId;
    }

    public int getUseOnce() {
        return useOnce;
    }

    public void setUseOnce(int useOnce) {
        this.useOnce = useOnce;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    private long classId;
    private String courseCode;
    private String courseDate;
    private long courseDefineId;
    private String courseLocation;
    private String courseName;
    private int courseSeq;
    private String courseType;
    private long instituteId;
    private int useOnce;
    private int weekday;
}
