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
import com.google.gson.Gson;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.custom.ClearEditText;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.TimeUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by straw on 2016/7/2.
 */
public class LoginActivity extends ThreadBaseActivity {
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
//        Utils.getInstance().systemBarTintUpdate(LoginActivity.this , R.drawable.shape_gradient );
        setContentView(R.layout.activity_login);
        MainApplication.getInstance().addActivity(this);
        initViews();
//        login.setOnClickListener(new View.OnClickListener() {
//                                     public void onClick(View v) {
//                                         Intent intent = new Intent();
//                                         intent.setClass(mContext , MainActivity.class);
//                                         MainApplication.getInstance().popCurrentActivity();
//                                         startActivity(intent);
//                                     }
//                                 });

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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameEdit.getText().toString();
                password = passwordEdit.getText().toString();

                String errorMsg = "请输入";
                if (TextUtils.isEmpty(userName)){
                    //Toast.makeText(mContext, getResources().getString(R.string.please_input_mobile), Toast.LENGTH_SHORT).show();
                    //return ;
                    errorMsg = errorMsg + "手机号" +", ";
                }
                if (TextUtils.isEmpty(password)){
                    //Toast.makeText(mContext, getResources().getString(R.string.please_input_loginpass), Toast.LENGTH_SHORT).show();
                    //return ;
                    errorMsg = errorMsg + "登录密码" +", ";
                }

                if (errorMsg.length() ==3){
                    loginRequest();
                }else {
                    errorMsg = errorMsg.substring( 0 , errorMsg.length() - 2 );
                    Toast.makeText(mContext, errorMsg , Toast.LENGTH_LONG).show();
                }


            }
        });
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

    @Override
    protected void doAfterGetToken() {

    }

    @Override
    protected void loadDataFromService() {

    }

    @Override
    protected void loadDataFromLocal() {

    }

    private void initViews(){
        login = (Button) findViewById(R.id.login);
        reg = (Button) findViewById(R.id.reg);
        forgetText = (TextView) findViewById(R.id.forget);
        userNameEdit =(ClearEditText) findViewById(R.id.userNameEdit);
        passwordEdit =(ClearEditText) findViewById(R.id.passwordEdit);
    }

    private void loginRequest(){
        showProgressDialog(getResources().getString(R.string.loading));

        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("mobile", userName  ));
        params.add(new BasicNameValuePair("password", password  ));

        final String URL = ReqConstant.URL_BASE + "/auth/teacher/login";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL ,params , null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){//登录成功

                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                        String userToken = dataObject.getString(ParamConstant.USER_TOKEN);
                        AppPreference.saveToken(new TokenInfo(userToken));
                        JSONObject userObject = dataObject.getJSONObject(ParamConstant.USER_INFO);
                        String nowTime = TimeUtils.getCurrentDateStr2();
                        Gson gson = new Gson();
                        LoginInfoVo loginInfo = gson.fromJson(userObject.toString(), LoginInfoVo.class);
                        loginInfo.setNowTime(nowTime);
                        AppPreference.saveLoginInfo(LoginActivity.this,loginInfo);
                        MainApplication.getInstance().popCurrentActivity();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {//登录失败
                        String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
                    }
                }catch(Exception e){
                    hideProgressDialog();
                    showErrorMsgInfo(e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                hideProgressDialog();
                String errorContent = Utils.parseErrorMessage(mContext, content);
                showErrorMsgInfo(errorContent);
                Log.e(TAG, content);
            }
        });
        mThreadPool.execute(asyncHttpClient);
    }
}
