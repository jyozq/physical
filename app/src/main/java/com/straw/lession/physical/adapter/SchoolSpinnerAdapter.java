package com.straw.lession.physical.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.vo.SchoolInfo;

import java.util.List;

/**
 * Created by straw on 2016/7/12.
 */
public class SchoolSpinnerAdapter extends ArrayAdapter{
    private static final String TAG = "SchoolSpinnerAdapter";
    private List<SchoolInfo> mList;
    private Context mContext;
    private Spinner spinner;

    public SchoolSpinnerAdapter(Context pContext, Spinner spinner, List<SchoolInfo> pList){
        super(pContext,android.R.layout.simple_spinner_item,pList);
        this.mContext = pContext;
        this.mList = pList;
        this.spinner = spinner;
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
            Log.i(TAG,mList.get(position).getName());
            school_label.setText(mList.get(position).getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.school_item_spinner_dropdown, null);
        TextView label = (TextView) view
                .findViewById(R.id.school_label);
        ImageView check = (ImageView) view
                .findViewById(R.id.school_checked_image);
        label.setText(mList.get(position).getName());
        if (spinner.getSelectedItemPosition() == position) {
            check.setVisibility(View.VISIBLE);
            check.setImageResource(R.mipmap.checkmark_icon);
        } else {
            check.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
