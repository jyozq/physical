package com.straw.lession.physical.fragment;

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
import com.alibaba.fastjson.JSON;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.MainActivity;
import com.straw.lession.physical.activity.StudentCommentListActivity;
import com.straw.lession.physical.adapter.ClassListViewAdapter;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.ClassInfoVo;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.item.ClassItemInfo;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/13.
 */
public class ClassFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ClassListViewAdapter.Callback {
    private static final String TAG = "ClassFragment";
    private View layoutView;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private ClassListViewAdapter adapter;
    private List<ClassItemInfo> infoList = new ArrayList<ClassItemInfo>();
    private MainActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    protected void loadDataFromLocal() {
        List<ClassInfo> classInfos = DBService.getInstance(getContext()).findAllClass();
        infoList.clear();
        for(ClassInfo classInfo : classInfos){
            infoList.add(toItem(classInfo));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void loadDataFromService() {
        checkTokenInfo();
    }

    @Override
    public void doAfterGetToken() {
        final LoginInfoVo loginInfoVo;
        final TokenInfo tokenInfo;
        try {
            loginInfoVo = AppPreference.getLoginInfo();
            tokenInfo = AppPreference.getUserToken();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "获取登录信息出错", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = ReqConstant.URL_BASE + "/class/list";
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("instituteId",String.valueOf(loginInfoVo.getCurrentInstituteIdR())));
        mContext.showProgressDialog(getResources().getString(R.string.loading));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET,URL,params,
                tokenInfo.getToken(),new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                try{
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){//登录成功
                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                        List<ClassInfoVo> classInfoVos =
                                JSON.parseArray(dataObject.getJSONArray("classes").toString(), ClassInfoVo.class);
                        DBService.getInstance(mContext).refineClassInfoData(classInfoVos, loginInfoVo.getCurrentInstituteIdR());
                        List<ClassInfo> classInfos = DBService.getInstance(mContext)
                                .getClassByInstitute(loginInfoVo.getCurrentInstituteIdR());
                        infoList.clear();
                        for(ClassInfo classInfo:classInfos){
                            infoList.add(toItem(classInfo));
                        }
                        mContext.hideProgressDialog();
                        adapter.notifyDataSetChanged();
                    }else {//登录失败
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
        layoutView = inflater.inflate(R.layout.fragment_class, container, false);
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) layoutView.findViewById(R.id.class_listview);
        adapter = new ClassListViewAdapter(layoutView.getContext(), infoList, this);
        listView.setAdapter(adapter);
        query();
    }

    public void query() {
        getDataByNetSate();
    }

    private ClassItemInfo toItem(ClassInfo classInfo) {
        ClassItemInfo classItemInfo = new ClassItemInfo();
        classItemInfo.setClassId(classInfo.getClassIdR());
        classItemInfo.setClassIdR(classInfo.getClassIdR());
        classItemInfo.setClassName(classInfo.getName());
        classItemInfo.setInstituteId(classInfo.getInstituteIdR());
        classItemInfo.setInstituteIdR(classInfo.getInstituteIdR());
        classItemInfo.setTotalNum(classInfo.getTotalNum());
        return classItemInfo;
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
        ClassItemInfo classItemInfo = infoList.get((Integer) v.getTag());
        Intent intent = new Intent(getActivity(), StudentCommentListActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("classInfo", classItemInfo);
        intent.putExtras(b);
        startActivity(intent);
    }
}
