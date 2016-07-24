package com.straw.lession.physical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.straw.lession.physical.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ActionSheetTwoColumnGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater listContainer;
    private int selectedPosition = -1;
    private int numColumns ;
    private String [] monthArray;

    public class ViewHolder {
        public TextView itemText;
        public View verline;
        public View horline;
    }

    public ActionSheetTwoColumnGridAdapter(Context context , int numColumns , String [] monthArray , int selectionIndex ) {
        listContainer = LayoutInflater.from(context);
        this.numColumns = numColumns ;
        this.monthArray = monthArray;
        this.context = context;
        this.selectedPosition = selectionIndex;
    }

    int count = 0;
    public int getCount() {
        count = monthArray.length;
        return count ;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

   /* public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }*/

    public View getView(int position, View convertView, ViewGroup parent) {
        final int sign = position;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = listContainer.inflate( R.layout.view_actionsheet_grid_item, null);
            holder.itemText = (TextView) convertView.findViewById(R.id.itemText);
            holder.verline = (View) convertView.findViewById(R.id.verline);
            holder.horline = (View) convertView.findViewById(R.id.horline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ( isOdd (position) ){
            holder.verline.setVisibility(View.GONE);
            //holder.horline.setVisibility(View.VISIBLE);
        }
        if ( position >= (count - numColumns) ){
            //holder.verline.setVisibility(View.GONE);
            holder.horline.setVisibility(View.GONE);
        }

        if (position == 0 ) {
            convertView.setBackgroundResource(R.drawable.button_actionsheet_topleftround_selector);
        }else if ( position == numColumns - 1){
            convertView.setBackgroundResource(R.drawable.button_actionsheet_toprightround_selector);
        }
        else if (position == count - numColumns ){
            convertView.setBackgroundResource(R.drawable.button_actionsheet_bottomleftround_selector);
        }else if ( position == count -1){
            convertView.setBackgroundResource(R.drawable.button_actionsheet_bottomrightround_selector);
        }else {
            convertView.setBackgroundResource(R.drawable.button_actionsheet_middle_selector);
        }

        String month = monthArray[position];
        holder.itemText.setText(month);
        if (selectedPosition == position){
            holder.itemText.setTextColor(context.getResources().getColor(R.color.actionsheet_blue));
        }
        return convertView;
    }

    public static boolean isOdd(int num){
        return num % 2 == 1;
    }

}
