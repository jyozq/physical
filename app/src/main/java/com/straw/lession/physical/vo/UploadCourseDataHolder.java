package com.straw.lession.physical.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/30.
 */
public class UploadCourseDataHolder {
    public List<UploadCourseDataVo> getCourseData() {
        return courseData;
    }

    public void setCourseData(List<UploadCourseDataVo> courseData) {
        this.courseData = courseData;
    }

    private List<UploadCourseDataVo> courseData = new ArrayList<>();
}
