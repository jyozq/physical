package com.straw.lession.physical.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by straw on 2016/7/30.
 */
public class CourseDefineListViewAdapter extends BaseAdapter implements View.OnClickListener,View.OnLongClickListener{
    private static final String TAG = "CourseListViewAdapter";
    private Context mContext;
    private List<CourseDefineItemInfo> mContentList;
    private Callback mCallback;
    private LayoutInflater inflater;
    private int opr;
    private HashMap<Integer, Integer> isCheckBoxVisible;// 用来记录是否显示checkBox
    private HashMap<Integer, Boolean> isChecked;// 用来记录是否被选中
    private List<CourseDefineItemInfo> list_delete = new ArrayList<CourseDefineItemInfo>();// 需要删除的数据
    private boolean isMultiSelect = false;// 是否处于多选状态

    public CourseDefineListViewAdapter(Context context, List<CourseDefineItemInfo> list, Callback callback){
        inflater = LayoutInflater.from(context);
        mContext = context;
        mContentList = list;
        mCallback = callback;
        isCheckBoxVisible = new HashMap<Integer, Integer>();
        isChecked = new HashMap<Integer, Boolean>();
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
//            holder.editButton = (ImageButton) convertView.findViewById(R.id.btn_edit_coursedefine);
//            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.btn_del_coursedefine);
            holder.coursedefine_info_view = (LinearLayout) convertView.findViewById(R.id.coursedefine_info_view);
            holder.coursedefine_add_view = (LinearLayout) convertView.findViewById(R.id.coursedefine_add_view);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TextView class_no_label = (TextView) convertView.findViewById(R.id.class_no_label);
        class_no_label.setText(String.valueOf(info.getSeq()));
        LinearLayout coursedefine_info_view = (LinearLayout) convertView.findViewById(R.id.coursedefine_info_view);
        LinearLayout coursedefine_add_view = (LinearLayout) convertView.findViewById(R.id.coursedefine_add_view);
        if(Detect.notEmpty(info.getClassName())){
            // 根据position设置CheckBox是否可见，是否选中
            if(isMultiSelect){
                holder.cb.setVisibility(View.VISIBLE);
            }
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
//        holder.editButton.setOnClickListener(this);
//        holder.deleteButton.setOnClickListener(this);
        holder.coursedefine_add_view.setOnClickListener(this);
//        holder.editButton.setTag(position);
//        holder.deleteButton.setTag(position);
        holder.coursedefine_add_view.setTag(position);
        holder.coursedefine_info_view.setOnLongClickListener(this);
        holder.coursedefine_info_view.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_coursedefine:
                opr = CommonConstants.OPR_EDIT;
                mCallback.click(v, opr);
                break;
            case R.id.btn_del_coursedefine:
                opr = CommonConstants.OPR_DEL;
                mCallback.click(v, opr);
                break;
            case R.id.coursedefine_add_view:
                mCallback.addCourseDefine(v);
                break;
        }


    }

    @Override
    public boolean onLongClick(View v) {
        mCallback.longClick(v);
        return false;
    }

    public interface Callback{
        public void addCourseDefine(View v);
        public void click(View v, int opr);
        public void longClick(View v);
    }

    public class ViewHolder {
//        public ImageButton editButton;
//        public ImageButton deleteButton;
        public LinearLayout coursedefine_info_view;
        public LinearLayout coursedefine_add_view;
        public CheckBox cb;
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        isMultiSelect = multiSelect;
    }
}
