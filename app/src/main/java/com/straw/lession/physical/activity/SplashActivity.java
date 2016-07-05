package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.straw.lession.physical.R;

public class SplashActivity extends ThreadBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        };
    };
}
