package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.*;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.task.CreateDBTask;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.Teacher;

import java.util.Date;

public class SplashActivity extends ThreadBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ITask task = new CreateDBTask(this, new TaskHandler() {
            @Override
            public void onSuccess(TaskResult result) {
                handler.sendEmptyMessageDelayed(999,2000);
            }

            @Override
            public void onFailure(Throwable error, String content) {
            }

            @Override
            protected void onSelf() {
                LoginInfoVo loginInfoVo = null;
                try{
                    loginInfoVo = AppPreference.getLoginInfo();
                    Teacher teacher = DBService.getInstance(SplashActivity.this).getTeacherById(loginInfoVo.getUserId());
                    if(teacher == null){
                        AppPreference.saveLoginInfoToDB(SplashActivity.this,loginInfoVo);
                    }
                    loginInfoVo.setLastLoginTime(DateUtil.dateTimeToStr(teacher.getLast_login_time()));
                    AppPreference.saveLoginInfoWithoutDB(loginInfoVo);
                    teacher.setLast_login_time(new Date());
                    DBService.getInstance(SplashActivity.this).updateTeacherInfo(teacher);
                }catch(Exception ex){
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                    return;
                }
                if(loginInfoVo == null) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }
        });
        TaskWorker taskWorker = new TaskWorker(task);
        mThreadPool.submit(taskWorker);
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
}
