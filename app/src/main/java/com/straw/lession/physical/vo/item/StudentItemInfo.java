package com.straw.lession.physical.vo.item;

/**
 * Created by straw on 2016/7/11.
 */
public class StudentItemInfo {
    private long id;
    private String name;
    private String code;
    private int gender;
    private long studentIdR;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getStudentIdR() {
        return studentIdR;
    }

    public void setStudentIdR(long studentIdR) {
        this.studentIdR = studentIdR;
    }
}
