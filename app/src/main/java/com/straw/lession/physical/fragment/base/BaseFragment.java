package com.straw.lession.physical.fragment.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * Created by Administrator on 2015/12/10.
 */
public class BaseFragment extends Fragment implements View.OnClickListener {
    protected Activity mActivity;

    protected MainApplication mApp;
    protected ThreadPoolExecutor mThreadPool;
    private View viewTip ;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        mApp = (MainApplication) mActivity.getApplication();
        mThreadPool = mApp.getThreadPool();
        viewTip = mActivity.getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Fragment页面起始 (注意： 如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         * 此处因只含有Activity和Fragment,必不能调用onContainerFragmentStart/onContainerFragmentEnd
         */
        //MobCreditEase.onFragmentStart(getActivity(), "BaseFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * Fragment 页面结束（注意：如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         * 此处因只含有Activity和Fragment,务必不能调用onContainerFragmentStart/onContainerFragmentEnd
         */
        //MobCreditEase.onFragmentEnd(getActivity(), "BaseFragment");
    }
}
