package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadBaseActivity;
import com.straw.lession.physical.async.*;
import com.straw.lession.physical.task.CreateDBTask;

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
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                SplashActivity.this.finish();
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
