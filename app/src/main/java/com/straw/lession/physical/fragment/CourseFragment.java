package com.straw.lession.physical.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.straw.lession.physical.R;
import com.straw.lession.physical.fragment.base.BaseFragment;

/**
 * Created by straw on 2016/7/7.
 */
public class CourseFragment extends BaseFragment {
    private View layoutView;
    private int currentColor =0xFF5161BC;
    private PagerSlidingTabStrip tabs;

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

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        tabs.setIndicatorColor(currentColor);
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
                    return fragment1;
                case 1:
                    fragment2 = new DayCourseFragment();
                    return fragment2;
                case 2:
                    fragment3 = new DayCourseFragment();
                    return fragment3;
                case 3:
                    fragment4 = new DayCourseFragment();
                    return fragment4;
                case 4:
                    fragment5 = new DayCourseFragment();
                    return fragment5;
                case 5:
                    fragment6 = new DayCourseFragment();
                    return fragment6;
                case 6:
                    fragment7 = new DayCourseFragment();
                    return fragment7;
                default:
                    return null;
            }
        }
    }
}
