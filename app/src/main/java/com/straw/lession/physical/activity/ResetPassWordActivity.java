package com.straw.lession.physical.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.utils.EncryptUtil;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by straw on 2016/7/26.
 */
public class ResetPassWordActivity extends ThreadToolBarBaseActivity{
    private static final String TAG = "ResetPassWordActivity";
    private EditText originPasswd;
    private EditText newPasswd;
    private EditText repnewPasswd;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.reset_password);
        getLoginAndToken();
        initToolBar(getResources().getString(R.string.profile_reset_passwd));
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {

    }

    private void initViews() {
        originPasswd = (EditText) findViewById(R.id.resetpwd_origin);
        newPasswd = (EditText) findViewById(R.id.resetpwd_new);
        repnewPasswd = (EditText) findViewById(R.id.resetpwd_repnew);
        saveBtn = (Button) findViewById(R.id.resetpwd_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkTokenInfo();
            }
        });
    }

    @Override
    public void doAfterGetToken() {
        super.doAfterGetToken();
        String oldPwd = originPasswd.getText().toString();
        String newPwd = newPasswd.getText().toString();
        String repPwd = repnewPasswd.getText().toString();
        if(!Detect.notEmpty(oldPwd)){
            Toast.makeText(this,"请输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Detect.notEmpty(newPwd)){
            Toast.makeText(this,"请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Detect.notEmpty(repPwd)){
            Toast.makeText(this,"请再输入一遍新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newPwd.equals(repPwd)){
            Toast.makeText(this,"新密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = ReqConstant.URL_BASE + "/user/password/change";
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        String encyptedOldPwd = EncryptUtil.MD5(loginInfo.getMobile() + "_" + oldPwd);
        String encyptedNewPwd = EncryptUtil.MD5(loginInfo.getMobile() + "_" + newPwd);
        params.add(new BasicNameValuePair("oldPassword",oldPwd));
        params.add(new BasicNameValuePair("newPassword",newPwd));
        showProgressDialog(getResources().getString(R.string.loading));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST,URL,params,
                tokenInfo.getToken(),new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                try{
                    hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){//登录成功
                        Toast.makeText(ResetPassWordActivity.this,"密码修改成功", Toast.LENGTH_SHORT).show();
                        MainApplication.getInstance().popCurrentActivity();
                    }else {
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
