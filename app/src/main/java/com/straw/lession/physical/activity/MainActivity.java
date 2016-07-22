package com.straw.lession.physical.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadBaseActivity;
import com.straw.lession.physical.adapter.SchoolSpinnerAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.async.TaskWorker;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.fragment.ClassFragment;
import com.straw.lession.physical.fragment.CourseFragment;
import com.straw.lession.physical.fragment.ProfileFragment;
import com.straw.lession.physical.fragment.TodayFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.task.AddAllDataToDBTask;
import com.straw.lession.physical.task.AddDataToDBTask;
import com.straw.lession.physical.task.DeleteAllDataTask;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.LoginInfo;
import com.straw.lession.physical.vo.TokenInfo;

import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.db.Institute;
import com.straw.lession.physical.vo.db.Student;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/5.
 */
public class MainActivity extends ThreadBaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int CURRENT_PAGE_TODAY = 0;
    private static final int CURRENT_PAGE_SCHEDULE = 1;
    private static final int CURRENT_PAGE_PROFILE = 2;
    private static final int CURRENT_PAGE_CLASS = 3;
    private int currentPage;

    private LinearLayout ll_today;
    private LinearLayout ll_course;
    private LinearLayout ll_profile;
    private LinearLayout ll_class;

    private ImageView iv_today;
    private ImageView iv_course;
    private ImageView iv_profile;
    private ImageView iv_class;

    private TextView tv_today;
    private TextView tv_course;
    private TextView tv_profile;
    private TextView tv_class;

    private Toolbar toolbar;
    private ImageButton btn_back ;
    private ImageButton btn_add_course;
    private TextView textView ;
    private ImageButton btn_sync;

    private TodayFragment todayFragment;
    private CourseFragment courseFragment;
    private ProfileFragment profileFragment;
    private ClassFragment classFragment;

    private Spinner spinner;
    private LoginInfo loginInfo;
    private TokenInfo tokenInfo;
    private List<Institute> institutes = new ArrayList<>();
    private List<CourseDefine> courseDefines = new ArrayList<>();
    private List<ClassInfo> classes = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
        }
        if(loginInfo == null){
            return;
        }
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)this.findViewById(R.id.id_tool_bar);
        setSupportActionBar(toolbar);
        MainApplication.getInstance().addActivity(this);
        getDataByNetSate();
        // 初始化控件
        initView();
    }

    private void goOnLoad(){
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(CURRENT_PAGE_TODAY);
    }

    @Override
    protected void loadDataFromService() {
        showProgressDialog(getResources().getString(R.string.loading));
        try {
            tokenInfo = AppPreference.getUserToken();
            checkTokenInfo(tokenInfo);
        } catch (IOException e) {
            Log.e(TAG,"获取token出错",e);
            e.printStackTrace();
            return;
        }

        //有网络的情况下，可重新获取所有的数据，需要先删除之前的旧数据
        deleteAllData();
    }

    private void deleteAllData() {
        showProgressDialog(getResources().getString(R.string.loading));
        ITask deleteAllDataTask = new DeleteAllDataTask(this, new TaskHandler(){
            @Override
            public void onSuccess(TaskResult result) {
                hideProgressDialog();
                getInstituteCoursedefineClassStudentInfo();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                hideProgressDialog();
                String errorContent = Utils.parseErrorMessage(mContext, content);
                showErrorMsgInfo(errorContent);
                Log.e(TAG, content);
            }

            @Override
            protected void onSelf() {

            }
        },loginInfo);
        mThreadPool.submit(new TaskWorker(deleteAllDataTask));
    }

    private void getInstituteCoursedefineClassStudentInfo() {
        showProgressDialog(getResources().getString(R.string.loading));
        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        String URL = ReqConstant.URL_BASE + "/course/define/full";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET, URL ,params , tokenInfo.getToken(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){ //登录成功
                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                        assembleData(dataObject);
                        addDataToDB();
                    }else {//登录失败
                        String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
                    }
                }catch(Exception e){
                    hideProgressDialog();
                    showErrorMsgInfo(e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                hideProgressDialog();
                String errorContent = Utils.parseErrorMessage(mContext, content);
                showErrorMsgInfo(errorContent);
                Log.e(TAG, content);
            }
        });
        mThreadPool.execute(asyncHttpClient);
    }

    private void addDataToDB() {
        showProgressDialog(getResources().getString(R.string.loading));
        ITask addAllDataToDBTask = new AddAllDataToDBTask(this, new TaskHandler() {
            @Override
            public void onSuccess(TaskResult result) {
                hideProgressDialog();
                goOnLoad();
            }

            @Override
            public void onFailure(Throwable e, String content) {
                hideProgressDialog();
                String errorContent = Utils.parseErrorMessage(mContext, content);
                showErrorMsgInfo(errorContent);
                Log.e(TAG, content);
            }

            @Override
            protected void onSelf() {

            }
        },institutes,classes,students,courseDefines);
        TaskWorker taskWorker = new TaskWorker(addAllDataToDBTask);
        mThreadPool.submit(taskWorker);
    }

    private void assembleData(JSONObject dataObject) throws JSONException {
        JSONArray instituteArr = dataObject.getJSONArray("institutes");
        for(int i = 0; i < instituteArr.length(); i ++){
            JSONObject instituteObj = instituteArr.getJSONObject(i);
            Institute institute = new Institute();
            assembleInstitute(institute, instituteObj);
            institutes.add(institute);
        }
    }

    private void assembleInstitute(Institute institute, JSONObject instituteObj) throws JSONException {
        institute.setInstituteIdR(instituteObj.getLong("instituteId"));
        institute.setName(instituteObj.getString("instituteName"));
        institute.setLoginId(loginInfo.getTeacherId());

        try{
            JSONArray classArr = instituteObj.getJSONArray("classes");
            for(int i = 0; i < classArr.length(); i ++){
                JSONObject classObj = classArr.getJSONObject(i);
                ClassInfo classInfo = new ClassInfo();
                assembleClass(classInfo, classObj);
                classInfo.setInstituteIdR(institute.getInstituteIdR());
                classes.add(classInfo);
            }
        }catch(Exception e){
            Log.e(TAG, "", e);
        }

        try {
            JSONArray courseArr = instituteObj.getJSONArray("courseDefines");
            for (int i = 0; i < courseArr.length(); i++) {
                JSONObject courseObj = courseArr.getJSONObject(i);
                CourseDefine courseDefine = new CourseDefine();
                assembleCourseDefine(courseDefine, courseObj);
                courseDefines.add(courseDefine);
            }
        }catch(Exception e){
            Log.e(TAG,"",e);
        }
    }

    private void assembleCourseDefine(CourseDefine courseDefine, JSONObject courseObj) throws JSONException {
        courseDefine.setClassIdR(courseObj.getLong("classId"));
        courseDefine.setCode(courseObj.getString("courseCode"));
        courseDefine.setCourseDefineIdR(courseObj.getLong("courseDefineId"));
        courseDefine.setLocation(courseObj.getString("courseLocation"));
        courseDefine.setName(courseObj.getString("courseName"));
        courseDefine.setSeq(courseObj.getInt("courseSeq"));
        courseDefine.setType(courseObj.getString("courseType"));
        courseDefine.setInstituteIdR(courseObj.getLong("instituteId"));
        courseDefine.setUseOnce(courseObj.getInt("useOnce"));
        courseDefine.setWeekDay(courseObj.getInt("weekday"));
        courseDefine.setLoginId(loginInfo.getTeacherId());
    }

    private void assembleClass(ClassInfo classInfo, JSONObject classObj) throws JSONException {
        classInfo.setCode(classObj.getString("classCode"));
        classInfo.setClassIdR(classObj.getLong("classId"));
        classInfo.setName(classObj.getString("className"));
        classInfo.setType(classObj.getInt("classType"));
        classInfo.setTotalNum(classObj.getInt("totalNum"));
        classInfo.setLoginId(loginInfo.getTeacherId());

        try {
            JSONArray studentArr = classObj.getJSONArray("students");
            for (int i = 0; i < studentArr.length(); i++) {
                JSONObject studentObj = studentArr.getJSONObject(i);
                Student student = new Student();
                assembleStudent(student, studentObj);
                student.setClassIdR(classInfo.getClassIdR());
                students.add(student);
            }
        }catch(Exception e){
            Log.e(TAG,"",e);
        }
    }

    private void assembleStudent(Student student, JSONObject studentObj) throws JSONException {
        student.setName(studentObj.getString("studentName"));
        student.setCode(studentObj.getString("studentCode"));
        student.setBirthday(DateUtil.formatStrToDate(studentObj.getString("birthday")));
        student.setGender(studentObj.getInt("gender"));
        student.setStudentIdR(studentObj.getLong("studentId"));
        student.setLoginId(loginInfo.getTeacherId());
    }

    @Override
    protected void loadDataFromLocal() {

    }

    private void initFragment(int index) {
        currentPage = index;
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case CURRENT_PAGE_TODAY:
                textView.setText(R.string.toolbar_today);
                btn_add_course.setVisibility(View.VISIBLE);
                btn_sync.setVisibility(View.VISIBLE);
                if (todayFragment == null) {
                    todayFragment = new TodayFragment();
                    transaction.add(R.id.fl_content, todayFragment);
                } else {
                    transaction.show(todayFragment);
                }
                break;
            case CURRENT_PAGE_SCHEDULE:
                textView.setText(R.string.toolbar_course);
                btn_add_course.setVisibility(View.VISIBLE);
                btn_sync.setVisibility(View.VISIBLE);
                if (courseFragment == null) {
                    courseFragment = new CourseFragment();
                    transaction.add(R.id.fl_content, courseFragment);
                } else {
                    transaction.show(courseFragment);
                }
                break;
            case CURRENT_PAGE_PROFILE:
                textView.setText(R.string.toolbar_profile);
                btn_add_course.setVisibility(View.GONE);
                btn_sync.setVisibility(View.GONE);
                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
                    transaction.add(R.id.fl_content, profileFragment);
                } else {
                    transaction.show(profileFragment);
                }
                break;
            case CURRENT_PAGE_CLASS:
                textView.setText(R.string.toolbar_class);
                btn_add_course.setVisibility(View.GONE);
                btn_sync.setVisibility(View.GONE);
                if (classFragment == null) {
                    classFragment = new ClassFragment();
                    transaction.add(R.id.fl_content, classFragment);
                } else {
                    transaction.show(classFragment);
                }
                break;
            default:
                break;
        }

        // 提交事务
        transaction.commit();
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (todayFragment != null) {
            transaction.hide(todayFragment);
        }
        if (courseFragment != null) {
            transaction.hide(courseFragment);
        }
        if (profileFragment != null) {
            transaction.hide(profileFragment);
        }
        if (classFragment != null) {
            transaction.hide(classFragment);
        }
    }

    private void initEvent() {
        // 设置按钮监听
        ll_course.setOnClickListener(this);
        ll_profile.setOnClickListener(this);
        ll_today.setOnClickListener(this);
        ll_class.setOnClickListener(this);
        btn_add_course.setOnClickListener(this);
        btn_sync.setOnClickListener(this);

        final List<LoginInfo.Institute> institutes = loginInfo.getInstitutes();
        SchoolSpinnerAdapter schoolSpinnerAdapter = new SchoolSpinnerAdapter(this, spinner, institutes);
        schoolSpinnerAdapter.setDropDownViewResource(R.layout.school_item_spinner_dropdown);
        spinner.setAdapter(schoolSpinnerAdapter);
        spinner.setDropDownVerticalOffset(15);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Toast.makeText(MainActivity.this, "你点击的是:"+institutes.get(pos).getInsName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initView() {
        // 底部菜单4个Linearlayout
        this.ll_today = (LinearLayout) findViewById(R.id.id_today);
        this.ll_course = (LinearLayout) findViewById(R.id.id_lesson);
        this.ll_profile = (LinearLayout) findViewById(R.id.id_profile);
        this.ll_class = (LinearLayout) findViewById(R.id.id_class);

        // 底部菜单4个ImageView
        this.iv_today = (ImageView) findViewById(R.id.id_today_img);
        this.iv_course = (ImageView) findViewById(R.id.id_lesson_img);
        this.iv_profile = (ImageView) findViewById(R.id.id_profile_img);
        this.iv_class = (ImageView) findViewById(R.id.id_class_img);

        // 底部菜单4个菜单标题
        this.tv_today = (TextView) findViewById(R.id.id_today_txt);
        this.tv_course = (TextView) findViewById(R.id.id_lesson_txt);
        this.tv_profile = (TextView) findViewById(R.id.id_profile_txt);
        this.tv_class = (TextView) findViewById(R.id.id_class_txt);

        // 顶部工具栏
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        textView = (TextView) findViewById(R.id.textView);
        btn_add_course = (ImageButton) findViewById(R.id.btn_add_course);
        btn_sync = (ImageButton) findViewById(R.id.btn_sync);
        btn_back.setVisibility(View.GONE);
        btn_add_course.setVisibility(View.VISIBLE);
        spinner = (Spinner) findViewById(R.id.spinner_school);
    }

    @Override
    public void onClick(View v) {
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBotton();
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.id_today:
                iv_today.setImageResource(R.mipmap.bottom_toolbar_icon_today);
                tv_today.setTextColor(getResources().getColor(R.color.toolbar_btn_pressed));
                initFragment(CURRENT_PAGE_TODAY);
                break;
            case R.id.id_lesson:
                iv_course.setImageResource(R.mipmap.bottom_toolbar_icon_kc);
                tv_course.setTextColor(getResources().getColor(R.color.toolbar_btn_pressed));
                initFragment(CURRENT_PAGE_SCHEDULE);
                break;
            case R.id.id_profile:
                iv_profile.setImageResource(R.mipmap.bottom_toolbar_icon_wo);
                tv_profile.setTextColor(getResources().getColor(R.color.toolbar_btn_pressed));
                initFragment(CURRENT_PAGE_PROFILE);
                break;
            case R.id.id_class:
                iv_class.setImageResource(R.mipmap.bottom_toolbar_icon_class);
                tv_class.setTextColor(getResources().getColor(R.color.toolbar_btn_pressed));
                initFragment(CURRENT_PAGE_CLASS);
                break;
            case R.id.btn_add_course:
                break;
            case R.id.btn_sync:
                break;
            default:
                break;
        }
    }

    private void restartBotton() {
        // ImageView置为灰色
        iv_today.setImageResource(R.mipmap.bottom_toolbar_icon_today_gray);
        iv_course.setImageResource(R.mipmap.bottom_toolbar_icon_kc_gray);
        iv_profile.setImageResource(R.mipmap.bottom_toolbar_icon_wo_gray);
        iv_class.setImageResource(R.mipmap.bottom_toolbar_icon_class_gray);
        // TextView置为白色
        tv_today.setTextColor(getResources().getColor(R.color.toolbar_btn_gray));
        tv_course.setTextColor(getResources().getColor(R.color.toolbar_btn_gray));
        tv_profile.setTextColor(getResources().getColor(R.color.toolbar_btn_gray));
        tv_class.setTextColor(getResources().getColor(R.color.toolbar_btn_gray));
    }

    /**************** 以下实现两次退出逻辑 *********************/
    boolean isExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), R.string.system_exit, Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }

    };
    /**************** 以上实现两次退出逻辑 *********************/
}