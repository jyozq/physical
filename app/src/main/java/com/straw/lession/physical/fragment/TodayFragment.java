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

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.StartCourseActivity;
import com.straw.lession.physical.adapter.CourseListViewAdapter;
import com.straw.lession.physical.constant.CourseStatus;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.item.CourseItemInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by straw on 2016/7/8.
 */
public class TodayFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,CourseListViewAdapter.Callback{
    private static final String TAG = "TodayFragment";
    private View layoutView;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private CourseListViewAdapter adapter;
    private List<CourseItemInfo> infoList = new ArrayList<CourseItemInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        query();
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
        listView = (ListView) layoutView.findViewById(R.id.listview);
        adapter = new CourseListViewAdapter(layoutView.getContext(), infoList, this);
        query();
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("course", infoList.get((Integer) v.getTag()));
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    public void query() {
        LoginInfoVo loginInfo = null;
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            return;
        }

        List<Course> courses = DBService.getInstance(getActivity()).getTodayCourses(loginInfo.getUserId(),
                                                                                    loginInfo.getCurrentInstituteIdR());
        List<CourseDefine> unstartCourses = DBService.getInstance(getActivity())
                .getUnStartedTodayCourses(loginInfo.getUserId(),loginInfo.getCurrentInstituteIdR());
        Iterator<CourseDefine> iter = unstartCourses.iterator();
        CourseDefine courseDefineTmp = null;
        while(iter.hasNext()){
            courseDefineTmp = iter.next();
            for(Course course:courses){
                if(courseDefineTmp.getCourseDefineIdR() == course.getCourseDefineIdR()){
                    iter.remove();
                }
            }
        }
        infoList.clear();
        for(Course course : courses){
            infoList.add(toItem(course));
        }
        for(CourseDefine courseDefine : unstartCourses){
            infoList.add(toItem(courseDefine));
        }
        adapter.notifyDataSetChanged();
    }

    private CourseItemInfo toItem(CourseDefine courseDefine) {
        CourseItemInfo courseItemInfo = new CourseItemInfo();
        if(courseDefine != null) {
            courseItemInfo.setCourseDefineId(courseDefine.getCourseDefineIdR());
            courseItemInfo.setName(courseDefine.getName());
            courseItemInfo.setLocation(courseDefine.getLocation());
            courseItemInfo.setDate(DateUtil.dateToStr(new Date()));
            courseItemInfo.setSeq(courseDefine.getSeq());
            courseItemInfo.setType(courseDefine.getType());
            courseItemInfo.setWeekDay(courseDefine.getWeekDay());
            ClassInfo classInfo = courseDefine.getClassInfo();
            courseItemInfo.setClassName(classInfo.getName());
            courseItemInfo.setClassId(classInfo.getClassIdR());
        }
        courseItemInfo.setStatus(CourseStatus.UNSTARTED.getValue());
        return courseItemInfo;
    }

    private CourseItemInfo toItem(Course course) {
        CourseItemInfo courseItemInfo = toItem(course.getCourseDefine());
        courseItemInfo.setCourseId(course.getId());
        courseItemInfo.setStatus(course.getStatus());
        return courseItemInfo;
    }
}
