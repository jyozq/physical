package com.straw.lession.physical.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.astuetz.PagerSlidingTabStrip;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.fragment.UploadDataFragment;

/**
 * Created by straw on 2016/7/28.
 */
public class UploadDataActivity extends ThreadToolBarBaseActivity{
    private static final String TAG = "UploadDataActivity";
    private View layoutView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_upload_data);
        initToolBar(getResources().getString(R.string.profile_upload));
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void initViews() {
        ViewPager pager = (ViewPager) layoutView.findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) layoutView.findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {

    }

    @Override
    public void doAfterGetToken() {

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "未上传", "已上传"};

        private UploadDataFragment fragment1;
        private UploadDataFragment fragment2;

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
            switch (position) {
                case 0:
                    fragment1 = new UploadDataFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isUploaded", false);
                    fragment1.setArguments(bundle);
                    return fragment1;
                case 1:
                    fragment2 = new UploadDataFragment();
                    bundle = new Bundle();
                    bundle.putBoolean("isUploaded", true);
                    fragment1.setArguments(bundle);
                    return fragment2;
                default:
                    return null;
            }
        }
    }
}
