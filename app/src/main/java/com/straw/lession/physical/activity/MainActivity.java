package com.straw.lession.physical.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import com.straw.lession.physical.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/5.
 */
public class MainActivity extends ThreadBaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    /**
     * 给ListView添加下拉刷新
     */
    private SwipeRefreshLayout swipeLayout;

    /**
     * ListView
     */
    private ListView listView;

    /**
     * ListView适配器
     */
    private ListViewAdapter adapter;

    private List<ItemInfo> infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        infoList = new ArrayList<ItemInfo>();
        ItemInfo info = new ItemInfo();
        info.setName("coin");
        infoList.add(info);
        listView = (ListView) this.findViewById(R.id.listview);
        adapter = new ListViewAdapter(this, infoList);
        listView.setAdapter(adapter);
    }

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
