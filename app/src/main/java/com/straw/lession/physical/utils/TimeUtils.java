package com.straw.lession.physical.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理工具
 */
public class TimeUtils {

    public static String nowFormat( String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date());
    }

    public static String timeFormat(long timeMillis, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time){
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatDateStr(){
        return nowFormat( "yyyy-MM-dd");
    }
    public static String formatTimeStr(){
        return nowFormat( "HH:mm");
    }

    public static String formatDate(long time){
        return timeFormat(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatPhotoDate(String path){
        File file = new File(path);
        if(file.exists()){
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }






    public static String getCurrentDateStr() {
        Date dNow = new Date();  //当前时间
        Calendar calendar = Calendar.getInstance();//得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd HH:mm"); //设置时间格式
        String currentdate = sdf.format(dNow); //格式化当前时间
        return currentdate;
    }
    public static String getCurrentDateStr2() {
        Date dNow = new Date();  //当前时间
        Calendar calendar = Calendar.getInstance();//得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //设置时间格式
        String currentdate = sdf.format(dNow); //格式化当前时间
        return currentdate;
    }

    //某个时间的后7天
    public static String getNextWeekStr(String datetime) {
        //Date dNow = new Date();  //当前时间
        //Date dAfter = new Date();
        Date dAfter = getTime(datetime);
        Calendar calendar = Calendar.getInstance();//得到日历
        calendar.setTime(dAfter);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, +7); //设置为前7天
        dAfter = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd HH:mm"); //设置时间格式
        String dAfterdate = sdf.format(dAfter);   //格式化前一天
        return dAfterdate;
    }

    //某个时间的后7天
    public static Date getNextWeekDate(String datetime) {
        //Date dNow = new Date();  //当前时间
        //Date dAfter = new Date();
        Date dAfter = getTime(datetime);
        Calendar calendar = Calendar.getInstance();//得到日历
        calendar.setTime(dAfter);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, +7); //设置为前7天
        dAfter = calendar.getTime();   //得到前一天的时间
        return dAfter;
    }

    public static Date getNextWeekDate2(String datetime) {
        //Date dNow = new Date();  //当前时间
        //Date dAfter = new Date();
        try{
            Date dAfter = getTime2(datetime);
            Calendar calendar = Calendar.getInstance();//得到日历
            calendar.setTime(dAfter);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, +7); //设置为前7天
            dAfter = calendar.getTime();   //得到前一天的时间
            return dAfter;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Date getTime(String date){
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd HH:mm");
            Date d = simpledateformat.parse(date);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Date getTime2(String date){
        try {
            //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd HH:mm");
            //Date d = simpledateformat.parse(date);

            SimpleDateFormat simpledateformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //String d2 = simpledateformat2.format(d);
            //String d2 = simpledateformat2.format(date);
            Date newdate = simpledateformat2.parse(date);
            return newdate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否登录
     * @param mContext
     * @return
     */
    public static boolean isLogin(Context mContext){
        try{
            SharedPreferences sharedPreferences = AppPreference.getLoginTime(mContext);
            String loginTime = sharedPreferences.getString(AppPreference.loginTime,"");
            if (null ==loginTime || loginTime.equals("")){
                return false;
            }
            Date afterDate = TimeUtils.getNextWeekDate2(loginTime);
            if (null != afterDate){
                Date nowDate = new Date();
                if (nowDate.getTime() >= afterDate.getTime()){  //登录过期
                    return false;
                }else {   //有效期内
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }



}
