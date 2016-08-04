package com.straw.lession.physical.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.utils.Utils;


/**
 * Created by Administrator on 2015/12/9.
 */
public  abstract class ToolBarActivity extends AppCompatActivity implements View.OnClickListener {
    protected Activity mContext = ToolBarActivity.this;
    private ToolBarHelper mToolBarHelper ;
    protected Toolbar toolbar ;

    private ImageButton btn_back;
    private ImageButton btn_add_course;
    private TextView textView;
    private Spinner spinner_school;
    private Spinner spinner_class;
    private ImageButton btn_sync;
    private ImageButton btn_history;
    private ImageButton btn_add_tempcourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        //systemBarTintUpdate();
        Utils.getInstance().systemBarTintUpdate(mContext);
        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
        toolbar = mToolBarHelper.getToolBar() ;
        setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar) ;
        btn_back = (ImageButton) toolbar.findViewById(R.id.btn_back);
        btn_add_course = (ImageButton) toolbar.findViewById(R.id.btn_add_course);
        btn_add_tempcourse = (ImageButton) toolbar.findViewById(R.id.btn_add_tempcourse);
        textView = (TextView) toolbar.findViewById(R.id.textView);
        spinner_school = (Spinner) toolbar.findViewById(R.id.spinner_school);
        spinner_class = (Spinner) toolbar.findViewById(R.id.spinner_class);
        btn_sync = (ImageButton) toolbar.findViewById(R.id.btn_sync);
        btn_history = (ImageButton) toolbar.findViewById(R.id.btn_history);
    }

    public void hideToolBarView(){
        btn_back.setVisibility(View.GONE);
        btn_add_course.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        spinner_school.setVisibility(View.GONE);
        spinner_class.setVisibility(View.GONE);
        btn_sync.setVisibility(View.GONE);
        btn_history.setVisibility(View.GONE);
        btn_add_tempcourse.setVisibility(View.GONE);
    }

    public void displayBackBtn(){
        btn_back.setOnClickListener(this);
        btn_back.setVisibility(View.VISIBLE);
    }

    public void initToolBar(String titleText){
        hideToolBarView();
        textView.setText( titleText );
        btn_back.setOnClickListener(this);
        textView.setVisibility(View.VISIBLE);
        btn_back.setVisibility(View.VISIBLE);
    }

    public void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                leftEvent();
                break;
            case R.id.btn_sync:
                rightEvent();
                break;
        }
    }

    protected void rightEvent(){
    }

    protected void rightButtonEvent(){
    }

    protected void leftEvent(){
        //finish();
        MainApplication.getInstance().popCurrentActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         */
        //MobCreditEase.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         */
        //MobCreditEase.onPause(this);
    }
}
