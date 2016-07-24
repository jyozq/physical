package com.straw.lession.physical.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/24.
 */
public class Institute {
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

    public List<ClassInfo> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassInfo> classes) {
        this.classes = classes;
    }

    public List<CourseDefine> getCourseDefines() {
        return courseDefines;
    }

    public void setCourseDefines(List<CourseDefine> courseDefines) {
        this.courseDefines = courseDefines;
    }

    private long instituteId;
    private String instituteName;
    List<ClassInfo> classes = new ArrayList<ClassInfo>();
    List<CourseDefine> courseDefines = new ArrayList<CourseDefine>();

}
