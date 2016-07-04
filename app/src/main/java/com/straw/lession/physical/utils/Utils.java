package com.straw.lession.physical.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;

/**
 * Created by Administrator on 2015/12/10.
 */
public class Utils {
    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {

    }


    public void systemBarTintUpdate(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, mActivity);
            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);//通知栏所需颜色
        }
    }

    public void systemBarTintUpdate(Activity mActivity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, mActivity);
            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    private void setTranslucentStatus(boolean on, Activity mActivity) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static boolean isNotEmpty(String str) {
        if (null != str && !str.equals("") && !str.equals("null")) {
            return true;
        }
        return false;
    }

    public static String parseErrorMessage(Context mContext, String content) {
        String tipContent = mContext.getResources().getString(R.string.tips_connection_error);
        if (Utils.isNotEmpty(content)) {
            if (content.equals(ResponseParseUtils.ERROR1)) {
                tipContent = mContext.getResources().getString(R.string.tips_malformed_url_error);
            } else if (content.equals(ResponseParseUtils.ERROR2)) {
                tipContent = mContext.getResources().getString(R.string.tips_connection_error);
            } else if (content.equals(ResponseParseUtils.ERROR3)) {
                tipContent = mContext.getResources().getString(R.string.tips_io_error);
            } else if (content.equals(ResponseParseUtils.ERROR4)) {
                tipContent = mContext.getResources().getString(R.string.tips_socket_timeout_error);
            }
        }
        return tipContent;
    }


    /*public static String parseStatus( int status){
        String result = "-";
        if ( StatusEnum.TYPE10.getValue() == status ||
                StatusEnum.TYPE11.getValue() == status ||
                StatusEnum.TYPE12.getValue() == status ||
                StatusEnum.TYPE13.getValue() == status ||
                StatusEnum.TYPE14.getValue() == status ||
                StatusEnum.TYPE19.getValue() == status ||
                StatusEnum.TYPE31.getValue() == status ||
                StatusEnum.TYPE41.getValue() == status ||
                StatusEnum.TYPE51.getValue() == status ) {
            result ="审核中";
        }
        else  if (StatusEnum.TYPE20.getValue() == status) {
            result ="待确认";
        }
        else  if (StatusEnum.TYPE21.getValue() == status) {
            result="待补件";
        }
        else  if (StatusEnum.TYPE61.getValue() == status) {
            result="待放款";
        }
        else  if (StatusEnum.TYPE71.getValue() == status) {
            result ="已放款";
        }
        else  if (StatusEnum.TYPE81.getValue() == status) {
            result="放款中";
        }
        else  if (StatusEnum.TYPE91.getValue() == status) {
            result="已结案";
        }
        else  if (StatusEnum.TYPE101.getValue() == status) {
            result="超时关闭";
        }
        else  if (StatusEnum.TYPE102.getValue() == status) {
            result="拒绝";
        }
        return result;
    }*/


//    public static String getDeviceId(Context context){
//        //TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
//        //String szImei = TelephonyMgr.getDeviceId();
//        //return szImei;
//        SharedPreferences sharedPreferences = AppPreference.getLoginTime(context);
//        String userid = "-1";
//        if (null != sharedPreferences){
//            userid = sharedPreferences.getString(AppPreference.id , "-1")  ;
//        }
//        return userid;
//    }
//
//    public static boolean isEmptyDict(){
//        if ( (MainApplication.getInstance().status_label ==null ) ||( MyApplication.getInstance().pos_card_org_label ==null ) ){
//            return true;
//        }else {
//            return false;
//        }
//    }

    //获取软件版本号
    public static String getSoftVersionName(Context m_context) {
        PackageManager manager = m_context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(m_context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

}
