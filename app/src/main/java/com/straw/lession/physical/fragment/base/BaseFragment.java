package com.straw.lession.physical.fragment.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.LoginActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.TokenInfo;
import org.json.JSONObject;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * Created by Administrator on 2015/12/10.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "BaseFragment";
    protected Activity mActivity;

    protected MainApplication mApp;
    protected ThreadPoolExecutor mThreadPool;
    private View viewTip ;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        mApp = (MainApplication) mActivity.getApplication();
        mThreadPool = mApp.getThreadPool();
        viewTip = mActivity.getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Fragment页面起始 (注意： 如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         * 此处因只含有Activity和Fragment,必不能调用onContainerFragmentStart/onContainerFragmentEnd
         */
        //MobCreditEase.onFragmentStart(getActivity(), "BaseFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * Fragment 页面结束（注意：如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         * 此处因只含有Activity和Fragment,务必不能调用onContainerFragmentStart/onContainerFragmentEnd
         */
        //MobCreditEase.onFragmentEnd(getActivity(), "BaseFragment");
    }

    // 判断网络是否可用
    public void getDataByNetSate(){
        if(isOpenNetwork() == true) {
            loadDataFromService();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
                        startActivity(intent);
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
                }
            }).show();
        }
    }

    protected abstract void loadDataFromLocal();

    protected abstract void loadDataFromService();

    /**
     * 对网络连接状态进行判断
     * @return  true, 可用； false， 不可用
     */
    public boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager)mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    public void checkTokenInfo() {
        try {
            final TokenInfo tokenInfo = AppPreference.getUserToken();
            long tokenExpireTime = DateUtil.formatStrToDateTime(tokenInfo.getTokenExpireTime()).getTime();
            long nowTime = System.currentTimeMillis();
            if(nowTime >= tokenExpireTime){
                AppPreference.logout();
                MainApplication.getInstance().exit();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }else{
                long duration = tokenExpireTime - nowTime;
                if(duration <= CommonConstants.EXPIRE_DURATION){
                    String URL = ReqConstant.URL_BASE + "/auth/token/refresh";
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET, URL ,"" , tokenInfo.getToken(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(HttpResponseBean httpResponseBean) {
                            super.onSuccess(httpResponseBean);
                            try{
                                JSONObject contentObject = new JSONObject(httpResponseBean.content);
                                String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                                if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
                                    JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                                    String newToken = dataObject.getString(ParamConstant.USER_TOKEN);
                                    String expireTime = dataObject.getString(ParamConstant.TOKEN_EXPIRE_TIME);
                                    tokenInfo.setToken(newToken);
                                    tokenInfo.setTimeStamp(System.currentTimeMillis());
                                    tokenInfo.setTokenExpireTime(expireTime);
                                    Log.i(TAG,"token:"+newToken);
                                    Log.i(TAG,"expireTime:"+expireTime);
                                    AppPreference.saveToken(tokenInfo);
                                    doAfterGetToken();
                                }else {//登录失败
                                    String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
                                    AlertDialogUtil.showAlertWindow(mActivity, -1, errorMessage , null );
                                    throw new IllegalStateException(errorMessage);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                throw new IllegalStateException(e.toString());
                            }
                        }
                        @Override
                        public void onFailure(Throwable error, String content) {
                            super.onFailure(error, content);
                            String errorContent = Utils.parseErrorMessage(mActivity, content);
                            throw new IllegalStateException(errorContent);
                        }
                    });
                    mThreadPool.execute(asyncHttpClient);
                }else{
                    doAfterGetToken();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("获取token出错");
        }
    }

    public abstract void doAfterGetToken();
}
