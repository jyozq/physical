package com.straw.lession.physical.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/24.
 */
public class InstituteVo {
    public long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(long instituteId) {
        this.instituteId = instituteId;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public List<ClassInfoVo> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassInfoVo> classes) {
        this.classes = classes;
    }

    public List<CourseDefineVo> getCourseDefines() {
        return courseDefines;
    }

    public void setCourseDefines(List<CourseDefineVo> courseDefines) {
        this.courseDefines = courseDefines;
    }

    private long instituteId;
    private String instituteName;
    List<ClassInfoVo> classes = new ArrayList<ClassInfoVo>();
    List<CourseDefineVo> courseDefines = new ArrayList<CourseDefineVo>();

}
