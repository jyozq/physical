package com.straw.lession.physical.vo;

/**
 * Created by straw on 2016/7/29.
 */
public class UploadCourseDataResultVo {
    public long getLocalCourseSeq() {
        return localCourseSeq;
    }

    public void setLocalCourseSeq(long localCourseSeq) {
        this.localCourseSeq = localCourseSeq;
    }

    public String getSyncResult() {
        return syncResult;
    }

    public void setSyncResult(String syncResult) {
        this.syncResult = syncResult;
    }

    public String getSyncResultMsg() {
        return syncResultMsg;
    }

    public void setSyncResultMsg(String syncResultMsg) {
        this.syncResultMsg = syncResultMsg;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    private long localCourseSeq;
    private String syncResult;
    private String syncResultMsg;
    private long courseId;
}
