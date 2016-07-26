package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.SelClassListViewAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.db.DbService;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.item.SelClassItemInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/24.
 */
public class SelectClassActivity extends ThreadToolBarBaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, SelClassListViewAdapter.Callback{
    private static final String TAG = "SelectClassActivity";
    private final int RESULT_CODE=101;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private SelClassListViewAdapter adapter;
    private List<SelClassItemInfo> infoList = new ArrayList<SelClassItemInfo>();

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
        adapter = new SelClassListViewAdapter(this, infoList, this);
        listView.setAdapter(adapter);
        checkTokenInfo();
    }

    @Override
    public void doAfterGetToken() {
        getClassInfos();
    }

    private void getClassInfos() {
        TokenInfo tokenInfo = null;
        LoginInfoVo loginInfo = null;
        try {
            tokenInfo = AppPreference.getUserToken();
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMsgInfo(e.toString());
            return;
        }
//        final String URL = ReqConstant.URL_BASE + "/course/define/class/list";
//        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//        params.add(new BasicNameValuePair("instituteId", String.valueOf(loginInfo.getCurrentInstituteIdR())));
//        showProgressDialog(getResources().getString(R.string.loading));
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
//                        List<ClassInfoVo> classInfoVos =
//                                JSON.parseArray(dataObject.getJSONArray("classes").toString(), ClassInfoVo.class);
//                        for(ClassInfoVo classInfoVo : classInfoVos){
//                            infoList.add(toItemInfo(classInfoVo));
//                        }
//                        adapter.notifyDataSetChanged();
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
        List<ClassInfo> classInfos = DbService.getInstance(this).getAllClass(loginInfo.getTeacherId(),
                                                                                loginInfo.getCurrentInstituteId());
        for(ClassInfo classInfo : classInfos) {
            infoList.add(toItemInfo(classInfo));
        }
        adapter.notifyDataSetChanged();
    }

    private SelClassItemInfo toItemInfo(ClassInfo classInfo) {
        SelClassItemInfo selClassItemInfo = new SelClassItemInfo();
        selClassItemInfo.setClassId(classInfo.getClassIdR());
        selClassItemInfo.setClassName(classInfo.getName());
        return selClassItemInfo;
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
        MainApplication.getInstance().popCurrentActivity();
    }
}
