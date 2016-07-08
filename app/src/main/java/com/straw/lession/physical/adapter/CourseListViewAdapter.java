package com.straw.lession.physical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.vo.CourseItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/6.
 */
public class CourseListViewAdapter extends ArrayAdapter<CourseItemInfo> {

    private LayoutInflater inflater;

    public CourseListViewAdapter(Context context, List<CourseItemInfo> list) {
        super(context, 0, list);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.course_item_listview, null);
        }

//        TextView name = (TextView) convertView.findViewById(R.id.item_name);
//        name.setText(info.getName());

        return convertView;
    }

}

