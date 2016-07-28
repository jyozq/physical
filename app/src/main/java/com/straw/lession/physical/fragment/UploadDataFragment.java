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
import com.straw.lession.physical.adapter.UploadListViewAdapter;
import com.straw.lession.physical.db.DbService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.item.UploadDataItemInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/28.
 */
public class UploadDataFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,UploadListViewAdapter.Callback{
    private static final String TAG = "UploadDataFragment";
    private LoginInfoVo loginInfo;
    private View layoutView;
    private boolean isUploaded;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private UploadListViewAdapter adapter;
    private List<UploadDataItemInfo> infoList = new ArrayList<UploadDataItemInfo>();

    @Override
    public void onResume() {
        super.onResume();
        query();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isUploaded = getArguments().getBoolean("isUploaded");
        layoutView = inflater.inflate(R.layout.fragment_upload_listview, container, false);
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
        adapter = new UploadListViewAdapter(layoutView.getContext(), infoList, this);
        query();
        listView.setAdapter(adapter);
    }

    private void query() {
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            return;
        }
        List<Course> courses = null;
        if(isUploaded){
            courses = DbService.getInstance(getContext()).getUploadedData(loginInfo.getUserId());
        }else{
            courses = DbService.getInstance(getContext()).getUnUploadedData(loginInfo.getUserId());
        }
        infoList.clear();
        for(Course course : courses){
            infoList.add(toItemInfo(course));
        }
        adapter.notifyDataSetChanged();
    }

    private UploadDataItemInfo toItemInfo(Course course) {
        UploadDataItemInfo uploadDataItemInfo = new UploadDataItemInfo();
        uploadDataItemInfo.setCourseId(course.getId());
        uploadDataItemInfo.setClassName(course.getCourseDefine().getClassInfo().getName());
        uploadDataItemInfo.setDate(DateUtil.dateToStr(course.getDate()));
        String startTimeStr = DateUtil.dateTimeToStr(course.getStartTime());
        String endTimeStr = DateUtil.dateTimeToStr(course.getEndTime());
        uploadDataItemInfo.setDuration(startTimeStr.substring(startTimeStr.indexOf("") + 1) + "-"
                                        +endTimeStr.substring(endTimeStr.indexOf("") + 1));
        return uploadDataItemInfo;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void click(View v) {
        UploadDataItemInfo uploadDataItemInfo = infoList.get((Integer) v.getTag());

    }
}
