package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.item.StudentItemInfo;

import java.io.IOException;

/**
 * Created by straw on 2016/7/27.
 */
public class StudentCommentActivity extends ThreadToolBarBaseActivity{
    private StudentItemInfo studentItemInfo;
    private LoginInfoVo loginInfoVo;
    private TokenInfo tokenInfo;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_comment_student);
        Intent intent = getIntent();
        studentItemInfo = (StudentItemInfo) intent.getSerializableExtra("studentInfo");
        initToolBar(studentItemInfo.getName());
        MainApplication.getInstance().addActivity(this);
        try {
            loginInfoVo = AppPreference.getLoginInfo();
            tokenInfo = AppPreference.getUserToken();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"获取登录信息出错",Toast.LENGTH_SHORT).show();
            return;
        }
        initViews();
    }

    private void initViews() {

    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {

    }

    @Override
    public void doAfterGetToken() {

    }
}
