package com.straw.lession.physical.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.constant.Weekday;
import com.straw.lession.physical.vo.item.CourseItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/6.
 */
public class CourseListViewAdapter extends BaseAdapter implements View.OnClickListener{

    private static final String TAG = "CourseListViewAdapter";
    private Context mContext;
    private List<CourseItemInfo> mContentList;
    private Callback mCallback;
    private LayoutInflater inflater;

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    public interface Callback{
        public void click(View v);
    }

    public class ViewHolder {
        public TextView textView;
        public Button button;
    }

    public CourseListViewAdapter(Context context, List<CourseItemInfo> list, Callback callback) {
//        super(context, 0, list);
        inflater = LayoutInflater.from(context);
        mContext = context;
        mContentList = list;
        mCallback = callback;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount");
        return mContentList.size();
    }

    @Override
    public CourseItemInfo getItem(int position) {
        Log.i(TAG, "getItem");
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        ViewHolder holder = null;
        CourseItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.course_item_listview, null);
            holder = new ViewHolder();
            holder.button = (Button) convertView.findViewById(R.id.btn_start_course);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView className = (TextView) convertView.findViewById(R.id.class_name);
        className.setText(info.getClassName());
        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(info.getDate());
        TextView weekdayAndSeq = (TextView) convertView.findViewById(R.id.weekdayAndSeq);
        weekdayAndSeq.setText(Weekday.getName(info.getWeekDay())
                                + "   " + "第" + info.getSeq() + "节");
        TextView location = (TextView) convertView.findViewById(R.id.location);
        location.setText(info.getLocation());
        TextView type = (TextView) convertView.findViewById(R.id.type);
        type.setText(info.getType());
        holder.button.setOnClickListener(this);
        holder.button.setTag(position);
        return convertView;
    }
}

