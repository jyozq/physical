package com.straw.lession.physical.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/21.
 */
public class AppPreference {

    public final static String loginTime = "login_time";
    public final static String userName = "user_name"; //用户名
    public final static String id = "_id";
    public final static String mobile = "mobile";  //手机号
    public final static String business_license = "business_license";  //营业执照
    public final static String merchant_no = "merchant_no";  //商户编号
    public final static String introducer_name ="introducer_name"; //推荐人

    public final static String NEWSINFO_NAME = "newsinfo_name";

    public final static String NEWS_STATUS = "news_status";

    public final static String BANNER_NAME = "banner_name";

    //public final static String user_Id = "user_id";

    //记住登录信息
    public static void saveLoginTime (Context paramContext , String _loginTime , String _userName  ,String _id , String _mobile
    ,String _business_license , String _merchant_no ,String _introducer_name){
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("com.creditease.sxd.android.client.logininfo", 0).edit();
        localEditor.putString(loginTime , _loginTime);
        localEditor.putString(userName , _userName);
        localEditor.putString(id , _id);
        localEditor.putString(mobile , _mobile);
        localEditor.putString(business_license , _business_license);
        localEditor.putString(merchant_no , _merchant_no);
        localEditor.putString(introducer_name , _introducer_name);
        localEditor.commit();
    }

    public static void saveLoginTime (Context paramContext , String _loginTime  ){
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("com.creditease.sxd.android.client.logininfo", 0).edit();
        localEditor.putString(loginTime , _loginTime);
        localEditor.commit();
    }


    //记住登录信息
    public static SharedPreferences getLoginTime (Context paramContext ){
        //return paramContext.getSharedPreferences("com.creditease.sxd.android.client.logintime", 0).getString(loginTime ,defaultvalue);
        SharedPreferences sharedPreferences =  paramContext.getSharedPreferences("com.creditease.sxd.android.client.logininfo", 0);
        return sharedPreferences;
    }


    //保存新消息状态
    public static void saveNewsStatus (Context paramContext , String _new_status  ){
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("com.creditease.sxd.android.client.news_status", 0).edit();
        localEditor.putString(NEWS_STATUS , _new_status);
        localEditor.commit();
    }

    //获取新消息状态
    public static String getNewsStatus (Context paramContext ){
        SharedPreferences sharedPreferences =  paramContext.getSharedPreferences("com.creditease.sxd.android.client.news_status", 0);
        return sharedPreferences.getString(NEWS_STATUS ,"");
    }





    //保存banner
    public static void bannerWriteToLocal (Context context , ArrayList<String[]> list ) throws Exception {
        String dataSystemDirectory = context.getFilesDir().getAbsolutePath();
        File file = new File(dataSystemDirectory , BANNER_NAME);
        try {
            if(!file.exists()){
                file.createNewFile();
            }else{
                file.delete();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.flush();
            oos.close();

        }catch(IOException e){
            file.delete();
            throw new Exception(e);
        }finally {

        }
    }

    //获取banner
    public static ArrayList<String[]> getbannerFromLocal(Context context ) throws Exception{
        String dataSystemDirectory = context.getFilesDir().getAbsolutePath();
        File file = new File(dataSystemDirectory , BANNER_NAME);
        try {
            if (file.exists()){
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<String[]> list =(ArrayList<String[]>) ois.readObject();
                ois.close();
                return list;
            }
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }



}
