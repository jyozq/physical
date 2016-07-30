package com.straw.lession.physical.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.astuetz.PagerSlidingTabStrip;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.MainActivity;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.CourseScheduleVo;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/7.
 */
public class CourseFragment extends BaseFragment {
    private View layoutView;
    private int currentColor =0xFF5161BC;
    private PagerSlidingTabStrip tabs;
    private MainActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_course, container, false);
        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) layoutView.findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) layoutView.findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        return layoutView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        tabs.setIndicatorColor(currentColor);
//        query();
    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {
        checkTokenInfo();
    }

    @Override
    public void doAfterGetToken() {
//        LoginInfoVo loginInfo = null;
//        TokenInfo tokenInfo = null;
//        try {
//            loginInfo = AppPreference.getLoginInfo();
//            tokenInfo = AppPreference.getUserToken();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG,"",e);
//            return;
//        }
//
//        String URL = ReqConstant.URL_BASE + "/course/define/sorted";
//        ArrayList<BasicNameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("instituteId", String.valueOf(loginInfo.getCurrentInstituteIdR())));
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.GET,
//                URL,params,tokenInfo.getToken(),new AsyncHttpResponseHandler(){
//            @Override
//            public void onSuccess(HttpResponseBean httpResponseBean) {
//                super.onSuccess(httpResponseBean);
//                try{
//                    mContext.hideProgressDialog();
//                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
//                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
//                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
//                        JSONObject dataObject = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
//                        JSONArray dataArr = dataObject.getJSONArray("institutes");
//                        List<CourseScheduleVo> courseScheduleVoList =
//                                JSON.parseArray(dataArr.toString(), CourseScheduleVo.class);
//
//                    }else {
//                        String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
//                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
//                    }
//                }catch(Exception e){
//                    mContext.hideProgressDialog();
//                    mContext.showErrorMsgInfo(e.toString());
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Throwable error, String content) {
//                super.onFailure(error, content);
//                mContext.hideProgressDialog();
//                String errorContent = Utils.parseErrorMessage(mContext, content);
//                mContext.showErrorMsgInfo(errorContent);
//                Log.e(TAG, content);
//            }
//        });
//        mThreadPool.execute(asyncHttpClient);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        private DayCourseFragment fragment1;
        private DayCourseFragment fragment2;
        private DayCourseFragment fragment3;
        private DayCourseFragment fragment4;
        private DayCourseFragment fragment5;
        private DayCourseFragment fragment6;
        private DayCourseFragment fragment7;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

//            current_position = position;

            switch (position) {
                case 0:
                    fragment1 = new DayCourseFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("weekday",1);
                    fragment1.setArguments(bundle);
                    return fragment1;
                case 1:
                    fragment2 = new DayCourseFragment();
                    bundle = new Bundle();
                    bundle.putInt("weekday",2);
                    fragment2.setArguments(bundle);
                    return fragment2;
                case 2:
                    fragment3 = new DayCourseFragment();
                    bundle = new Bundle();
                    bundle.putInt("weekday",3);
                    fragment3.setArguments(bundle);
                    return fragment3;
                case 3:
                    fragment4 = new DayCourseFragment();
                    bundle = new Bundle();
                    bundle.putInt("weekday",4);
                    fragment4.setArguments(bundle);
                    return fragment4;
                case 4:
                    fragment5 = new DayCourseFragment();
                    bundle = new Bundle();
                    bundle.putInt("weekday",5);
                    fragment5.setArguments(bundle);
                    return fragment5;
                case 5:
                    fragment6 = new DayCourseFragment();
                    bundle = new Bundle();
                    bundle.putInt("weekday",6);
                    fragment6.setArguments(bundle);
                    return fragment6;
                case 6:
                    fragment7 = new DayCourseFragment();
                    bundle = new Bundle();
                    bundle.putInt("weekday",0);
                    fragment7.setArguments(bundle);
                    return fragment7;
                default:
                    return null;
            }
        }
    }
}
