package com.straw.lession.physical.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.astuetz.PagerSlidingTabStrip;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.fragment.UploadDataFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/28.
 */
public class UploadDataActivity extends ThreadToolBarBaseActivity{
    private static final String TAG = "UploadDataActivity";
    private int currentColor =0xFF5161BC;
    private List<UploadDataFragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_upload_data);
        initToolBar(getResources().getString(R.string.profile_upload));
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void initViews() {
        for(int i = 0; i < 2; i ++){
            list.add(new UploadDataFragment());
        }

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),list));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        tabs.setIndicatorColor(currentColor);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                UploadDataFragment fragment = list.get(position);
                fragment.query();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

        private List<UploadDataFragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<UploadDataFragment> list) {
            super(fm);
            fragmentList = list;
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
            UploadDataFragment fragment = fragmentList.get(position);
            Bundle bundle = new Bundle();
            if(position == 0) {
                bundle.putBoolean("isUploaded", false);
            }else if(position == 1){
                bundle.putBoolean("isUploaded", true);
            }
            fragment.setArguments(bundle);
            return fragment;
        }
    }
}
