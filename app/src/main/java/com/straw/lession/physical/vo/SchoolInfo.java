package com.straw.lession.physical.vo;

/**
 * Created by straw on 2016/7/12.
 */
public class SchoolInfo {
    private long id;
    private String name;
    private String code;

    public SchoolInfo(String name, String code){
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String toString(){
        return name;
    }
}
