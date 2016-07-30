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
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/30.
 */
public class CourseDefineListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "CourseListViewAdapter";
    private Context mContext;
    private List<CourseDefineItemInfo> mContentList;
    private Callback mCallback;
    private LayoutInflater inflater;

    public CourseDefineListViewAdapter(Context context, List<CourseDefineItemInfo> list, Callback callback){
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
    public CourseDefineItemInfo getItem(int position) {
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mContentList.get(position).getCourseDefineId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        ViewHolder holder = null;
        CourseDefineItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.coursedefine_item_listview, null);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.coursedefine_item_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TextView class_no_label = (TextView) convertView.findViewById(R.id.class_no_label);
        class_no_label.setText(String.valueOf(info.getSeq()));
        LinearLayout coursedefine_info_view = (LinearLayout) convertView.findViewById(R.id.coursedefine_info_view);
        LinearLayout coursedefine_add_view = (LinearLayout) convertView.findViewById(R.id.coursedefine_add_view);
        if(Detect.notEmpty(info.getClassName())){
            class_no_label.setBackground(mContext.getResources().getDrawable(R.drawable.round_label_select));
            coursedefine_info_view.setVisibility(View.VISIBLE);
            coursedefine_add_view.setVisibility(View.GONE);
        }else{
            class_no_label.setBackground(mContext.getResources().getDrawable(R.drawable.round_label));
            coursedefine_info_view.setVisibility(View.GONE);
            coursedefine_add_view.setVisibility(View.VISIBLE);
        }
        TextView className = (TextView) convertView.findViewById(R.id.class_name);
        className.setText(info.getClassName());
        TextView class_info = (TextView) convertView.findViewById(R.id.class_info);
        class_info.setText(info.getName() + "   " + "第" + info.getSeq() + "节");
        TextView location = (TextView) convertView.findViewById(R.id.class_location);
        location.setText(info.getLocation());
        holder.linearLayout.setOnClickListener(this);
        holder.linearLayout.setTag(position);
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
        public LinearLayout linearLayout;
    }
}
