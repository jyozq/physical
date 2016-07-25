package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.ClassListViewAdapter;
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
import com.straw.lession.physical.vo.ClassInfo;
import com.straw.lession.physical.vo.LoginInfo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.item.ClassItemInfo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/24.
 */
public class SelectClassActivity extends ThreadToolBarBaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, ClassListViewAdapter.Callback{
    private static final String TAG = "SelectClassActivity";
    private final int RESULT_CODE=101;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private ClassListViewAdapter adapter;
    private List<ClassItemInfo> infoList = new ArrayList<ClassItemInfo>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_select_class);
        initToolBar("");
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.selclass_swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) findViewById(R.id.selclass_listview);
        adapter = new ClassListViewAdapter(this, infoList, this);
        listView.setAdapter(adapter);
        checkTokenInfo();
    }

    @Override
    public void doAfterGetToken() {
        getClassInfos();
    }

    private void getClassInfos() {
        TokenInfo tokenInfo = null;
        LoginInfo loginInfo = null;
        try {
            tokenInfo = AppPreference.getUserToken();
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMsgInfo(e.toString());
            return;
        }
        final String URL = ReqConstant.URL_BASE + "/course/define/class/list";
        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("instituteId", String.valueOf(loginInfo.getCurrentInstituteIdR())));
        showProgressDialog(getResources().getString(R.string.loading));
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
                        List<com.straw.lession.physical.vo.ClassInfo> classInfoVos =
                                JSON.parseArray(dataObject.getJSONArray("classes").toString(), com.straw.lession.physical.vo.ClassInfo.class);
                        for(ClassInfo classInfoVo : classInfoVos){
                            infoList.add(toItemInfo(classInfoVo));
                        }
                        adapter.notifyDataSetChanged();
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

    private ClassItemInfo toItemInfo(ClassInfo classInfoVo) {
        ClassItemInfo classItemInfo = new ClassItemInfo();
        classItemInfo.setClassId(classInfoVo.getClassId());
        classItemInfo.setClassName(classInfoVo.getClassName());
        return classItemInfo;
    }

    @Override
    public void onRefresh() {
        getClassInfos();
    }

    @Override
    public void click(View v) {
        Intent intent=new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("classInfo", infoList.get((Integer) v.getTag()));
        intent.putExtras(bundle);
        setResult(RESULT_CODE, intent);
        finish();
    }
}
