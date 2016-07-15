package com.straw.lession.physical.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.crouton.Crouton;
import com.straw.lession.physical.custom.ProgressDialogFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.ResponseParseUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by straw on 2016/7/2.
 */
public class ThreadBaseActivity extends AppCompatActivity {
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
}