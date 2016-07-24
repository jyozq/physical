package com.straw.lession.physical.constant;

/**
 * Created by straw on 2016/7/22.
 */
public enum Weekday {
    SUNDAY(0,"星期日"),
    MONDAY(1,"星期一"),
    TUESDAY(2,"星期二"),
    WENSDAY(0,"星期三"),
    THIRSDAY(0,"星期四"),
    FRIDAY(0,"星期五"),
    SATURDAY(0,"星期六");

    private int value;
    private String text;

    private Weekday(int value, String text){
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String getName(int val) {
        for (Weekday c : Weekday.values()) {
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
