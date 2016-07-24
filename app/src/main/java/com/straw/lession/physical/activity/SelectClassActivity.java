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
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.item.ClassItemInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/24.
 */
public class SelectClassActivity extends ThreadToolBarBaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, ClassListViewAdapter.Callback{
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
        getClassInfos();
        listView.setAdapter(adapter);
    }

    private void getClassInfos() {
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET, URL ,params , tokenInfo.getToken(), new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(HttpResponseBean httpResponseBean) {
//                super.onSuccess(httpResponseBean);
//                try{
//                    hideProgressDialog();
//                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
//                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
//                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){ //登录成功
//                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
//                        List<com.straw.lession.physical.vo.Institute> instituteVos =
//                                JSON.parseArray(dataObject.getJSONArray("institutes").toString(), com.straw.lession.physical.vo.Institute.class);
//                        assembleData(instituteVos);
//                        addDataToDB();
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
