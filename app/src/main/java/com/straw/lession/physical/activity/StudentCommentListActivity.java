package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.StudentCommentListViewAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.vo.item.ClassItemInfo;
import com.straw.lession.physical.vo.item.StudentItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/26.
 */
public class StudentCommentListActivity extends ThreadToolBarBaseActivity{
    private static final String TAG = "StudentListActivity";
    private StudentCommentListViewAdapter adapter;
    private List<StudentItemInfo> infoList = new ArrayList<StudentItemInfo>();
    private Spinner spinner_class;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_student_list);
        Intent intent = getIntent();
        ClassItemInfo classItemInfo = (ClassItemInfo)intent.getSerializableExtra("classInfo");
        initToolBar();
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void initToolBar() {
        hideToolBarView();
        toolbar.findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        spinner_class = (Spinner) toolbar.findViewById(R.id.spinner_class);
        spinner_class.setVisibility(View.VISIBLE);
    }

    private void initViews() {

    }

    @Override
    public void doAfterGetToken() {

    }
}
