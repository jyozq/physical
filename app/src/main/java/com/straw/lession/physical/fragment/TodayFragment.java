package com.straw.lession.physical.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.StartCourseActivity;
import com.straw.lession.physical.adapter.CourseListViewAdapter;
import com.straw.lession.physical.constant.CourseStatus;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.custom.swipemenulistview.SwipeMenu;
import com.straw.lession.physical.custom.swipemenulistview.SwipeMenuCreator;
import com.straw.lession.physical.custom.swipemenulistview.SwipeMenuItem;
import com.straw.lession.physical.custom.swipemenulistview.SwipeMenuListView;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.db.StudentDevice;
import com.straw.lession.physical.vo.item.CourseItemInfo;

import java.util.*;

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
    private Dialog dialog;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        }else{
            query();
        }
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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_today, container, false);
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        query();
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
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
        final CourseItemInfo selCourseItemInfo = infoList.get((Integer) v.getTag());
        switch (v.getId()){
            case R.id.btn_start_course:
                Intent intent = new Intent();
                intent.setClass(getContext() , StartCourseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", selCourseItemInfo);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
            case R.id.btn_del_coursedefine:
                dialog = AlertDialogUtil.showAlertWindow2Button(getContext(), "确定要删除吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {   //cancel
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {     //ok
                    @Override
                    public void onClick(View v) {
                        deleteTodayCourse(selCourseItemInfo);
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void deleteTodayCourse(CourseItemInfo selCourseItemInfo) {

    }

    public void query() {
        getLoginAndToken();
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
            CourseItemInfo courseItemInfo = toItem(course);
            int bindedStudentNum = DBService.getInstance(getContext())
                                            .countBindedStudentNumByCourse(loginInfo.getUserId(),course.getId());
            courseItemInfo.setBindedStudentNum(bindedStudentNum);
            infoList.add(courseItemInfo);
        }
        for(CourseDefine courseDefine : unstartCourses){
            CourseItemInfo courseItemInfo = toItem(courseDefine);
            List<StudentDevice> studentDevices = DBService.getInstance(getContext())
                    .getStudentDeviceByCourseDefine(courseDefine.getCourseDefineIdR(),loginInfo.getUserId());
            int cnt = 0;
            for(StudentDevice studentDevice : studentDevices){
                if(DateUtil.isToday(studentDevice.getBindTime())){
                    cnt++;
                }
            }
            courseItemInfo.setBindedStudentNum(cnt);
            infoList.add(courseItemInfo);
        }
        Collections.sort(infoList, new Comparator<CourseItemInfo>() {
            @Override
            public int compare(CourseItemInfo lhs, CourseItemInfo rhs) {
                return lhs.getSeq() - rhs.getSeq();
            }
        });
        adapter.notifyDataSetChanged();
    }

    private CourseItemInfo toItem(CourseDefine courseDefine) {
        CourseItemInfo courseItemInfo = new CourseItemInfo();
        if(courseDefine != null) {
            courseItemInfo.setCourseDefineId(courseDefine.getCourseDefineIdR());
            courseItemInfo.setName(courseDefine.getName());
            courseItemInfo.setLocation(courseDefine.getLocation());
            courseItemInfo.setDate(DateUtil.dateToStr(courseDefine.getDate()==null?new Date():courseDefine.getDate()));
            courseItemInfo.setSeq(courseDefine.getSeq());
            courseItemInfo.setType(courseDefine.getType());
            courseItemInfo.setWeekDay(courseDefine.getWeekDay()==null?-1:courseDefine.getWeekDay());
            ClassInfo classInfo = courseDefine.getClassInfo();
            courseItemInfo.setClassName(classInfo.getName());
            courseItemInfo.setClassId(classInfo.getClassIdR());
            courseItemInfo.setTotalStudentNum(classInfo.getTotalNum());
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
