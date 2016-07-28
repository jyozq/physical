package com.straw.lession.physical.constant;

/**
 * Created by straw on 2016/7/28.
 */
public enum CourseStatus {
    UNSTARTED(0,"开始上课"),
    STARTED(1,"正在上课"),
    OVER(2,"课程完成");


    private int value;
    private String text;

    private CourseStatus(int value, String text){
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String getName(int val) {
        for (CourseStatus c : CourseStatus.values()) {
            if (c.getValue() == val) {
                return c.text;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
