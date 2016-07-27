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
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.StudentListViewAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.Weekday;
import com.straw.lession.physical.db.DaoSession;
import com.straw.lession.physical.db.StudentDao;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.Student;
import com.straw.lession.physical.vo.item.CourseItemInfo;
import com.straw.lession.physical.vo.item.StudentItemInfo;
import com.zbar.lib.CaptureActivity;

import org.greenrobot.greendao.query.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/11.
 */
public class StartCourseActivity extends ThreadToolBarBaseActivity implements SwipeRefreshLayout.OnRefreshListener,StudentListViewAdapter.Callback{
    private static final String TAG = "StartCourseActivity";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private List<StudentItemInfo> infoList = new ArrayList<StudentItemInfo>();
    private StudentListViewAdapter studentListViewAdapter;
    private CourseItemInfo course;
    private LoginInfoVo loginInfo;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_start_course);
        Intent intent = getIntent();
        course = (CourseItemInfo)intent.getSerializableExtra("course");
        initToolBar(course.getName());
        MainApplication.getInstance().addActivity(this);
        initViews();
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

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);

        TextView startcourse_className = (TextView)findViewById(R.id.startcourse_className);
        TextView startcourse_time = (TextView)findViewById(R.id.startcourse_time);
        TextView startcourse_location = (TextView)findViewById(R.id.startcourse_location);
        startcourse_className.setText(course.getClassName());
        startcourse_time.setText(Weekday.getName(course.getWeekDay()) + "   第"+course.getSeq()+"节");
        startcourse_location.setText(course.getLocation() + "   " + course.getType());

        getStudentsInfo();
        listView = (ListView) findViewById(R.id.listview);
        studentListViewAdapter = new StudentListViewAdapter(this, infoList, this);
        listView.setAdapter(studentListViewAdapter);
    }

    private void getStudentsInfo() {
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            showErrorMsgInfo(e.toString());
            return;
        }
        DaoSession session = MainApplication.getInstance().getDaoSession(this);
        StudentDao studentDao = session.getStudentDao();
        Query<Student> query = studentDao.queryBuilder()
                            .where(StudentDao.Properties.ClassId.eq(course.getClassId()),
                                    StudentDao.Properties.LoginId.eq(loginInfo.getTeacherId()))
                            .build();
        List<Student> students = query.list();
        Student student = null;
        for(int i = 0; i < students.size(); i ++){
            student = students.get(i);
            infoList.add(toItem(student));
        }

    }

    private StudentItemInfo toItem(Student student) {
        StudentItemInfo studentItemInfo = new StudentItemInfo();
        studentItemInfo.setCode(student.getCode());
        studentItemInfo.setName(student.getName());
        studentItemInfo.setGender(student.getGender());
        studentItemInfo.setId(student.getId());
        studentItemInfo.setStudentIdR(student.getStudentIdR());
        return studentItemInfo;
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
