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
