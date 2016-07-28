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
import com.straw.lession.physical.constant.Gender;
import com.straw.lession.physical.vo.item.StudentItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/26.
 */
public class StudentCommentListViewAdapter  extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "StudentComListAdapter";
    private Context mContext;
    private List<StudentItemInfo> mContentList;
    private Callback mCallback;
    private LayoutInflater inflater;

    public StudentCommentListViewAdapter(Context context, List<StudentItemInfo> list, Callback callback) {
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
        return mContentList.get(position).getStudentIdR();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        ViewHolder holder = null;
        StudentItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.student_comment_item_listview, null);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.student_comment_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView studentname = (TextView) convertView.findViewById(R.id.studentname);
        TextView studentno = (TextView) convertView.findViewById(R.id.studentno);
        TextView gender = (TextView) convertView.findViewById(R.id.gender);
        studentname.setText(info.getName());
        studentno.setText(info.getCode());
        gender.setText(Gender.getName(info.getGender()));
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
