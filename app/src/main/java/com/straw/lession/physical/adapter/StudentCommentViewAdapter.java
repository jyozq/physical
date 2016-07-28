package com.straw.lession.physical.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.vo.item.CommentItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/27.
 */
public class StudentCommentViewAdapter extends BaseAdapter {
    private static final String TAG = "StudentCommentAdapter";
    private Context mContext;
    private List<CommentItemInfo> mContentList;
    private LayoutInflater inflater;

    public StudentCommentViewAdapter(Context context, List<CommentItemInfo> list) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mContentList = list;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount");
        return mContentList.size();
    }

    @Override
    public CommentItemInfo getItem(int position) {
        Log.i(TAG, "getItem");
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId");
        return mContentList.get(position).getCommentId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        CommentItemInfo info = getItem(position);
        convertView = inflater.inflate(R.layout.comment_item_listview, null);

        TextView teacherName = (TextView)convertView.findViewById(R.id.teacherName);
        TextView commentTime = (TextView)convertView.findViewById(R.id.commentTime);
        TextView comment = (TextView)convertView.findViewById(R.id.comment);
        teacherName.setText(info.getTeachName());
        commentTime.setText(info.getCommentTime());
        comment.setText(info.getTeacherComment());
        return convertView;
    }
}
