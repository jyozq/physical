package com.straw.lession.physical.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.utils.Utils;

/**
 * Created by straw on 2016/7/5.
 */
public class MainActivity extends ThreadBaseActivity{
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        Utils.getInstance().systemBarTintUpdate(mContext);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar = (Toolbar)this.findViewById(R.id.id_tool_bar);
//        setSupportActionBar(toolbar);
        MainApplication.getInstance().addActivity(this);
    }
}
