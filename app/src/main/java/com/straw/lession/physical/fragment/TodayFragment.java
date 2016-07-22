package com.straw.lession.physical.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.StartCourseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.item.CourseItemInfo;
import com.straw.lession.physical.adapter.CourseListViewAdapter;
import com.straw.lession.physical.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/8.
 */
public class TodayFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,CourseListViewAdapter.Callback{
    private View layoutView;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private CourseListViewAdapter adapter;
    private List<CourseItemInfo> infoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_today, container, false);
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        infoList = new ArrayList<CourseItemInfo>();
        query();
        listView = (ListView) layoutView.findViewById(R.id.listview);
        adapter = new CourseListViewAdapter(layoutView.getContext(), infoList, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
                query();
                adapter.notifyDataSetChanged();
            }
        }, 500);
    }

    @Override
    public void click(View v) {
//        Toast.makeText(
//                getContext(),
//                "listview的内部的按钮被点击了！，位置是-->" + (Integer) v.getTag() + ",内容是-->"
//                        + infoList.get((Integer) v.getTag()),
//                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(getContext() , StartCourseActivity.class);
        intent.putExtra("course_id" , infoList.get((Integer) v.getTag()).getId());
        getContext().startActivity(intent);
    }

    public void query() {
        List<CourseDefine> courseDefines = MainApplication.getInstance().getDaoSession(getActivity()).getCourseDefineDao().loadAll();
        infoList.clear();
        CourseDefine courseDefine = null;
        for(int i = 0; i < courseDefines.size(); i++){
            courseDefine = courseDefines.get(i);
            infoList.add(toItem(courseDefine));
        }
    }

    private CourseItemInfo toItem(CourseDefine courseDefine) {
        CourseItemInfo courseItemInfo = new CourseItemInfo();
        courseItemInfo.setName(courseDefine.getName());
        courseItemInfo.setLocation(courseDefine.getCode());
        return courseItemInfo;
    }
}
