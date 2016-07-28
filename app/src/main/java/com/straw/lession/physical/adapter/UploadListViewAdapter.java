package com.straw.lession.physical.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.straw.lession.physical.R;
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
        return mContentList.size();
    }

    @Override
    public UploadDataItemInfo getItem(int position) {
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mContentList.get(position).getCourseId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i(TAG, "getView");
        ViewHolder holder = null;
        UploadDataItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.upload_item_listview, null);
            holder = new ViewHolder();
            holder.button = (ImageButton) convertView.findViewById(R.id.btn_upload);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView class_name = (TextView) convertView.findViewById(R.id.class_name);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        class_name.setText(info.getClassName());
        date.setText(info.getDate());
        duration.setText(info.getDuration());
        holder.button.setOnClickListener(this);
        holder.button.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    public interface Callback{
        public void click(View v);
    }

    public class ViewHolder {
        public ImageButton button;
    }
}
