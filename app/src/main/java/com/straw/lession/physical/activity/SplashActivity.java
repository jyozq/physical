package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadBaseActivity;
import com.straw.lession.physical.async.CreateDBTask;
import com.straw.lession.physical.async.CreateDBTaskHandler;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskWorker;
import com.straw.lession.physical.db.DBManager;

public class SplashActivity extends ThreadBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ITask task = new CreateDBTask(this,new CreateDBTaskHandler(mHandler));
        TaskWorker taskWorker = new TaskWorker(task);
        mThreadPool.submit(taskWorker);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        };
    };
}
