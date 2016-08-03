package com.straw.lession.physical.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.UploadDataActivity;
import com.straw.lession.physical.adapter.UploadListViewAdapter;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.async.TaskWorker;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.task.UpdateUploadResultTask;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.UploadCourseDataHolder;
import com.straw.lession.physical.vo.UploadCourseDataResultVo;
import com.straw.lession.physical.vo.UploadCourseDataVo;
import com.straw.lession.physical.vo.UploadStudentDataVo;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.db.StudentDevice;
import com.straw.lession.physical.vo.item.UploadDataItemInfo;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/28.
 */
public class UploadDataFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,UploadListViewAdapter.Callback,View.OnClickListener{
    private static final String TAG = "UploadDataFragment";
    private LoginInfoVo loginInfo;
    private TokenInfo tokenInfo;
    private View layoutView;
    private boolean isUploaded;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private UploadListViewAdapter adapter;
    private List<UploadDataItemInfo> infoList = new ArrayList<UploadDataItemInfo>();
    private UploadDataActivity mContext;
    private Button btn_upload_all;
    private List<UploadDataItemInfo> readyToUploadDatas = new ArrayList<UploadDataItemInfo>();
    private Dialog dialog;

    @Override
    public void onResume() {
        super.onResume();
        query();
    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {

    }

    @Override
    public void doAfterGetToken() {
        try{
            tokenInfo = AppPreference.getUserToken();
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"",ex);
            Toast.makeText(getContext(),"获取token出错",Toast.LENGTH_SHORT).show();
            return;
        }

        List<UploadCourseDataVo> uploadCourseDataVos = new ArrayList<>();
        for(UploadDataItemInfo uploadDataItemInfo : readyToUploadDatas){
            long courseId = uploadDataItemInfo.getCourseId();
            Course course = DBService.getInstance(getContext()).getUnUploadCourseById(courseId, loginInfo.getUserId());
            List<StudentDevice> studentDevices = DBService.getInstance(getContext())
                    .getUnUploadStudentDeviceByCourse(courseId,loginInfo.getUserId());
            UploadCourseDataVo uploadCourseDataVo = assembleUploadCourseData(course, studentDevices);
            uploadCourseDataVos.add(uploadCourseDataVo);
        }
        UploadCourseDataHolder uploadCourseDataHolder = new UploadCourseDataHolder();
        uploadCourseDataHolder.setCourseData(uploadCourseDataVos);

        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        String URL = ReqConstant.URL_BASE + "/course/data/sync";
        mContext.showProgressDialog(getResources().getString(R.string.loading));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL ,JSON.toJSONString(uploadCourseDataHolder) , tokenInfo.getToken(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    mContext.hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                        JSONArray courseArr = dataObject.getJSONArray("courses");
                        List<UploadCourseDataResultVo> uploadResults =
                                JSON.parseArray(courseArr.toString(), UploadCourseDataResultVo.class);
                        updateUploadResult(uploadResults);
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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_upload_listview, container, false);
        isUploaded = getArguments().getBoolean("isUploaded");
        mContext = (UploadDataActivity)getActivity();
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) layoutView.findViewById(R.id.listview);
        btn_upload_all = (Button) layoutView.findViewById(R.id.btn_upload_all);
        if(isUploaded){
            btn_upload_all.setVisibility(View.INVISIBLE);
        }else{
            btn_upload_all.setVisibility(View.VISIBLE);
            btn_upload_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUpload();
                }
            });
        }
        adapter = new UploadListViewAdapter(layoutView.getContext(), infoList, this);
