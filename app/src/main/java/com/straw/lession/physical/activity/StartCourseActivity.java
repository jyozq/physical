package com.straw.lession.physical.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.StudentListViewAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.vo.item.StudentInfo;
import com.zbar.lib.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/11.
 */
public class StartCourseActivity extends ThreadToolBarBaseActivity implements SwipeRefreshLayout.OnRefreshListener,StudentListViewAdapter.Callback{
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private List<StudentInfo> infoList;
    private StudentListViewAdapter studentListViewAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_start_course);
        initToolBar("");
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        infoList = new ArrayList<StudentInfo>();
        StudentInfo info = null;
        for(int i = 0; i < 4; i ++) {
            info = new StudentInfo();
            infoList.add(info);
        }
        listView = (ListView) findViewById(R.id.listview);
        studentListViewAdapter = new StudentListViewAdapter(this, infoList, this);
        listView.setAdapter(studentListViewAdapter);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void click(View v) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(StartCourseActivity.this)
                        .setMessage("你需要启动相机权限")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(StartCourseActivity.this,
                                        new String[]{Manifest.permission.CAMERA,Manifest.permission.FLASHLIGHT},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.FLASHLIGHT},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }


        } else {
            startActivity(new Intent(this, CaptureActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                startActivity(new Intent(this, CaptureActivity.class));
            } else
            {
                // Permission Denied
                Toast.makeText(StartCourseActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
