package com.straw.lession.physical.activity;

import android.os.Bundle;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;

/**
 * Created by straw on 2016/7/13.
 */
public class CourseActivity extends ThreadToolBarBaseActivity{
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_course);
        initToolBar("");
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    @Override
    public void doAfterGetToken() {

    }

    private void initViews() {
    }
}
