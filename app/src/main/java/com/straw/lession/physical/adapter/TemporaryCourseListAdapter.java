package com.straw.lession.physical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/31.
 */
public class TemporaryCourseListAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private List<CourseDefineItemInfo> infoList;
    private Callback mCallback;
    private LayoutInflater inflater;
    private int opr;

    public TemporaryCourseListAdapter(Context context, List<CourseDefineItemInfo> infoList, Callback mCallback) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.infoList = infoList;
        this.mCallback = mCallback;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public CourseDefineItemInfo getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return infoList.get(position).getCourseDefineId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CourseDefineItemInfo info = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.temporary_coursedefine_item_listview, null);
            holder = new ViewHolder();
            holder.editButton = (ImageButton) convertView.findViewById(R.id.btn_edit_coursedefine);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.btn_del_coursedefine);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TextView className = (TextView) convertView.findViewById(R.id.class_name);
        className.setText(info.getClassName());
        TextView class_info = (TextView) convertView.findViewById(R.id.class_info);
        class_info.setText(info.getName() + "   " + "第" + info.getSeq() + "节");
        TextView location = (TextView) convertView.findViewById(R.id.class_location);
        location.setText(info.getLocation());
        holder.editButton.setOnClickListener(this);
        holder.deleteButton.setOnClickListener(this);
        holder.editButton.setTag(position);
        holder.deleteButton.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_coursedefine:
                opr = CommonConstants.OPR_EDIT;
                break;
            case R.id.btn_del_coursedefine:
                opr = CommonConstants.OPR_DEL;
                break;
        }
        mCallback.click(v,opr);
    }

    public interface Callback{
        public void click(View v,int opr);
    }

    public class ViewHolder {
        public ImageButton editButton;
        public ImageButton deleteButton;
    }
}
