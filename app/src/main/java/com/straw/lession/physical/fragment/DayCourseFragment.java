package com.straw.lession.physical.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.MainActivity;
import com.straw.lession.physical.adapter.CourseDefineListViewAdapter;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.async.TaskWorker;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.task.InitDayCourseTask;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by straw on 2016/7/13.
 */
public class DayCourseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,CourseDefineListViewAdapter.Callback{
    private static final String TAG = "DayCourseFragment";
    private View layoutView;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private CourseDefineListViewAdapter adapter;
    private List<CourseDefineItemInfo> infoList = new ArrayList<CourseDefineItemInfo>();
    private MainActivity mContext;
    private int weekday;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.day_fragment_course, container, false);
        weekday = getArguments().getInt("weekday");
        mContext = (MainActivity)getActivity();
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

    private void query() {
        LoginInfoVo loginInfo = null;
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            return;
        }

        mContext.showProgressDialog(getResources().getString(R.string.loading));
        ITask updResultTask = new InitDayCourseTask(getContext(), new TaskHandler() {
            @Override
            public void onSuccess(TaskResult result) {
                mContext.hideProgressDialog();
                Map<String,Object> dataMap = result.getData();
                List<CourseDefine> courseDefines = (List<CourseDefine>)dataMap.get("data");
                Collections.sort(courseDefines, new Comparator<CourseDefine>() {
                    @Override
                    public int compare(CourseDefine lhs, CourseDefine rhs) {
                        return lhs.getSeq() - rhs.getSeq();
                    }
                });
                infoList.clear();
                for(CourseDefine courseDefine : courseDefines){
                    infoList.add(toItem(courseDefine));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable error, String content) {

            }

            @Override
            protected void onSelf() {

            }
        }, loginInfo.getUserId(),weekday);
        TaskWorker taskWorker = new TaskWorker(updResultTask);
        mThreadPool.submit(taskWorker);

    }

    private CourseDefineItemInfo toItem(CourseDefine courseDefine) {
        CourseDefineItemInfo courseItemInfo = new CourseDefineItemInfo();
        courseItemInfo.setCourseDefineId(courseDefine.getCourseDefineIdR());
        courseItemInfo.setWeekDay(courseDefine.getWeekDay());
        courseItemInfo.setSeq(courseDefine.getSeq());
        if(courseDefine.getCourseDefineIdR() == -1){
            return courseItemInfo;
        }
        courseItemInfo.setName(courseDefine.getName());
        courseItemInfo.setLocation(courseDefine.getLocation());
        courseItemInfo.setDate(DateUtil.dateToStr(new Date()));
        courseItemInfo.setType(courseDefine.getType());
        ClassInfo classInfo = courseDefine.getClassInfo();
        courseItemInfo.setClassName(classInfo.getName());
        courseItemInfo.setClassId(classInfo.getClassIdR());
        return courseItemInfo;
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) layoutView.findViewById(R.id.class_listview);
        adapter = new CourseDefineListViewAdapter(layoutView.getContext(), infoList, this);
        listView.setAdapter(adapter);
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

    @Override
    public void onRefresh() {

    }

    @Override
    public void click(View v) {

    }
}
