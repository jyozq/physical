package com.straw.lession.physical.activity.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.crouton.Crouton;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.custom.ProgressDialogFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.TokenInfo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by straw on 2016/7/2.
 */
public abstract class ThreadBaseActivity extends AppCompatActivity {
    private final static String TAG = "ThreadBaseActivity";
    protected Activity mContext = ThreadBaseActivity.this;
    protected MainApplication mApp;
    protected ThreadPoolExecutor mThreadPool;
    protected ProgressDialogFragment mProgressDialog;

    protected int pageNumber = 1;
    protected final static int pageCount = 20;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mApp = (MainApplication) mContext.getApplication();
        mThreadPool = mApp.getThreadPool();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         */
        //MobCreditEase.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         */
        //MobCreditEase.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showProgressDialog(String msg){
        hideProgressDialog();
        mProgressDialog = ProgressDialogFragment.getInstance(ProgressDialogFragment.DEFAULT, msg);
        //mProgressDialog.show(getFragmentManager(), null);
        mProgressDialog.show(getSupportFragmentManager() , null);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismissAllowingStateLoss();
            //mProgressDialog.dismiss();
        }
    }


    public void isShowProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isHidden()){
            }else {
            }
            if (mProgressDialog.isVisible()){
            }else{
            }
        }
    }

    /*public void showErrorMsgInfo(String errorContent){
        if (null == mMessageBar){
            mMessageBar = new MessageBar(mContext);
        }
        mMessageBar.show(errorContent);
    }*/


    public void showErrorMsgInfo(String errmsg) {
        View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        TextView textView =(TextView) view.findViewById(R.id.errorText);
        textView.setText(errmsg);
        final Crouton crouton;
        //if (displayOnTop.isChecked()) {

        //} else {
        //   crouton = Crouton.make(mContext, view, R.id.alternate_view_group);
        //}

        crouton = Crouton.make(mContext, view);
        crouton.show();
    }

    /**
     * 对网络连接状态进行判断
     * @return  true, 可用； false， 不可用
     */
    public boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }


    // 判断网络是否可用
    public void getDataByNetSate(){
        if(isOpenNetwork() == true) {
            loadDataFromService();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ThreadBaseActivity.this);
            builder.setTitle("没有可用的网络").setMessage("是否对网络进行设置?");

            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = null;

                    try {
                        String sdkVersion = android.os.Build.VERSION.SDK;
                        if(Integer.valueOf(sdkVersion) > 10) {
                            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        }else {
                            intent = new Intent();
                            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        ThreadBaseActivity.this.startActivity(intent);
                    } catch (Exception e) {
                        Log.w(TAG, "open network settings failed, please check...");
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadDataFromLocal();
                    dialog.cancel();
                    finish();
                }
            }).show();
        }
    }

    public void checkTokenInfo(final TokenInfo tokenInfo) {
        if(System.currentTimeMillis() - tokenInfo.getTimeStamp() > 60*1000){
            String URL = ReqConstant.URL_BASE + "/auth/token/refresh";
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET, URL ,null , null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(HttpResponseBean httpResponseBean) {
                    super.onSuccess(httpResponseBean);
                    try{
                        hideProgressDialog();
                        JSONObject contentObject = new JSONObject(httpResponseBean.content);
                        String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                        if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){ //登录成功
                            JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                            String newToken = dataObject.getString("newToken");
                            tokenInfo.setToken(newToken);
                            tokenInfo.setTimeStamp(System.currentTimeMillis());
                            AppPreference.saveToken(tokenInfo);
                        }else {//登录失败
                            String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
                            AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
                            throw new IllegalStateException(errorMessage);
                        }
                    }catch(Exception e){
                        hideProgressDialog();
                        showErrorMsgInfo(e.toString());
                        e.printStackTrace();
                        throw new IllegalStateException(e.toString());
                    }
                }
                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
                    hideProgressDialog();
                    String errorContent = Utils.parseErrorMessage(mContext, content);
                    showErrorMsgInfo(errorContent);
                    Log.e(TAG, content);
                    throw new IllegalStateException(errorContent);
                }
            });
            mThreadPool.execute(asyncHttpClient);
        }

    }

    protected abstract void loadDataFromService();

    protected abstract void loadDataFromLocal();
}