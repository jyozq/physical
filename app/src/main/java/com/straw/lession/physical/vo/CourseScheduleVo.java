package com.straw.lession.physical.vo;

/**
 * Created by straw on 2016/7/30.
 */
public class CourseScheduleVo {
    public long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(long instituteId) {
        this.instituteId = instituteId;
    }

    public SortedCourseDefinesVo getSortedCourseDefines() {
        return sortedCourseDefines;
    }

    public void setSortedCourseDefines(SortedCourseDefinesVo sortedCourseDefines) {
        this.sortedCourseDefines = sortedCourseDefines;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    private long instituteId;
    private SortedCourseDefinesVo sortedCourseDefines;
    private String instituteName;
}
