package com.straw.lession.physical.vo;

/**
 * Created by straw on 2016/7/29.
 */
public class UploadStudentDataVo {
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getBindingTime() {
        return bindingTime;
    }

    public void setBindingTime(String bindingTime) {
        this.bindingTime = bindingTime;
    }

    private String studentId;
    private String deviceCode;
    private String bindingTime;
}
