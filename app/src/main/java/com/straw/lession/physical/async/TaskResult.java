package com.straw.lession.physical.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by straw on 2016/7/18.
 */
public class TaskResult {
    private int resultCode;
    private String resultMsg;
    private Map<String, Object> data = new HashMap<String, Object>();

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void addData(String key, Object value){
        data.put(key,value);
    }
}
