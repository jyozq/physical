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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.StudentListViewAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.CourseStatus;
import com.straw.lession.physical.constant.Weekday;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.db.Student;
import com.straw.lession.physical.vo.item.CourseItemInfo;
import com.straw.lession.physical.vo.item.StudentItemInfo;
import com.zbar.lib.CaptureActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    private CourseItemInfo courseItemVo;
    private LoginInfoVo loginInfo;
    private Button btn_do_start_course,btn_end_course;
    private long courseId;
    private MyListener listener = new MyListener();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_start_course);
        Intent intent = getIntent();
        courseItemVo = (CourseItemInfo)intent.getSerializableExtra("course");
        initToolBar(courseItemVo.getName());
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            showErrorMsgInfo(e.toString());
            return;
        }
        refreshStatus();
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

        startcourse_className.setText(courseItemVo.getClassName());
        startcourse_time.setText(Weekday.getName(courseItemVo.getWeekDay()) + "   第"+courseItemVo.getSeq()+"节");
        startcourse_location.setText(courseItemVo.getLocation() + "   " + courseItemVo.getType());

        btn_do_start_course = (Button) findViewById(R.id.btn_do_start_course);
        btn_end_course = (Button) findViewById(R.id.btn_end_course);
        getStudentsInfo();
        refreshStatus();
        listView = (ListView) findViewById(R.id.listview);
        studentListViewAdapter = new StudentListViewAdapter(this, infoList, this);
        listView.setAdapter(studentListViewAdapter);
    }

    private void refreshStatus() {
        List<Course> courses = DBService.getInstance(this).getCourseExceptUnstarted(loginInfo.getUserId());
        Integer status = null;
        for(Course course:courses){
            if(course.getCourseDefineIdR() == courseItemVo.getCourseDefineId()){
                status = course.getStatus();
                break;
            }
        }
        if(status == null){
            btn_do_start_course.setOnClickListener(listener);
            btn_end_course.setOnClickListener(null);
        }else if(status == CourseStatus.STARTED.getValue()){
            btn_end_course.setOnClickListener(listener);
            btn_do_start_course.setOnClickListener(null);
            btn_do_start_course.setText(CourseStatus.getName(status));
        }else if(status == CourseStatus.OVER.getValue()){
            btn_end_course.setOnClickListener(null);
            btn_do_start_course.setOnClickListener(null);
            btn_do_start_course.setText(CourseStatus.getName(status));
        }
    }

    private void endCourse() {
        Course course = DBService.getInstance(this).findCourseById(courseId);
        course.setStatus(CourseStatus.OVER.getValue());
        course.setEndTime(new Date());
        DBService.getInstance(this).updateCourse(course);
        btn_end_course.setOnClickListener(null);
        btn_end_course.setVisibility(View.INVISIBLE);
        btn_do_start_course.setText(CourseStatus.OVER.getText());
    }

    private void startCourse() {
        Course course = new Course();
        CourseDefine courseDefine = DBService.getInstance(this).findCourseDefineById(courseItemVo.getCourseDefineId());
        course.setDate(new Date());
        course.setWeekday(courseDefine.getWeekDay());
        course.setCourseDefineIdR(courseDefine.getCourseDefineIdR());
        course.setInstituteIdR(courseDefine.getInstituteIdR());
        course.setTeacherIdR(courseDefine.getTeacherIdR());
        course.setUseOnce(courseDefine.getUseOnce());
        course.setStatus(CourseStatus.STARTED.getValue());
        course.setStartTime(new Date());
        course.setIsUploaded(false);
        DBService.getInstance(this).addCourse(course);
        courseId = course.getId();
        btn_do_start_course.setOnClickListener(null);
        btn_do_start_course.setText(CourseStatus.STARTED.getText());
    }

    private void getStudentsInfo() {
        List<Student> students = DBService.getInstance(this).getStudentByClass(courseItemVo.getClassId());
        for(Student student : students){
            infoList.add(toItem(student));
        }
    }

    private StudentItemInfo toItem(Student student) {
        StudentItemInfo studentItemInfo = new StudentItemInfo();
        studentItemInfo.setCode(student.getCode());
        studentItemInfo.setName(student.getName());
        studentItemInfo.setGender(student.getGender());
        studentItemInfo.setStudentIdR(student.getStudentIdR());
        studentItemInfo.setCourseDefindIdR(courseItemVo.getCourseDefineId());
        return studentItemInfo;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void click(View v) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
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
            StudentItemInfo studentItemInfo = infoList.get((Integer) v.getTag());
            Intent intent = new Intent(this, CaptureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("student", studentItemInfo);
            infoList.remove(studentItemInfo);
            bundle.putSerializable("students",(Serializable)infoList);
            intent.putExtras(bundle);
            startActivity(intent);
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

    public class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_do_start_course:
                    startCourse();
                    break;
                case R.id.btn_end_course:
                    endCourse();
                    break;
            }
        }
    }
}
