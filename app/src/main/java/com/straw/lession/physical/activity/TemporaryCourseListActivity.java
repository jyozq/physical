package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.adapter.TemporaryCourseListAdapter;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/31.
 */
public class TemporaryCourseListActivity extends ThreadToolBarBaseActivity implements SwipeRefreshLayout.OnRefreshListener, TemporaryCourseListAdapter.Callback{
    private static final String TAG = "TemporaryCourseListActivity";
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private TemporaryCourseListAdapter adapter;
    private List<CourseDefineItemInfo> infoList = new ArrayList<CourseDefineItemInfo>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_temporary_coursedefine);
        initToolBar(getResources().getString(R.string.temporary_coursedefine_label));
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        query();
    }

    private void query() {
        LoginInfoVo loginInfo = null;
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMsgInfo(e.toString());
            return;
        }

        List<CourseDefine> courseDefines = DBService.getInstance(this)
                .getTemporaryCourseDefine(loginInfo.getUserId(),loginInfo.getCurrentInstituteIdR());
        infoList.clear();
        for(CourseDefine courseDefine : courseDefines){
            infoList.add(toItem(courseDefine));
        }
        adapter.notifyDataSetChanged();
    }

    private CourseDefineItemInfo toItem(CourseDefine courseDefine) {
        CourseDefineItemInfo courseItemInfo = new CourseDefineItemInfo();
        courseItemInfo.setCourseDefineId(courseDefine.getCourseDefineIdR());
        courseItemInfo.setWeekDay(courseDefine.getWeekDay());
        courseItemInfo.setSeq(courseDefine.getSeq());
        courseItemInfo.setName(courseDefine.getName());
        courseItemInfo.setLocation(courseDefine.getLocation());
        courseItemInfo.setDate(DateUtil.dateToStr(courseDefine.getDate()));
        courseItemInfo.setType(courseDefine.getType());
        ClassInfo classInfo = courseDefine.getClassInfo();
        courseItemInfo.setClassName(classInfo.getName());
        courseItemInfo.setClassId(classInfo.getClassIdR());
        return courseItemInfo;
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new TemporaryCourseListAdapter(this, infoList, this);
        listView.setAdapter(adapter);

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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
                query();
            }
        }, 500);
    }

    @Override
    public void click(View v, int opr) {
        CourseDefineItemInfo courseDefineItemInfo = infoList.get((Integer) v.getTag());
        if(opr == CommonConstants.OPR_EDIT){   //编辑
            Intent intent = new Intent(this,AddCourseActivity.class);
            intent.putExtra("useOnce", true);
            Bundle bundle = new Bundle();
            bundle.putSerializable("courseDefine", courseDefineItemInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(opr == CommonConstants.OPR_DEL){ //删除

        }
    }
}
