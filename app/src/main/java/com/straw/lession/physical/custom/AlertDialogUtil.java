package com.straw.lession.physical.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.straw.lession.physical.R;

public class AlertDialogUtil {

    //一个按钮默认文字
    public static Dialog showAlertWindow (Context mContext, int icon ,  String content , OnClickListener clickListener){
        final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(R.layout.mydialog_new);
        dialog.setCancelable(false);
        ImageView alertImage =(ImageView) dialog.findViewById(R.id.alert_icon);
        if (icon == -1){
        	alertImage.setVisibility(View.GONE);
        }else {
        	alertImage.setVisibility(View.VISIBLE);
        	alertImage.setImageResource(icon);
        }
        TextView contentText =(TextView) dialog.findViewById(R.id.alert_title);
        contentText.setText(content);
        Button alert_ok_btn =(Button) dialog.findViewById(R.id.alert_ok_btn);
        if (null  == clickListener) {
            alert_ok_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else {
            alert_ok_btn.setOnClickListener(clickListener);
        }

        dialog.show();
        return dialog;
    }
    
    //一个按钮 传文字
    public static Dialog showAlertWindow (Context mContext, int icon ,  String content ,String buttontext, OnClickListener clickListener){
        final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(R.layout.mydialog_new);
        dialog.setCancelable(false);
        ImageView alertImage =(ImageView) dialog.findViewById(R.id.alert_icon);
        if (icon == -1){
        	alertImage.setVisibility(View.GONE);
        }else {
        	alertImage.setVisibility(View.VISIBLE);
        	alertImage.setImageResource(icon);
        }
        TextView contentText =(TextView) dialog.findViewById(R.id.alert_title);
        contentText.setText(content);
        Button alert_ok_btn =(Button) dialog.findViewById(R.id.alert_ok_btn);
        alert_ok_btn.setOnClickListener(clickListener);
        alert_ok_btn.setText(buttontext);
        dialog.show();
        return dialog;
    }
   
    //2个按钮
    public static Dialog showAlertWindow2Button (Context mContext,  String content , OnClickListener cancelclickListener
    		, OnClickListener okclickListener){
        final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(R.layout.mydialog_2_button);
        dialog.setCancelable(false);
        
        TextView contentText =(TextView) dialog.findViewById(R.id.alert_title);
        contentText.setText(content);
        Button alert_cancel_btn =(Button) dialog.findViewById(R.id.alert_cancel_btn);
        alert_cancel_btn.setOnClickListener(cancelclickListener);
        Button alert_ok_btn =(Button) dialog.findViewById(R.id.alert_ok_btn);
        alert_ok_btn.setOnClickListener(okclickListener);
        
        dialog.show();
        return dialog;
    }

    //2个按钮
    public static Dialog showAlertWindow2Button (Context mContext,  String content , String btn1Txt, String btn2Txt, OnClickListener cancelclickListener
            , OnClickListener okclickListener){
        final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(R.layout.mydialog_2_button);
        dialog.setCancelable(false);

        TextView contentText =(TextView) dialog.findViewById(R.id.alert_title);
        contentText.setText(content);
        Button alert_cancel_btn =(Button) dialog.findViewById(R.id.alert_cancel_btn);
        alert_cancel_btn.setText(btn1Txt);
        alert_cancel_btn.setOnClickListener(cancelclickListener);
        Button alert_ok_btn =(Button) dialog.findViewById(R.id.alert_ok_btn);
        alert_ok_btn.setText(btn2Txt);
        alert_ok_btn.setOnClickListener(okclickListener);

        dialog.show();
        return dialog;
    }

    //2个按钮
    public static Dialog showAlertVersion2Button (Context mContext,  String content , OnClickListener cancelclickListener
            , OnClickListener okclickListener){
        final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(R.layout.mydialog_3_button);
        dialog.setCancelable(false);

        TextView contentText =(TextView) dialog.findViewById(R.id.alert_title);
        contentText.setText(content);
        Button alert_cancel_btn =(Button) dialog.findViewById(R.id.alert_cancel_btn);
        alert_cancel_btn.setOnClickListener(cancelclickListener);
        Button alert_ok_btn =(Button) dialog.findViewById(R.id.alert_ok_btn);
        alert_ok_btn.setOnClickListener(okclickListener);

        dialog.show();
        return dialog;
    }

    public static Dialog showAlertVersionWindow (Context mContext, int icon ,  String content , OnClickListener clickListener){
        final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(R.layout.mydialog_version_new);
        dialog.setCancelable(false);

        TextView contentText =(TextView) dialog.findViewById(R.id.alert_title);
        contentText.setText(content);
        Button alert_ok_btn =(Button) dialog.findViewById(R.id.alert_ok_btn);
        if (null  == clickListener) {
            alert_ok_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else {
            alert_ok_btn.setOnClickListener(clickListener);
        }

        dialog.show();
        return dialog;
    }


}
