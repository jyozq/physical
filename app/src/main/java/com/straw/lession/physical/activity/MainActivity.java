package com.straw.lession.physical.activity;

import android.content.Intent;
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

import com.alibaba.fastjson.JSON;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadBaseActivity;
import com.straw.lession.physical.adapter.SchoolSpinnerAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.async.TaskWorker;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.ClassFragment;
import com.straw.lession.physical.fragment.CourseFragment;
import com.straw.lession.physical.fragment.ProfileFragment;
import com.straw.lession.physical.fragment.TodayFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.task.AddAllDataToDBTask;
import com.straw.lession.physical.utils.*;
import com.straw.lession.physical.vo.*;

import com.straw.lession.physical.vo.db.*;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

import org.apache.http.message.BasicNameValuePair;
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
    private ImageButton btn_history;

    private TodayFragment todayFragment;
    private CourseFragment courseFragment;
    private ProfileFragment profileFragment;
    private ClassFragment classFragment;

    private Spinner spinner;
    private LoginInfoVo loginInfo;
    private TokenInfo tokenInfo;
    private List<Institute> institutes = new ArrayList<>();
    private List<CourseDefine> courseDefines = new ArrayList<>();
    private List<ClassInfo> classes = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private List<TeacherInstitute> teacherInstitutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            Toast.makeText(MainActivity.this, "获取登录信息出错", Toast.LENGTH_SHORT).show();
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
        btn_history = (ImageButton) findViewById(R.id.btn_history);
        btn_back.setVisibility(View.GONE);
        btn_add_course.setVisibility(View.VISIBLE);
        btn_history.setVisibility(View.GONE);
        spinner = (Spinner) findViewById(R.id.spinner_school);
    }

    @Override
    protected void doAfterGetToken() {
        //获取数据并更新本地库
        getInstituteCoursedefineClassStudentInfo();
    }

    private void goOnLoad(){
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(CURRENT_PAGE_TODAY);
    }

    @Override
    protected void loadDataFromService() {
        try {
            tokenInfo = AppPreference.getUserToken();
            checkTokenInfo(tokenInfo);
        } catch (IOException e) {
            Log.e(TAG,"获取token出错",e);
            e.printStackTrace();
            showErrorMsgInfo("获取token出错");
            return;
        }
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
                        List<InstituteVo> instituteVos =
                            JSON.parseArray(dataObject.getJSONArray("institutes").toString(), InstituteVo.class);
                        refineDBData(instituteVos);
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

    private void refineDBData(List<InstituteVo> instituteVos) {
        showProgressDialog(getResources().getString(R.string.loading));
        ITask addAllDataToDBTask = new AddAllDataToDBTask(this, new TaskHandler() {
            @Override
            public void onSuccess(TaskResult result) {
                hideProgressDialog();
                institutes = DBService.getInstance(MainActivity.this).getInsituteDataByTeacher(loginInfo.getUserId());
                initInstituteSpinner();
                goOnLoad();
//                getAllClassInfoByInstitute();
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
        },instituteVos);
        TaskWorker taskWorker = new TaskWorker(addAllDataToDBTask);
        mThreadPool.submit(taskWorker);
    }

//    private void getAllClassInfoByInstitute() {
//        long instituteIdR = loginInfo.getCurrentInstituteIdR();
//        String URL = ReqConstant.URL_BASE + "/class/list";
//        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("instituteId", String.valueOf(instituteIdR)));
//        showProgressDialog(getResources().getString(R.string.loading));
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET, URL,params,
//                                                tokenInfo.getToken(),new AsyncHttpResponseHandler(){
//            @Override
//            public void onSuccess(HttpResponseBean httpResponseBean) {
//                super.onSuccess(httpResponseBean);
//                try{
//                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
//                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
//                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
//                        JSONObject dataObj = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
//                        JSONArray classesArray = dataObj.getJSONArray("classes");
//                        List<ClassInfoVo> classVos =
//                                JSON.parseArray(classesArray.toString(), ClassInfoVo.class);
//                        DbService.getInstance(MainActivity.this).refineClassInfoData(classVos, instituteVO.getInstituteId());
//                        hideProgressDialog();
//                        goOnLoad();
//                    }else {
//                        String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
//                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
//                    }
//                }catch(Exception e){
//                    hideProgressDialog();
//                    showErrorMsgInfo(e.toString());
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable error, String content) {
//                super.onFailure(error, content);
//                hideProgressDialog();
//                String errorContent = Utils.parseErrorMessage(mContext, content);
//                showErrorMsgInfo(errorContent);
//                Log.e(TAG, content);
//            }
//        });
//        mThreadPool.execute(asyncHttpClient);
//    }

    private void assembleInstituteData(List<InstituteVo> instituteVos) throws JSONException {
        InstituteVo instituteVo = null;
        for(int i = 0; i < instituteVos.size(); i ++){
            instituteVo = instituteVos.get(i);
            Institute institute = new Institute();
            try {
                assembleInstitute(institute, instituteVo);
                institutes.add(institute);
            }catch(Exception ex){
                Log.e(TAG, "", ex);
            }
        }
    }

    private void assembleInstitute(Institute institute, InstituteVo instituteVo) throws JSONException {
        institute.setInstituteIdR(instituteVo.getInstituteId());
        institute.setName(instituteVo.getInstituteName());
        assembleTeacherInstitute(institute);
        try{
            List<ClassInfoVo> classInfoVos = instituteVo.getClasses();
            ClassInfoVo classInfoVo = null;
            for(int i = 0; i < classInfoVos.size(); i ++){
                classInfoVo = classInfoVos.get(i);
                ClassInfo classInfo = new ClassInfo();
                classInfo.setInstituteIdR(institute.getInstituteIdR());
                try{
                    assembleClass(classInfo, classInfoVo);
                }catch(Exception ex){
                    Log.e(TAG, "", ex);
                }
                classes.add(classInfo);
            }
        }catch(Exception e){
            Log.e(TAG, "", e);
        }

        try {
            List<CourseDefineVo> courseDefineVos
                        = instituteVo.getCourseDefines();
            CourseDefineVo courseDefineVo = null;
            for (int i = 0; i < courseDefineVos.size(); i++) {
                courseDefineVo = courseDefineVos.get(i);
                CourseDefine courseDefine = new CourseDefine();
                try {
                    assembleCourseDefine(courseDefine, courseDefineVo);
                }catch(Exception ex){
                    Log.e(TAG, "", ex);
                }
                courseDefines.add(courseDefine);
            }
        }catch(Exception e){
            Log.e(TAG,"",e);
        }
    }

    private void assembleTeacherInstitute(Institute institute) {
        TeacherInstitute teacherInstitute = new TeacherInstitute();
        teacherInstitute.setInstituteIdR(institute.getInstituteIdR());
        teacherInstitute.setTeacherIdR(loginInfo.getUserId());
        teacherInstitutes.add(teacherInstitute);
    }

    private void assembleCourseDefine(CourseDefine courseDefine, CourseDefineVo courseVo) throws JSONException {
        courseDefine.setClassIdR(courseVo.getClassId());
        courseDefine.setCode(courseVo.getCourseCode());
        courseDefine.setCourseDefineIdR(courseVo.getCourseDefineId());
        courseDefine.setLocation(courseVo.getCourseLocation());
        courseDefine.setName(courseVo.getCourseName());
        courseDefine.setSeq(courseVo.getCourseSeq());
        courseDefine.setType(courseVo.getCourseType());
        courseDefine.setInstituteIdR(courseVo.getInstituteId());
        courseDefine.setUseOnce(courseVo.getUseOnce());
        courseDefine.setWeekDay(courseVo.getWeekday());
        courseDefine.setTeacherIdR(loginInfo.getUserId());
    }

    private void assembleClass(ClassInfo classInfo, ClassInfoVo classInfoVo) throws JSONException {
        classInfo.setCode(classInfoVo.getClassCode());
        classInfo.setClassIdR(classInfoVo.getClassId());
        classInfo.setName(classInfoVo.getClassName());
        classInfo.setType(classInfoVo.getClassType());
        classInfo.setTotalNum(classInfoVo.getTotalNum());

        try {
            List<StudentVo> studentVos = classInfoVo.getStudents();
            StudentVo studentVo;
            for (int i = 0; i < studentVos.size(); i++) {
                studentVo = studentVos.get(i);
                Student student = new Student();
                try{
                    assembleStudent(student, studentVo);
                }catch(Exception ex){
                    Log.e(TAG, "", ex);
                }
                student.setClassIdR(classInfo.getClassIdR());
                students.add(student);
            }
        }catch(Exception e){
            Log.e(TAG,"",e);
        }
    }

    private void assembleStudent(Student student, StudentVo studentVo) throws JSONException {
        student.setName(studentVo.getStudentName());
        student.setCode(studentVo.getStudentCode());
        student.setBirthday(DateUtil.formatStrToDate(studentVo.getBirthday()));
        student.setGender(studentVo.getGender());
        student.setStudentIdR(studentVo.getStudentId());
    }

    @Override
    protected void loadDataFromLocal() {
        institutes = DBService.getInstance(this).getInsituteDataByTeacher(loginInfo.getUserId());
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
                spinner.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                btn_add_course.setVisibility(View.VISIBLE);
                btn_sync.setVisibility(View.VISIBLE);
                btn_history.setVisibility(View.GONE);
                if (todayFragment == null) {
                    todayFragment = new TodayFragment();
                    transaction.add(R.id.fl_content, todayFragment);
                } else {
                    todayFragment.query();
                    transaction.show(todayFragment);
                }
                break;
            case CURRENT_PAGE_SCHEDULE:
                spinner.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                btn_add_course.setVisibility(View.VISIBLE);
                btn_sync.setVisibility(View.GONE);
                btn_history.setVisibility(View.VISIBLE);
                if (courseFragment == null) {
                    courseFragment = new CourseFragment();
                    transaction.add(R.id.fl_content, courseFragment);
                } else {
                    transaction.show(courseFragment);
                }
                break;
            case CURRENT_PAGE_PROFILE:
                spinner.setVisibility(View.GONE);
                textView.setText(R.string.toolbar_profile);
                textView.setVisibility(View.VISIBLE);
                btn_add_course.setVisibility(View.GONE);
                btn_sync.setVisibility(View.GONE);
                btn_history.setVisibility(View.GONE);
                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
                    transaction.add(R.id.fl_content, profileFragment);
                } else {
                    transaction.show(profileFragment);
                }
                break;
            case CURRENT_PAGE_CLASS:
                spinner.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                btn_add_course.setVisibility(View.GONE);
                btn_sync.setVisibility(View.GONE);
                btn_history.setVisibility(View.GONE);
                if (classFragment == null) {
                    classFragment = new ClassFragment();
                    transaction.add(R.id.fl_content, classFragment);
                } else {
                    classFragment.query();
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

    private void initInstituteSpinner(){
        SchoolSpinnerAdapter schoolSpinnerAdapter = new SchoolSpinnerAdapter(this, spinner, institutes);
        schoolSpinnerAdapter.setDropDownViewResource(R.layout.school_item_spinner_dropdown);
        spinner.setAdapter(schoolSpinnerAdapter);
        spinner.setSelection(getSelInstitutePos(loginInfo.getCurrentInstituteIdR()), true);
        spinner.setDropDownVerticalOffset(15);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Toast.makeText(MainActivity.this, "你点击的是:"+institutes.get(pos).getName(), Toast.LENGTH_SHORT).show();
                loginInfo.setCurrentInstituteIdR(institutes.get(pos).getInstituteIdR());
                try {
                    AppPreference.saveLoginInfoWithoutDB(loginInfo);
                    if(todayFragment.isVisible()) {
                        todayFragment.query();
                    }else if(classFragment != null && classFragment.isVisible()) {
                        classFragment.query();
                    }else if(courseFragment != null && courseFragment.isVisible()){
                        courseFragment.refresh();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,"",e);
                    showErrorMsgInfo(e.toString());
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initEvent() {
        // 设置按钮监听
        ll_course.setOnClickListener(this);
        ll_profile.setOnClickListener(this);
        ll_today.setOnClickListener(this);
        ll_class.setOnClickListener(this);
        btn_add_course.setOnClickListener(this);
        btn_sync.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        btn_history.setOnClickListener(this);
    }

    private int getSelInstitutePos(Long currentInstituteId) {
        if(currentInstituteId == null){
            loginInfo.setCurrentInstituteIdR(institutes.get(0).getInstituteIdR());
            try {
                AppPreference.saveLoginInfoWithoutDB(loginInfo);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"",e);
                showErrorMsgInfo(e.toString());
            }
            return 0;
        }
        for(int i = 0; i < institutes.size(); i ++){
            if(institutes.get(i).getInstituteIdR() == currentInstituteId){
                return i;
            }
        }
        return -1;
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
                Intent intent = new Intent(this,AddCourseActivity.class);
                if(todayFragment.isVisible()) {
                    intent.putExtra("useOnce", true);
                }else if(courseFragment.isVisible()){
                    intent.putExtra("useOnce", false);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("courseDefine", new CourseDefineItemInfo());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_sync:
                startActivity(new Intent(this,UploadDataActivity.class));
                break;
            case R.id.btn_history:
                startActivity(new Intent(this,TemporaryCourseListActivity.class));
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