package com.straw.lession.physical.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.vo.item.SelClassItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/24.
 */
public class SelClassListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "SelClassListViewAdapter";
    private Context mContext;
    private List<SelClassItemInfo> mContentList;
    private Callback mCallback;
    private LayoutInflater inflater;

    public SelClassListViewAdapter(Context context, List<SelClassItemInfo> list, Callback callback) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mContentList = list;
        mCallback = callback;
    }

    public class ViewHolder {
        public LinearLayout linearLayout;
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
    public SelClassItemInfo getItem(int position) {
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
        SelClassItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.selclass_item_listview, null);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.selclass_item_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView textView = (TextView) convertView.findViewById(R.id.selclass_course_name);
        textView.setText(info.getClassName());
        holder.linearLayout.setOnClickListener(this);
        holder.linearLayout.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }
}
