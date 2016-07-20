package com.straw.lession.physical.vo;

/**
 * Created by straw on 2016/7/20.
 */
public class TokenInfo {
    private String token;
    private long timeStamp;

    public TokenInfo(String userToken){
        this.token = userToken;
        timeStamp = System.currentTimeMillis();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
