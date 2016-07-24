package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.custom.ActionSheetTwoColumnGridDialog;
import com.straw.lession.physical.vo.LoginInfo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.item.ClassItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/11.
 */
public class AddCourseActivity extends ThreadToolBarBaseActivity{
    private static final String TAG = "AddCourseActivity";
    private final int RESULT_CODE=101;
    private final int REQUEST_CODE=1;
    private LoginInfo loginInfo;
    private ActionSheetTwoColumnGridDialog actionSheetGridDialog;
    private EditText timeTxt, seqTxt, classTxt, kcTxt, typeTxt;
    private MyListener listener = new MyListener();
    private int numColumns = 2;
    private int weekdaySelectionIndex = -1, seqSelectionIndex = -1, typeSelectionIndex = -1;
    private String[] weekdayArr;
    private int[] weekday_val_arr;
    private String[] seqArr;
    private int[] seq_val_arr;
    private String[] typeArr;
    private int[] type_val_arr;
    private List<ClassInfo> classInfos = new ArrayList<ClassInfo>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_course);
        initToolBar("");
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void initViews() {
        timeTxt = (EditText) findViewById(R.id.course_time);
        seqTxt = (EditText) findViewById(R.id.course_seq);
        classTxt = (EditText) findViewById(R.id.course_class);
        kcTxt = (EditText) findViewById(R.id.course_kc);
        typeTxt = (EditText) findViewById(R.id.course_type);
        timeTxt.setOnClickListener(listener);
        seqTxt.setOnClickListener(listener);
        classTxt.setOnClickListener(listener);
        typeTxt.setOnClickListener(listener);

        timeTxt.setInputType(InputType.TYPE_NULL);
        seqTxt.setInputType(InputType.TYPE_NULL);
        classTxt.setInputType(InputType.TYPE_NULL);
        typeTxt.setInputType(InputType.TYPE_NULL);

        weekdayArr = getResources().getStringArray(R.array.weekday);
        weekday_val_arr = getResources().getIntArray(R.array.weekday_value);
        seqArr = getResources().getStringArray(R.array.seq);
        seq_val_arr = getResources().getIntArray(R.array.seq);
        typeArr = getResources().getStringArray(R.array.course_type);
        type_val_arr = getResources().getIntArray(R.array.course_type_val);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE) {
            if(resultCode==RESULT_CODE) {
                ClassItemInfo classItemInfo = (ClassItemInfo) data.getSerializableExtra("classInfo");
                classTxt.setText(classItemInfo.getClassName());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private AdapterView.OnItemClickListener onWeekdayItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String weekday = weekdayArr[position];
            int weekday_value = weekday_val_arr[position];
            timeTxt.setText(weekday);
            timeTxt.setTag(String.valueOf(weekday_value));
            weekdaySelectionIndex = position;
            actionSheetGridDialog.dismiss();
        }
    };

    private AdapterView.OnItemClickListener onSeqItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String seq = seqArr[position];
            int seq_value = seq_val_arr[position];
            seqTxt.setText(seq);
            seqTxt.setTag(String.valueOf(seq_value));
            seqSelectionIndex = position;
            actionSheetGridDialog.dismiss();
        }
    };

    private AdapterView.OnItemClickListener onTypeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String type = typeArr[position];
            int type_value = type_val_arr[position];
            typeTxt.setText(type);
            typeTxt.setTag(String.valueOf(type_value));
            typeSelectionIndex = position;
            actionSheetGridDialog.dismiss();
        }
    };

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.course_time:
                    actionSheetGridDialog = new ActionSheetTwoColumnGridDialog(
                            AddCourseActivity.this, numColumns , weekdayArr ,
                            onWeekdayItemClickListener, weekdaySelectionIndex)
                            .builder() .setCancelable(true) .setCanceledOnTouchOutside(true);
                    actionSheetGridDialog.show();
                    break;
                case R.id.course_seq:
                    actionSheetGridDialog = new ActionSheetTwoColumnGridDialog(
                            AddCourseActivity.this, numColumns , seqArr ,
                            onSeqItemClickListener, seqSelectionIndex)
                            .builder() .setCancelable(true) .setCanceledOnTouchOutside(true);
                    actionSheetGridDialog.show();
                    break;
                case R.id.course_class:
                    Intent intent=new Intent(AddCourseActivity.this,SelectClassActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.course_type:
                    actionSheetGridDialog = new ActionSheetTwoColumnGridDialog(
                            AddCourseActivity.this, numColumns , typeArr ,
                            onSeqItemClickListener, typeSelectionIndex)
                            .builder() .setCancelable(true) .setCanceledOnTouchOutside(true);
                    actionSheetGridDialog.show();
                    break;
            }
        }
    }
}
