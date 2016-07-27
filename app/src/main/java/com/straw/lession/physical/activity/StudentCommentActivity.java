package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.StudentCommentListViewAdapter;
import com.straw.lession.physical.adapter.StudentCommentViewAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
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
import com.straw.lession.physical.vo.item.CommentItemInfo;
import com.straw.lession.physical.vo.item.StudentItemInfo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/27.
 */
public class StudentCommentActivity extends ThreadToolBarBaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "StudentCommentActivity";
    private StudentItemInfo studentItemInfo;
    private LoginInfoVo loginInfoVo;
    private TokenInfo tokenInfo;
    private TextView student_comment_classname,student_comment_studentname,student_comment_studentno;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private StudentCommentViewAdapter adapter;
    private List<CommentItemInfo> infoList = new ArrayList<CommentItemInfo>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_comment_student);
        Intent intent = getIntent();
        studentItemInfo = (StudentItemInfo) intent.getSerializableExtra("studentInfo");
        initToolBar(studentItemInfo.getName());
        MainApplication.getInstance().addActivity(this);
        try {
            loginInfoVo = AppPreference.getLoginInfo();
            tokenInfo = AppPreference.getUserToken();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"获取登录信息出错",Toast.LENGTH_SHORT).show();
            return;
        }
        initViews();
    }

    private void initViews() {
        student_comment_classname = (TextView)findViewById(R.id.student_comment_classname);
        student_comment_studentname = (TextView)findViewById(R.id.student_comment_studentname);
        student_comment_studentno = (TextView)findViewById(R.id.student_comment_studentno);
        student_comment_classname.setText(studentItemInfo.getClassName());
        student_comment_studentname.setText(studentItemInfo.getName());
        student_comment_studentno.setText(studentItemInfo.getCode());

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new StudentCommentViewAdapter(this, infoList);
        listView.setAdapter(adapter);
        query();
    }

    public void query() {
        checkTokenInfo();
    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {

    }

    @Override
    public void doAfterGetToken() {
        String URL = ReqConstant.URL_BASE + "/comment/list";
//        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("instituteId", String.valueOf(studentItemInfo.getcl)));
//        params.add(new BasicNameValuePair("classId", String.valueOf(classItemInfo.getClassIdR())));
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET,
//                URL,params,tokenInfo.getToken(),new AsyncHttpResponseHandler(){
//            @Override
//            public void onSuccess(HttpResponseBean httpResponseBean) {
//                super.onSuccess(httpResponseBean);
//                try{
//                    hideProgressDialog();
//                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
//                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
//                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){//登录成功
//                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
//                        ClassInfoVo classInfoVo = JSON.parseObject(dataObject.toString(), ClassInfoVo.class, new Feature[0]);
//                        List<StudentVo> studentVos = classInfoVo.getStudents();
//                        infoList.clear();
//                        for(StudentVo studentVo : studentVos){
//                            infoList.add(toItemInfo(studentVo));
//                        }
//                        adapter.notifyDataSetChanged();
//                    }else {//登录失败
//                        String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
//                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
//                    }
//                }catch(Exception e){
//                    hideProgressDialog();
//                    showErrorMsgInfo(e.toString());
//                    e.printStackTrace();
//                }
//            }
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
    }

    @Override
    public void onRefresh() {

    }
}