//        query();
        listView.setAdapter(adapter);
    }

    public void query() {
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            return;
        }
        List<Course> courses = null;
        if(isUploaded){
            courses = DBService.getInstance(getContext()).getUploadedData(loginInfo.getUserId());
        }else{
            courses = DBService.getInstance(getContext()).getUnUploadedData(loginInfo.getUserId());
        }
        infoList.clear();
        UploadDataItemInfo uploadDataItemInfo = null;
        for(Course course : courses){
            uploadDataItemInfo = toItemInfo(course);
            if(uploadDataItemInfo != null) {
                infoList.add(uploadDataItemInfo);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private UploadDataItemInfo toItemInfo(Course course) {
        if(course.getEndTime() == null) {
            return null;
        }
        UploadDataItemInfo uploadDataItemInfo = new UploadDataItemInfo();
        uploadDataItemInfo.setCourseId(course.getId());
        uploadDataItemInfo.setClassName(course.getCourseDefine().getClassInfo().getName());
        uploadDataItemInfo.setDate(DateUtil.dateToStr(course.getDate()));
        String startTimeStr = DateUtil.dateTimeToStr(course.getStartTime());
        String endTimeStr = DateUtil.dateTimeToStr(course.getEndTime());
        uploadDataItemInfo.setDuration(startTimeStr.substring(startTimeStr.indexOf(" ") + 1) + "-"
                                        +endTimeStr.substring(endTimeStr.indexOf(" ") + 1));
        uploadDataItemInfo.setUploaded(course.getIsUploaded());
        return uploadDataItemInfo;
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
    public void click(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                readyToUploadDatas.clear();
                UploadDataItemInfo clickUploadDataItemInfo = infoList.get((Integer) v.getTag());
                readyToUploadDatas.add(clickUploadDataItemInfo);
                break;
            case R.id.btn_upload_all:
                readyToUploadDatas.clear();
                readyToUploadDatas.addAll(infoList);
                break;
        }
        startUpload();

    }

    private void startUpload() {
        dialog = AlertDialogUtil.showAlertWindow2Button(getContext(), "是否上传数据？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTokenInfo();
            }
        });
    }

    private void updateUploadResult(List<UploadCourseDataResultVo> uploadResults) {
        mContext.showProgressDialog(getResources().getString(R.string.loading));
        ITask updResultTask = new UpdateUploadResultTask(getContext(), new TaskHandler() {
            @Override
            public void onSuccess(TaskResult result) {
                mContext.hideProgressDialog();
                query();
            }

            @Override
            public void onFailure(Throwable error, String content) {

            }

            @Override
            protected void onSelf() {

            }
        }, uploadResults,loginInfo.getUserId());
        TaskWorker taskWorker = new TaskWorker(updResultTask);
        mThreadPool.submit(taskWorker);
    }

    private UploadCourseDataVo assembleUploadCourseData(Course course, List<StudentDevice> studentDevices) {
        UploadCourseDataVo uploadCourseDataVo = new UploadCourseDataVo();
        uploadCourseDataVo.setCourseDate(DateUtil.dateToStr(course.getDate()));
        uploadCourseDataVo.setCourseDefineId(String.valueOf(course.getCourseDefineIdR()));
        uploadCourseDataVo.setLocalCourseSeq(String.valueOf(course.getId()));
        String startTimeStr = DateUtil.dateTimeToStr(course.getStartTime());
        String endTimeStr = DateUtil.dateTimeToStr(course.getEndTime());
        uploadCourseDataVo.setStartTime(startTimeStr.substring(startTimeStr.indexOf(" ")+1));
        uploadCourseDataVo.setEndTime(endTimeStr.substring(endTimeStr.indexOf(" ")+1));
        List<UploadStudentDataVo> students = new ArrayList<>();
        for(StudentDevice studentDevice : studentDevices){
            students.add(assembleUploadStudentData(studentDevice));
        }
        uploadCourseDataVo.setStudents(students);
        return uploadCourseDataVo;
    }

    private UploadStudentDataVo assembleUploadStudentData(StudentDevice studentDevice) {
        UploadStudentDataVo uploadStudentDataVo = new UploadStudentDataVo();
        uploadStudentDataVo.setBindingTime(DateUtil.dateTimeToStr(studentDevice.getBindTime()));
        uploadStudentDataVo.setDeviceCode(studentDevice.getDeviceNo());
        uploadStudentDataVo.setStudentId(String.valueOf(studentDevice.getStudentIdR()));
        return uploadStudentDataVo;
    }
}
