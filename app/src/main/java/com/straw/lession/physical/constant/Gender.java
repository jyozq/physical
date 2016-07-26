package com.straw.lession.physical.constant;

/**
 * Created by straw on 2016/7/22.
 */
public enum Gender {
    MALE(1,"男"),
    FEMAIL(2,"女");

    private int value;
    private String text;

    private Gender(int value, String text){
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String getName(int val) {
        for (Gender c : Gender.values()) {
            if (c.getValue() == val) {
                return c.text;
            }
        }
        return null;
    }

    public long getValue() {
        return this.value;
    }
}
