package com.straw.lession.physical.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.AddCourseActivity;
import com.straw.lession.physical.activity.MainActivity;
import com.straw.lession.physical.adapter.CourseDefineListViewAdapter;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.async.TaskWorker;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.task.InitDayCourseTask;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by straw on 2016/7/13.
 */
public class DayCourseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,CourseDefineListViewAdapter.Callback{
    private static final String TAG = "DayCourseFragment";
    private View layoutView;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private CourseDefineListViewAdapter adapter;
    private List<CourseDefineItemInfo> infoList = new ArrayList<CourseDefineItemInfo>();
    private MainActivity mContext;
    private int weekday;
    private Dialog dialog;
    private CourseDefineItemInfo selCourseDefineItemInfo;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.day_fragment_course, container, false);
        mContext = (MainActivity)getActivity();
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        weekday = getArguments().getInt("weekday");
        query();
    }

    public void query() {
        LoginInfoVo loginInfo = null;
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            return;
        }

        mContext.showProgressDialog(getResources().getString(R.string.loading));
        ITask updResultTask = new InitDayCourseTask(getContext(), new TaskHandler() {
            @Override
            public void onSuccess(TaskResult result) {
                mContext.hideProgressDialog();
                Map<String,Object> dataMap = result.getData();
                List<CourseDefine> courseDefines = (List<CourseDefine>)dataMap.get("data");
                Collections.sort(courseDefines, new Comparator<CourseDefine>() {
                    @Override
                    public int compare(CourseDefine lhs, CourseDefine rhs) {
                        return lhs.getSeq() - rhs.getSeq();
                    }
                });
                infoList.clear();
                for(CourseDefine courseDefine : courseDefines){
                    infoList.add(toItem(courseDefine));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable error, String content) {
            }

            @Override
            protected void onSelf() {
            }
        }, loginInfo.getUserId(),loginInfo.getCurrentInstituteIdR(),weekday);
        TaskWorker taskWorker = new TaskWorker(updResultTask);
        mThreadPool.submit(taskWorker);

    }

    private CourseDefineItemInfo toItem(CourseDefine courseDefine) {
        CourseDefineItemInfo courseItemInfo = new CourseDefineItemInfo();
        courseItemInfo.setCourseDefineId(courseDefine.getCourseDefineIdR());
        courseItemInfo.setWeekDay(courseDefine.getWeekDay() == null?-1:courseDefine.getWeekDay());
        courseItemInfo.setSeq(courseDefine.getSeq());
        if(courseDefine.getCourseDefineIdR() == -1){
            return courseItemInfo;
        }
        courseItemInfo.setName(courseDefine.getName());
        courseItemInfo.setLocation(courseDefine.getLocation());
        courseItemInfo.setDate(DateUtil.dateToStr(new Date()));
        courseItemInfo.setType(courseDefine.getType());
        ClassInfo classInfo = courseDefine.getClassInfo();
        courseItemInfo.setClassName(classInfo.getName());
        courseItemInfo.setClassId(classInfo.getClassIdR());
        return courseItemInfo;
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) layoutView.findViewById(R.id.class_listview);
        adapter = new CourseDefineListViewAdapter(layoutView.getContext(), infoList, this);
        listView.setAdapter(adapter);
//        query();
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
        mContext.showProgressDialog(getResources().getString(R.string.loading));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL, params, tokenInfo.getToken(),new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    mContext.hideProgressDialog();
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
                    mContext.hideProgressDialog();
                    mContext.showErrorMsgInfo(e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                mContext.hideProgressDialog();
                String errorContent = Utils.parseErrorMessage(mContext, content);
                mContext.showErrorMsgInfo(errorContent);
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
    public void addCourseDefine(View v) {
        int seq = ((Integer)v.getTag()) + 1;
        CourseDefineItemInfo courseDefineItemInfo = new CourseDefineItemInfo();
        courseDefineItemInfo.setWeekDay(weekday);
        courseDefineItemInfo.setSeq(seq);
        Intent intent = new Intent(getContext(), AddCourseActivity.class);
        intent.putExtra("useOnce", false);
        Bundle bundle = new Bundle();
        bundle.putSerializable("courseDefine", courseDefineItemInfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void click(View v, int opr) {
        selCourseDefineItemInfo = infoList.get((Integer) v.getTag());
        if (opr == CommonConstants.OPR_EDIT) {   //编辑
            Intent intent = new Intent(getContext(), AddCourseActivity.class);
            intent.putExtra("useOnce", false);
            Bundle bundle = new Bundle();
            bundle.putSerializable("courseDefine", selCourseDefineItemInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (opr == CommonConstants.OPR_DEL) { //删除
            dialog = AlertDialogUtil.showAlertWindow2Button(getContext(), "确定要删除吗？", new View.OnClickListener() {
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
