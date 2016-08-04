package com.straw.lession.physical.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.TemporaryCourseListAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by straw on 2016/7/31.
 */
public class TemporaryCourseListActivity extends ThreadToolBarBaseActivity implements SwipeRefreshLayout.OnRefreshListener, TemporaryCourseListAdapter.Callback{
    private static final String TAG = "TemporaryCourseListActivity";
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private TemporaryCourseListAdapter adapter;
    private List<CourseDefineItemInfo> infoList = new ArrayList<CourseDefineItemInfo>();
    private Dialog dialog;
    private CourseDefineItemInfo selCourseDefineItemInfo;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_temporary_coursedefine);
        initToolBar(getResources().getString(R.string.temporary_coursedefine_label));
        myInitToolBar();
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void myInitToolBar() {
        ImageButton btn_add_tempcourse = (ImageButton)findViewById(R.id.btn_add_tempcourse);
        btn_add_tempcourse.setVisibility(View.VISIBLE);
        btn_add_tempcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemporaryCourseListActivity.this,AddCourseActivity.class);
                intent.putExtra("useOnce", true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("courseDefine", new CourseDefineItemInfo());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        query();
    }

    private void query() {
        LoginInfoVo loginInfo = null;
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMsgInfo(e.toString());
            return;
        }

        List<CourseDefine> courseDefines = DBService.getInstance(this)
                .getTemporaryCourseDefine(loginInfo.getUserId(),loginInfo.getCurrentInstituteIdR());
        infoList.clear();
        for(CourseDefine courseDefine : courseDefines){
            infoList.add(toItem(courseDefine));
        }
        adapter.notifyDataSetChanged();
    }

    private CourseDefineItemInfo toItem(CourseDefine courseDefine) {
        CourseDefineItemInfo courseItemInfo = new CourseDefineItemInfo();
        courseItemInfo.setCourseDefineId(courseDefine.getCourseDefineIdR());
        courseItemInfo.setWeekDay(courseDefine.getWeekDay()==null?-1:courseDefine.getWeekDay());
        courseItemInfo.setSeq(courseDefine.getSeq());
        courseItemInfo.setName(courseDefine.getName());
        courseItemInfo.setLocation(courseDefine.getLocation());
        courseItemInfo.setDate(DateUtil.dateToStr(courseDefine.getDate()));
        courseItemInfo.setType(courseDefine.getType());
        ClassInfo classInfo = courseDefine.getClassInfo();
        courseItemInfo.setClassName(classInfo.getName());
        courseItemInfo.setClassId(classInfo.getClassIdR());
        return courseItemInfo;
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new TemporaryCourseListAdapter(this, infoList, this);
        listView.setAdapter(adapter);

    }

    @Override
    protected void loadDataFromLocal() {
        AlertDialogUtil.showAlertWindow(mContext, -1, "请联网后重试",null);
    }

    @Override
    protected void loadDataFromService() {
        checkTokenInfo();
    }

    @Override
    public void doAfterGetToken() {
        TokenInfo tokenInfo = null;
        final LoginInfoVo loginInfoVo;
        try{
            tokenInfo = AppPreference.getUserToken();
            loginInfoVo = AppPreference.getLoginInfo();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"",e);
            Toast.makeText(mContext,"获取登录信息或token出错",Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = ReqConstant.URL_BASE + "/course/define/remove";
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("courseDefineId", String.valueOf(selCourseDefineItemInfo.getCourseDefineId())));
        showProgressDialog(getResources().getString(R.string.loading));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL, params, tokenInfo.getToken(),new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
                        DBService.getInstance(mContext).delteCourseDefine(selCourseDefineItemInfo.getCourseDefineId(),loginInfoVo.getUserId());
                        query();
                    }else {
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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
                query();
            }
        }, 500);
    }

    @Override
    public void click(View v, int opr) {
        selCourseDefineItemInfo = infoList.get((Integer) v.getTag());
        if(opr == CommonConstants.OPR_EDIT){   //编辑
            Intent intent = new Intent(this,AddCourseActivity.class);
            intent.putExtra("useOnce", true);
            Bundle bundle = new Bundle();
            bundle.putSerializable("courseDefine", selCourseDefineItemInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(opr == CommonConstants.OPR_DEL){ //删除
            dialog = AlertDialogUtil.showAlertWindow2Button(this, "确定要删除吗？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {   //cancel
                    dialog.dismiss();
                }
            }, new View.OnClickListener() {     //ok
                @Override
                public void onClick(View v) {
                    deleteCourseDefine();
                    dialog.dismiss();
                }
            });
        }
    }

    private void deleteCourseDefine() {
        getDataByNetSate();
    }
}
