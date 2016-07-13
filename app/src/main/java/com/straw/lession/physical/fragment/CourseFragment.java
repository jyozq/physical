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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_course, container, false);
        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) layoutView.findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) layoutView.findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
                "Top New Free", "Trending" };

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

            Fragment fragment = new BaseFragment();
            switch (position) {
                case 0:
//                    fragment = new ContainerFragment();
                    break;
                default:
//                    fragment = new CommonFragment(TITLES[position]);
                    break;
            }
            return fragment;
        }
    }
}
