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
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.db.CourseDao;
import com.straw.lession.physical.db.CourseDefineDao;
import com.straw.lession.physical.db.DaoSession;
import com.straw.lession.physical.dictionary.CourseDictionary;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.item.CourseItemInfo;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private List<Course> courses = new ArrayList<Course>();

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
        DaoSession session = MainApplication.getInstance().getDaoSession(getActivity());
        CourseDefineDao courseDefineDao = session.getCourseDefineDao();
        CourseDao courseDao = session.getCourseDao();

        Query courseQuery = courseDao.queryBuilder()
                .where(CourseDao.Properties.LoginId.eq(loginInfo.getTeacherId())).build();
        List<Course> courses = courseQuery.list();

        QueryBuilder.LOG_SQL=true;
        QueryBuilder.LOG_VALUES=true;
        QueryBuilder qb = courseDefineDao.queryBuilder();
        WhereCondition wc = qb.and(CourseDefineDao.Properties.LoginId.eq(loginInfo.getTeacherId()),
                                    CourseDefineDao.Properties.InstituteId
                                                .eq(loginInfo.getCurrentInstituteId()),
                                    CourseDefineDao.Properties.WeekDay.eq(DateUtil.getCurrentWeekday()));
        List<CourseDefine> courseDefines = qb.where(wc).list();

        for(Course course:courses){
            boolean notDel = false;
            for(CourseDefine courseDefine : courseDefines){
                if(course.getCourseDefineId() == courseDefine.getId()){
                    notDel = true;
                    break;
                }
            }

            if(!notDel){
                courseDao.delete(course);
            }
        }

        for(CourseDefine courseDefine : courseDefines){
            boolean isAdd = true;
            for(Course course:courses){
                if(course.getCourseDefineId() == courseDefine.getId()){
                    isAdd = false;
                    break;
                }
            }
            if(isAdd){
                Course newCourse = new Course();
                newCourse.setLoginId(loginInfo.getTeacherId());
                newCourse.setUseOnce(courseDefine.getUseOnce());
                newCourse.setCourseDefineId(courseDefine.getId());
                courseDao.insert(newCourse);
            }
        }

        courses = courseQuery.list();
        infoList.clear();
        for(Course course : courses){
            infoList.add(toItem(course));
        }
        adapter.notifyDataSetChanged();
    }

    private CourseItemInfo toItem(Course course) {
        CourseItemInfo courseItemInfo = new CourseItemInfo();
        CourseDefine courseDefine = course.getCourseDefine();
        courseItemInfo.setId(course.getCourseDefineId());
        courseItemInfo.setName(courseDefine.getName());
        courseItemInfo.setLocation(courseDefine.getLocation());
        courseItemInfo.setDate(DateUtil.dateToStr(new Date()));
        courseItemInfo.setSeq(courseDefine.getSeq());
        courseItemInfo.setType(courseDefine.getType());
        courseItemInfo.setWeekDay(courseDefine.getWeekDay());
        ClassInfo classInfo = courseDefine.getClassInfo();
        courseItemInfo.setClassName(classInfo.getName());
        courseItemInfo.setClassId(classInfo.getId());
        return courseItemInfo;
    }
}
