package com.straw.lession.physical.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.db.StudentDevice;
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
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        query();
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

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (SwipeMenuListView) layoutView.findViewById(R.id.listview);
        adapter = new CourseListViewAdapter(layoutView.getContext(), infoList, this);
//        initSwiperMenu();
//        query();
        listView.setAdapter(adapter);
    }

    private void initSwiperMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                    case 1:
                        createMenu2(menu);
                        break;
                    case 2:
                        createMenu3(menu);
                        break;
                }
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0x18, 0x5E)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_favorite);
                item1.setTitle("删除1");
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_action_good);
                item2.setTitle("测试1");
                menu.addMenuItem(item2);
            }

            private void createMenu2(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0xE0,
                        0x3F)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_important);
                item1.setTitle("删除2");
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(getContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_action_discard);
                item2.setTitle("测试2");
                menu.addMenuItem(item2);
            }

            private void createMenu3(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0x30, 0xB1,
                        0xF5)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_about);
                item1.setTitle("删除3");
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(getContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_action_share);
                item2.setTitle("测试3");
                menu.addMenuItem(item2);
            }
        };
        // set creator
//        listView.setMenuCreator(creator);
//
//        // step 2. listener item click event
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                CourseItemInfo courseItemInfo = infoList.get(position);
//                switch (index) {
//                    case 0:
//                        // open
//                        break;
//                    case 1:
//                        // delete
////					delete(item);
////                        mAppList.remove(position);
////                        mAdapter.notifyDataSetChanged();
//                        break;
//                }
//            }
//        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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
