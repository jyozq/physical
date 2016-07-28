package com.straw.lession.physical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.straw.lession.physical.vo.item.StudentItemInfo;
import com.straw.lession.physical.vo.item.UploadDataItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/28.
 */
public class UploadListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "UploadListViewAdapter";
    private Context mContext;
    private List<UploadDataItemInfo> mContentList;
    private Callback mCallback;
    private LayoutInflater inflater;

    public UploadListViewAdapter(Context context, List<UploadDataItemInfo> list, Callback callback) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mContentList = list;
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    public interface Callback{
        public void click(View v);
    }

    public class ViewHolder {
        public LinearLayout linearLayout;
    }
}
