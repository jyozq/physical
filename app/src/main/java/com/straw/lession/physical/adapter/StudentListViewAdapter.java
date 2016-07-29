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
import com.straw.lession.physical.vo.item.StudentItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/11.
 */
public class StudentListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "StudentListViewAdapter";
    private Context mContext;
    private List<StudentItemInfo> mContentList;
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

    public StudentListViewAdapter(Context context, List<StudentItemInfo> list, Callback callback) {
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
    public StudentItemInfo getItem(int position) {
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
        StudentItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.student_item_listview, null);
            holder = new ViewHolder();
            holder.button = (Button) convertView.findViewById(R.id.btn_do_start_course);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView studentno = (TextView) convertView.findViewById(R.id.studentno);
        TextView student_status = (TextView) convertView.findViewById(R.id.student_status);
        studentno.setText(info.getCode());
        holder.button.setOnClickListener(this);
        if(info.isHasBinded()){
            holder.button.setText("重新绑定");
            student_status.setText(info.getDeviceNo());
        }else{
            holder.button.setText("绑定");
            student_status.setText("未绑定设备");
        }
        holder.button.setTag(position);
        return convertView;
    }
}
