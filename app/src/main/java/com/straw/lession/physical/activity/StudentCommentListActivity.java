package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.ClassSpinnerAdapter;
import com.straw.lession.physical.adapter.StudentCommentListViewAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DbService;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.ClassInfoVo;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.StudentVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.item.ClassItemInfo;
import com.straw.lession.physical.vo.item.StudentItemInfo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/26.
 */
public class StudentCommentListActivity extends ThreadToolBarBaseActivity implements SwipeRefreshLayout.OnRefreshListener, StudentCommentListViewAdapter.Callback{
    private static final String TAG = "StudentListActivity";
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private StudentCommentListViewAdapter adapter;
    private List<StudentItemInfo> infoList = new ArrayList<StudentItemInfo>();
    private Spinner spinner_class;
    private ClassItemInfo classItemInfo;
    private LoginInfoVo loginInfoVo;
    private TokenInfo tokenInfo;
    private List<ClassInfo> classInfos;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_student_list);
        Intent intent = getIntent();
        classItemInfo = (ClassItemInfo)intent.getSerializableExtra("classInfo");
        initToolBar();
        MainApplication.getInstance().addActivity(this);
        try {
            loginInfoVo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"获取登录信息出错",Toast.LENGTH_SHORT).show();
            return;
        }
        initViews();
    }

    private void initToolBar() {
        hideToolBarView();
        toolbar.findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        spinner_class = (Spinner) toolbar.findViewById(R.id.spinner_class);
        spinner_class.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new StudentCommentListViewAdapter(this, infoList, this);
        listView.setAdapter(adapter);
        initClassSpinner();
        query();
    }

    private void initClassSpinner() {
        classInfos = DbService.getInstance(this)
                .getAllClass(loginInfoVo.getTeacherId(),loginInfoVo.getCurrentInstituteId());
        ClassSpinnerAdapter schoolSpinnerAdapter = new ClassSpinnerAdapter(this, spinner_class, classInfos);
        schoolSpinnerAdapter.setDropDownViewResource(R.layout.school_item_spinner_dropdown);
        spinner_class.setAdapter(schoolSpinnerAdapter);
        spinner_class.setSelection(getSelClassPos(classItemInfo.getClassId()), true);
        spinner_class.setDropDownVerticalOffset(15);
        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Toast.makeText(StudentCommentListActivity.this, "你点击的是:"+classInfos.get(pos).getName(), Toast.LENGTH_SHORT).show();
                query();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getSelClassPos(long classId) {
        for(int i = 0; i < classInfos.size(); i ++){
            if(classInfos.get(i).getId() == classId){
                return i;
            }
        }
        return 0;
    }

    public void query() {
        checkTokenInfo();
    }

    private StudentItemInfo toItemInfo(StudentVo studentVo) {
        StudentItemInfo studentItemInfo = new StudentItemInfo();
        studentItemInfo.setName(studentVo.getStudentName());
        studentItemInfo.setCode(studentVo.getStudentCode());
        studentItemInfo.setGender(studentVo.getGender());
        studentItemInfo.setStudentIdR(studentVo.getStudentId());
        return studentItemInfo;
    }

    @Override
    public void doAfterGetToken() {
        try {
            tokenInfo = AppPreference.getUserToken();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"获取token出错",Toast.LENGTH_SHORT).show();
            return;
        }
        String URL = ReqConstant.URL_BASE + "/class/student/list";
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("instituteId", String.valueOf(loginInfoVo.getCurrentInstituteIdR())));
        params.add(new BasicNameValuePair("classId", String.valueOf(classItemInfo.getClassIdR())));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET,
                URL,params,tokenInfo.getToken(),new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){//登录成功
                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                        ClassInfoVo classInfoVo = JSON.parseObject(dataObject.toString(), ClassInfoVo.class, null);
                        List<StudentVo> studentVos = classInfoVo.getStudents();
                        infoList.clear();
                        for(StudentVo studentVo : studentVos){
                            infoList.add(toItemInfo(studentVo));
                        }
                        adapter.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {

    }

    @Override
    public void click(View v) {
        StudentItemInfo studentItemInfo = infoList.get((Integer) v.getTag());
        Intent intent = new Intent(this, StudentCommentListActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("studentInfo", studentItemInfo);
        intent.putExtras(b);
//        startActivity(intent);
    }
}
