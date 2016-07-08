package com.straw.lession.physical.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.ItemInfo;
import com.straw.lession.physical.activity.ListViewAdapter;
import com.straw.lession.physical.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/7.
 */
public class CourseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View layoutView;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private ListViewAdapter adapter;
    private List<ItemInfo> infoList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_course, container, false);
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        swipeLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        infoList = new ArrayList<ItemInfo>();
        ItemInfo info = new ItemInfo();
        info.setName("coin");
        infoList.add(info);
        listView = (ListView) layoutView.findViewById(R.id.listview);
        adapter = new ListViewAdapter(layoutView.getContext(), infoList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
                ItemInfo info = new ItemInfo();
                info.setName("coin-refresh");
                infoList.add(info);
                adapter.notifyDataSetChanged();
            }
        }, 500);
    }
}
