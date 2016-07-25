package com.straw.lession.physical.activity.base;

import android.os.Bundle;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2015/12/23.
 */
public abstract class ThreadToolBarBaseActivity extends ToolBarActivity {
    private final static String TAG = "ThreadToolBarBaseActivity";
    protected MainApplication mApp;
    protected ThreadPoolExecutor mThreadPool;
    protected ProgressDialogFragment mProgressDialog;

    protected int pageNumber = 1;
    protected final static int pageCount = 20;

//    protected MessageBar mMessageBar = null;

    private View viewTip ;


    //this, getContext(), getApplicationgContext()；希望能帮到你
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mApp = (MainApplication) mContext.getApplication();
        mThreadPool = mApp.getThreadPool();
        viewTip = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    /*public void showErrorMsgInfo(String errorContent){
        if (null == mMessageBar){
            mMessageBar = new MessageBar(mContext);
        }
        mMessageBar.show(errorContent);
    }*/

    /*public void showErrorMsgInfo(String errmsg) {
        View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        TextView textView =(TextView) view.findViewById(R.id.errorText);
        textView.setText(errmsg);
        final Crouton crouton;
        crouton = Crouton.make(mContext, view);
        crouton.show();
    }*/


    public void showErrorMsgInfo(String errmsg) {
        //View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        TextView textView =(TextView) viewTip.findViewById(R.id.errorText);
        textView.setText(errmsg);
        final Crouton crouton;
        crouton = Crouton.make(mContext, viewTip);
        crouton.show();
    }

    public void checkTokenInfo() {
        try {
            final TokenInfo tokenInfo = AppPreference.getUserToken();
            if(System.currentTimeMillis() - tokenInfo.getTimeStamp() > 20*60*60*1000){
                String URL = ReqConstant.URL_BASE + "/auth/token/refresh";
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET, URL ,null , null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(HttpResponseBean httpResponseBean) {
                        super.onSuccess(httpResponseBean);
                        try{
                            hideProgressDialog();
                            JSONObject contentObject = new JSONObject(httpResponseBean.content);
                            String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                            if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
                                JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                                String newToken = dataObject.getString("newToken");
                                tokenInfo.setToken(newToken);
                                tokenInfo.setTimeStamp(System.currentTimeMillis());
                                AppPreference.saveToken(tokenInfo);
                                doAfterGetToken();
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
                        throw new IllegalStateException(errorContent);
                    }
                });
                mThreadPool.execute(asyncHttpClient);
            }else{
                doAfterGetToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsgInfo("获取token出错");
        }
    }

    public abstract void doAfterGetToken();
}
