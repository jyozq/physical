package com.straw.lession.physical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.straw.lession.physical.R;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/31.
 */
public class TemporaryCourseListAdapter extends BaseAdapter implements View.OnClickListener,View.OnLongClickListener{
    private Context mContext;
    private List<CourseDefineItemInfo> infoList;
    private Callback mCallback;
    private LayoutInflater inflater;
    private int opr;
    private List<CourseDefineItemInfo> list_delete = new ArrayList<CourseDefineItemInfo>();// 需要删除的数据
    private boolean isMultiSelect = false;// 是否处于多选状态
    private TextView tv_sum;

    public TemporaryCourseListAdapter(Context context, List<CourseDefineItemInfo> infoList, Callback mCallback, TextView tv_sum) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.infoList = infoList;
        this.mCallback = mCallback;
        this.tv_sum = tv_sum;
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
            holder.coursedefine_info_view = (LinearLayout) convertView.findViewById(R.id.coursedefine_info_view);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(isMultiSelect){
            holder.cb.setVisibility(View.VISIBLE);
            holder.cb.setChecked(info.isChecked());
        }else{
            holder.cb.setVisibility(View.INVISIBLE);
            holder.cb.setChecked(false);
        }
        TextView className = (TextView) convertView.findViewById(R.id.class_name);
        className.setText(info.getClassName());
        TextView class_info = (TextView) convertView.findViewById(R.id.class_info);
        class_info.setText(info.getName() + "   " + "第" + info.getSeq() + "节");
        TextView location = (TextView) convertView.findViewById(R.id.class_location);
        location.setText(info.getLocation());
        holder.coursedefine_info_view.setTag(position);
        holder.coursedefine_info_view.setOnLongClickListener(this);
        holder.coursedefine_info_view.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coursedefine_info_view:
                // 处于多选模式
                if (isMultiSelect) {
                    CheckBox cb = (CheckBox) v.findViewById(R.id.cb_select);
                    CourseDefineItemInfo info = infoList.get((Integer)v.getTag());
                    if (cb.isChecked()) {
                        cb.setChecked(false);
                        info.setChecked(false);
                        list_delete.remove(info);
                    } else {
                        cb.setChecked(true);
                        info.setChecked(true);
                        list_delete.add(info);
                    }
                    calcSelectedNum();
                }else {
                    opr = CommonConstants.OPR_EDIT;
                    mCallback.click(v, opr);
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        list_delete.clear();
        list_delete.add(infoList.get((Integer)v.getTag()));
        calcSelectedNum();
        mCallback.longClick(v);
        return true;
    }

    public interface Callback{
        public void click(View v,int opr);
        public void longClick(View v);
    }

    public class ViewHolder {
        public LinearLayout coursedefine_info_view;
        public CheckBox cb;
    }

    public void enterMultiSelectMode() {
        isMultiSelect = true;
        calcSelectedNum();
    }

    public void removeMultiSelectModel() {
        isMultiSelect = false;
        list_delete.clear();
        for(CourseDefineItemInfo courseDefineItemInfo : infoList){
            courseDefineItemInfo.setChecked(false);
        }
    }

    private void calcSelectedNum() {
        tv_sum.setText("共选择了" + list_delete.size() + "项");
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        isMultiSelect = multiSelect;
    }

    public List<CourseDefineItemInfo> getList_delete() {
        return list_delete;
    }

    public void setList_delete(List<CourseDefineItemInfo> list_delete) {
        this.list_delete = list_delete;
    }
}
