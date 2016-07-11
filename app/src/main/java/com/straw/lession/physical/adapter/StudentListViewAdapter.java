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
import com.straw.lession.physical.vo.StudentInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/11.
 */
public class StudentListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "StudentListViewAdapter";
    private Context mContext;
    private List<StudentInfo> mContentList;
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

    public StudentListViewAdapter(Context context, List<StudentInfo> list, Callback callback) {
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
    public StudentInfo getItem(int position) {
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
        StudentInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.student_item_listview, null);
            holder = new ViewHolder();
            holder.button = (Button) convertView.findViewById(R.id.btn_do_start_course);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        TextView name = (TextView) convertView.findViewById(R.id.item_name);
//        name.setText(info.getName());
        holder.button.setOnClickListener(this);
        holder.button.setTag(position);
        return convertView;
    }
}
