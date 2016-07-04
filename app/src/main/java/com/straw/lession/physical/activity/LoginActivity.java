package com.straw.lession.physical.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.custom.ClearEditText;
import com.straw.lession.physical.utils.Utils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by straw on 2016/7/2.
 */
public class LoginActivity extends ThreadBaseActivity{
    private final static String TAG = "LoginActivity";
    private Activity mContext = LoginActivity.this;
    private TextView forgetText;
    private ImageView closeImage;
    private Button login , reg;
    private ClearEditText userNameEdit ,passwordEdit ;
    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().systemBarTintUpdate(LoginActivity.this , R.drawable.shape_gradient );
        setContentView(R.layout.activity_login);
        MainApplication.getInstance().addActivity(this);
        initViews();
//        forgetText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String phone = userNameEdit.getText().toString();
//                Intent intent = new Intent();
//                //intent.setClass(LoginActivity.this , ForgetPasswordActivity.class);
//                intent.putExtra("phone" , phone);
//                intent.setClass(LoginActivity.this , ResetPwdActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        closeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyApplication.getInstance().popCurrentActivity();
//            }
//        });
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*String nowTime = TimeUtils.getCurrentDateStr();
//                AppPreference.saveLoginTime(mContext , nowTime ,"胡厚春" , "");
//                MyApplication.getInstance().popCurrentActivity();*/
//                EaseTui.onEvent(mContext, Utils.getDeviceId(mContext), EventAction.LOGIN, EventStatus.CLICK);
//
//                userName = userNameEdit.getText().toString();
//                password = passwordEdit.getText().toString();
//
//                String errorMsg = "请输入";
//                if (TextUtils.isEmpty(userName)){
//                    //Toast.makeText(mContext, getResources().getString(R.string.please_input_mobile), Toast.LENGTH_SHORT).show();
//                    //return ;
//                    errorMsg = errorMsg + "手机号" +", ";
//                }
//                if (TextUtils.isEmpty(password)){
//                    //Toast.makeText(mContext, getResources().getString(R.string.please_input_loginpass), Toast.LENGTH_SHORT).show();
//                    //return ;
//                    errorMsg = errorMsg + "登录密码" +", ";
//                }
//
//                if (errorMsg.length() ==3){
//                    loginRequest();
//                }else {
//                    errorMsg = errorMsg.substring( 0 , errorMsg.length() - 2 );
//                    Toast.makeText(mContext, errorMsg , Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        });
//        reg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext , BusinessRegActivity.class);
//                startActivity(intent);
//                //MyApplication.getInstance().popCurrentActivity();
//            }
//        });

    }

    private void initViews(){
        closeImage = (ImageView) findViewById(R.id.closeImage);
        login = (Button) findViewById(R.id.login);
        reg = (Button) findViewById(R.id.reg);
        forgetText = (TextView) findViewById(R.id.forget);
        userNameEdit =(ClearEditText) findViewById(R.id.userNameEdit);
        passwordEdit =(ClearEditText) findViewById(R.id.passwordEdit);
    }

//    private void loginRequest(){
//        showProgressDialog(getResources().getString(R.string.loading));
//
//        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("userName", userName  ));
//        params.add(new BasicNameValuePair("password", password  ));
//        //params.add(new BasicNameValuePair("weixinId", ""  ));
//        params.add(new BasicNameValuePair("dataInterface", ReqConstant.DATA_INTERFACE  ));
//
//        final String METHOD = "users.login";
//        final String URL = ReqConstant.URL_BASE +"?method=" + METHOD;
//
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL ,params , new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(HttpResponseBean httpResponseBean) {
//                super.onSuccess(httpResponseBean);
//                try{
//                    hideProgressDialog();
//                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
//                    boolean success = contentObject.getBoolean(ResponseParseUtils.success);
//                    if (success ){ //注册成功
//                        EaseTui.onEvent(mContext, Utils.getDeviceId(mContext), EventAction.LOGIN, EventStatus.SUCCESS);
//
//                        JSONObject dataObject = contentObject.getJSONObject(ResponseParseUtils.data);
//                        String nowTime = TimeUtils.getCurrentDateStr2();
//                        String username = dataObject.getString("name");
//                        int userid = dataObject.getInt("id");
//                        String mobile = dataObject.getString("mobile");
//                        String business_license = dataObject.getString("business_license");
//                        String merchant_no = dataObject.getString("merchant_no");
//                        String introducer_name = dataObject.getString("introducer_name");
//
//                        AppPreference.saveLoginTime(mContext , nowTime ,username , String.valueOf( userid ) ,String.valueOf(mobile) ,business_license , merchant_no ,introducer_name );
//                        MyApplication.getInstance().popCurrentActivity();
//
//                    }else {//注册失败
//                        EaseTui.onEvent(mContext, Utils.getDeviceId(mContext), EventAction.LOGIN, EventStatus.FAIL);
//                        String errorMessage = contentObject.getString(ResponseParseUtils.message);
//                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
//                    }
//                }catch(JSONException e){
//                    EaseTui.onEvent(mContext, Utils.getDeviceId(mContext), EventAction.LOGIN, EventStatus.FAIL);
//                    hideProgressDialog();
//                    showErrorMsgInfo(e.toString());
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Throwable error, String content) {
//                super.onFailure(error, content);
//                EaseTui.onEvent(mContext, Utils.getDeviceId(mContext), EventAction.LOGIN, EventStatus.FAIL);
//                hideProgressDialog();
//                String errorContent = Utils.parseErrorMessage(mContext, content);
//                showErrorMsgInfo(errorContent);
//                Log.e("TAG", content);
//            }
//        });
//        mThreadPool.execute(asyncHttpClient);
//    }
}
