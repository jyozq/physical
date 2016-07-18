package com.straw.lession.physical.data;

/**
 * Created by straw on 2016/7/18.
 */
public enum DataSource {
    LOCALSTORAGE(0,"本地缓存"),
    REMOTESERVICE(1,"远程服务");

    private int value;
    private String text;

    private DataSource(int value, String text){
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public String getName() {
        return this.name();
    }

    public long getValue() {
        return this.value;
    }
}
