package com.straw.lession.physical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.vo.SchoolInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/12.
 */
public class SchoolSpinnerAdapter extends ArrayAdapter{
    private List<SchoolInfo> mList;
    private Context mContext;

    public SchoolSpinnerAdapter(Context pContext, List<SchoolInfo> pList){
        super(pContext,android.R.layout.simple_spinner_item,pList);
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.school_item_spinner, null);
        if(convertView!=null) {
            TextView school_label = (TextView)convertView.findViewById(R.id.school_label);
            school_label.setText(mList.get(position).getName());
        }
        return convertView;
    }
}
