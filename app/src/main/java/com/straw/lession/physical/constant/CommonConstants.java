package com.straw.lession.physical.constant;

/**
 * Created by straw on 2016/7/31.
 */
public class CommonConstants {
    public static final int OPR_EDIT = 0;
    public static final int OPR_DEL = 1;

    public static final long EXPIRE_DURATION = 60*1000;

    public static final String UPLOAD_DATA_SUCCESS = "S";
    public static final String UPLOAD_DATA_FAILURE = "F";

    public enum UseOnce {
        USE_ONCE_NOT(0,"非临时课程"),
        USE_ONCE(1,"临时课程");


        private int value;
        private String text;

        private UseOnce(int value, String text){
            this.value = value;
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static String getName(int val) {
            for (UseOnce c : UseOnce.values()) {
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
}
