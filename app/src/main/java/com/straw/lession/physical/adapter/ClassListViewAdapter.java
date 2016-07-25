package com.straw.lession.physical.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.vo.item.ClassItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/24.
 */
public class ClassListViewAdapter  extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "ClassListViewAdapter";
    private Context mContext;
    private List<ClassItemInfo> mContentList;
    private Callback mCallback;
    private LayoutInflater inflater;

    public ClassListViewAdapter(Context context, List<ClassItemInfo> list, Callback callback) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mContentList = list;
        mCallback = callback;
    }

    public class ViewHolder {
        public TextView textView;
    }

    public interface Callback{
        public void click(View v);
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount");
        return mContentList.size();
    }

    @Override
    public ClassItemInfo getItem(int position) {
        Log.i(TAG, "getItem");
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId");
        return mContentList.get(position).getClassId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        ViewHolder holder = null;
        ClassItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.selclass_item_listview, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.selclass_course_name);
            holder.textView.setText(info.getClassName());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setOnClickListener(this);
        holder.textView.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }
}