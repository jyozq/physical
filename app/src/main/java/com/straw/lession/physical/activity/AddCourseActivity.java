package com.straw.lession.physical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.ActionSheetTwoColumnGridDialog;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.utils.ResponseParseUtils;
import com.straw.lession.physical.utils.Utils;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;
import com.straw.lession.physical.vo.item.SelClassItemInfo;

import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.greendao.query.Query;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by straw on 2016/7/11.
 */
public class AddCourseActivity extends ThreadToolBarBaseActivity{
    private static final String TAG = "AddCourseActivity";
    private final int RESULT_CODE=101;
    private final int REQUEST_CODE=1;
    private ActionSheetTwoColumnGridDialog actionSheetGridDialog;
    private EditText dateTxt, weekdayTxt, seqTxt, classTxt, kcTxt, typeTxt, locationTxt;
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
    private Long selectClassId;
    private Button addcourse_save_btn;
    private boolean useOnce;
    private CourseDefineItemInfo courseDefineItemInfo;
    private LinearLayout dateField, weekdayField;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_course);
        getLoginAndToken();
        useOnce = getIntent().getBooleanExtra("useOnce", true);
        courseDefineItemInfo = (CourseDefineItemInfo)getIntent().getSerializableExtra("courseDefine");
        if(courseDefineItemInfo.getCourseDefineId() > 0){
            initToolBar("编辑"+(useOnce?"临时":"常规")+"课程");
        }else {
            initToolBar("新增"+(useOnce?"临时":"常规")+"课程");
        }
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {

    }

    @Override
    public void doAfterGetToken() {
        super.doAfterGetToken();
        try {
            saveCourse();
        }catch (Exception ex){
            AlertDialogUtil.showAlertWindow(this,-1,"保存失败",null);
        }
    }

    private void initViews() {
        dateTxt = (EditText) findViewById(R.id.course_date);
        weekdayTxt = (EditText) findViewById(R.id.course_weekday);
        seqTxt = (EditText) findViewById(R.id.course_seq);
        classTxt = (EditText) findViewById(R.id.course_class);
        kcTxt = (EditText) findViewById(R.id.course_kc);
        typeTxt = (EditText) findViewById(R.id.course_type);
        locationTxt = (EditText) findViewById(R.id.course_location);
        addcourse_save_btn = (Button) findViewById(R.id.addcourse_save_btn);
        dateField = (LinearLayout) findViewById(R.id.course_date_field);
        weekdayField = (LinearLayout) findViewById(R.id.course_weekday_field);

        if(useOnce){
            dateField.setVisibility(View.VISIBLE);
            weekdayField.setVisibility(View.GONE);
        }else{
            weekdayField.setVisibility(View.VISIBLE);
            dateField.setVisibility(View.GONE);
        }

        dateTxt.setOnClickListener(listener);
        weekdayTxt.setOnClickListener(listener);
        seqTxt.setOnClickListener(listener);
        classTxt.setOnClickListener(listener);
        typeTxt.setOnClickListener(listener);
        addcourse_save_btn.setOnClickListener(listener);

        dateTxt.setInputType(InputType.TYPE_NULL);
        weekdayTxt.setInputType(InputType.TYPE_NULL);
        seqTxt.setInputType(InputType.TYPE_NULL);
        classTxt.setInputType(InputType.TYPE_NULL);
        typeTxt.setInputType(InputType.TYPE_NULL);
        dateTxt.setFocusable(false);
        dateTxt.setFocusableInTouchMode(false);
        weekdayTxt.setFocusable(false);
        weekdayTxt.setFocusableInTouchMode(false);
        seqTxt.setFocusable(false);
        seqTxt.setFocusableInTouchMode(false);
        classTxt.setFocusable(false);
        classTxt.setFocusableInTouchMode(false);
        typeTxt.setFocusable(false);
        typeTxt.setFocusableInTouchMode(false);

        weekdayArr = getResources().getStringArray(R.array.weekday);
        weekday_val_arr = getResources().getIntArray(R.array.weekday_value);
        seqArr = getResources().getStringArray(R.array.seq);
        seq_val_arr = getResources().getIntArray(R.array.seq_value);
        typeArr = getResources().getStringArray(R.array.course_type);
        type_val_arr = getResources().getIntArray(R.array.course_type_val);

        initVal();
    }

    private void initVal() {
        if(courseDefineItemInfo != null){
            if(useOnce){
                dateTxt.setText(courseDefineItemInfo.getDate());
                if(Detect.notEmpty(courseDefineItemInfo.getDate())){
                    dateTxt.setOnClickListener(null);
                }
            }else{
                if(courseDefineItemInfo.getWeekDay() >= 0){
                    int pos = getWeekdayPos(courseDefineItemInfo.getWeekDay());
                    weekdayTxt.setText(weekdayArr[pos]);
                    weekdayTxt.setTag(courseDefineItemInfo.getWeekDay());
                    weekdayTxt.setOnClickListener(null);
                    weekdaySelectionIndex = pos;
                }
            }
            if(courseDefineItemInfo.getSeq() > 0) {
                int pos = getSeqPos(courseDefineItemInfo.getSeq());
                seqTxt.setText(seqArr[pos]);
                seqTxt.setTag(courseDefineItemInfo.getSeq());
                seqTxt.setOnClickListener(null);
                seqSelectionIndex = pos;
            }
            classTxt.setText(courseDefineItemInfo.getClassName());
            selectClassId = courseDefineItemInfo.getClassId();
            kcTxt.setText(courseDefineItemInfo.getName());
            typeTxt.setText(courseDefineItemInfo.getType());
            locationTxt.setText(courseDefineItemInfo.getLocation());
        }
    }

    private int getSeqPos(int seq) {
        for(int i= 0; i< seq_val_arr.length; i ++){
            if(seq == seq_val_arr[i]){
                return i;
            }
        }
        return 0;
    }

    private int getWeekdayPos(int weekDay) {
        for(int i= 0; i< weekday_val_arr.length; i ++){
            if(weekDay == weekday_val_arr[i]){
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE) {
            if(resultCode==RESULT_CODE) {
                SelClassItemInfo selClassItemInfo = (SelClassItemInfo) data.getSerializableExtra("classInfo");
                classTxt.setText(selClassItemInfo.getClassName());
                selectClassId = selClassItemInfo.getClassId();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private AdapterView.OnItemClickListener onWeekdayItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String weekday = weekdayArr[position];
            int weekday_value = weekday_val_arr[position];
            weekdayTxt.setText(weekday);
            weekdayTxt.setTag(weekday_value);
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
            seqTxt.setTag(seq_value);
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
                case R.id.course_date:
                    final AlertDialog dialog = new AlertDialog.Builder(AddCourseActivity.this).create();
                    dialog.show();
                    DatePicker picker = new DatePicker(AddCourseActivity.this);
                    String dateStr = courseDefineItemInfo.getDate();
                    if(!Detect.notEmpty(dateStr)){
                        dateStr = DateUtil.dateToStr(new Date());
                    }
                    String year = dateStr.substring(0,4);
                    String month = dateStr.substring(5,7);
                    picker.setDate(Integer.parseInt(year), Integer.parseInt(month));
                    picker.setMode(DPMode.SINGLE);
                    picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                        @Override
                        public void onDatePicked(String date) {
                            dateTxt.setText(date);
                            dialog.dismiss();
                        }
                    });
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setContentView(picker, params);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    break;
                case R.id.course_weekday:
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
                            onTypeItemClickListener, typeSelectionIndex)
                            .builder() .setCancelable(true) .setCanceledOnTouchOutside(true);
                    actionSheetGridDialog.show();
                    break;
                case R.id.addcourse_save_btn:
                    getLoginAndToken();
                    checkTokenInfo();
                    break;
            }
        }
    }

    private void saveCourse() {
        String dateStr = null;
        int weekday = -1;
        int seq = -1;
        String type = null;
        String courseName = null;
        String location = null;
        final CourseDefine courseDefine;
        if(courseDefineItemInfo.getCourseDefineId() > 0) {
            courseDefine = DBService.getInstance(this).findCourseDefineById(courseDefineItemInfo.getCourseDefineId());
        }else{
            courseDefine = new CourseDefine();
        }
        if(useOnce) {
            if (dateTxt.getText() == null) {
                Toast.makeText(mContext, "请选择日期", Toast.LENGTH_LONG).show();
                return;
            } else {
                dateStr = dateTxt.getText().toString();
                courseDefine.setDate(DateUtil.formatStrToDate(dateStr));
                courseDefine.setWeekDay(DateUtil.dayForWeek(dateStr));
            }
        }else {
            if (weekdayTxt.getText() == null) {
                Toast.makeText(mContext, "请选择时间", Toast.LENGTH_LONG).show();
                return;
            } else {
                weekday = (Integer)weekdayTxt.getTag();
                courseDefine.setWeekDay(weekday);
            }
        }
        if(seqTxt.getText() == null){
            Toast.makeText(mContext, "请选择班次" , Toast.LENGTH_LONG).show();
            return;
        }else{
            seq = (Integer)seqTxt.getTag();
            courseDefine.setSeq(seq);
        }
        if(classTxt.getText() == null){
            Toast.makeText(mContext, "请选择班级" , Toast.LENGTH_LONG).show();
            return;
        }else{
            courseDefine.setClassIdR(selectClassId);
        }
        if(kcTxt.getText() == null){
            Toast.makeText(mContext, "请输入课程名称" , Toast.LENGTH_LONG).show();
            return;
        }else {
            courseName = kcTxt.getText().toString();
            courseDefine.setName(courseName);
        }
        if(typeTxt.getText() == null){
            Toast.makeText(mContext, "请选择活动类型" , Toast.LENGTH_LONG).show();
            return;
        }else{
            type = typeTxt.getText().toString();
            courseDefine.setType(type);
        }
        if(locationTxt.getText() == null){
            Toast.makeText(mContext, "请输入上课地点" , Toast.LENGTH_LONG).show();
            return;
        }else{
            location = locationTxt.getText().toString();
            courseDefine.setLocation(location);
        }
        LoginInfoVo loginInfo = null;
        TokenInfo tokenInfo = null;
        try {
            loginInfo = AppPreference.getLoginInfo();
            tokenInfo = AppPreference.getUserToken();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMsgInfo(e.toString());
            return;
        }
        courseDefine.setInstituteIdR(loginInfo.getCurrentInstituteIdR());
        courseDefine.setTeacherIdR(loginInfo.getUserId());
        courseDefine.setUseOnce(useOnce? CommonConstants.UseOnce.USE_ONCE.getValue()
                                        :CommonConstants.UseOnce.USE_ONCE_NOT.getValue());

        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("courseType", courseDefine.getType()));
        params.add(new BasicNameValuePair("courseName", courseDefine.getName()));
        params.add(new BasicNameValuePair("instituteId", String.valueOf(courseDefine.getInstituteIdR())));
        params.add(new BasicNameValuePair("classId", String.valueOf(courseDefine.getClassIdR())));
        params.add(new BasicNameValuePair("weekday", courseDefine.getWeekDay()==null?null:String.valueOf(courseDefine.getWeekDay())));
        params.add(new BasicNameValuePair("courseDate", dateStr));
        params.add(new BasicNameValuePair("courseSeq", String.valueOf(courseDefine.getSeq())));
        params.add(new BasicNameValuePair("courseLocation", courseDefine.getLocation()));
        params.add(new BasicNameValuePair("useOnce", String.valueOf(courseDefine.getUseOnce())));

        if(courseDefineItemInfo.getCourseDefineId() > 0){
            courseDefine.setCourseDefineIdR(courseDefineItemInfo.getCourseDefineId());
            params.add(new BasicNameValuePair("courseDefineId", String.valueOf(courseDefine.getCourseDefineIdR())));
            updateCourseDefine(courseDefine, params, loginInfo,tokenInfo);
        }else{
            courseDefine.setIsDel(0);
            createCourseDefine(courseDefine, params, loginInfo,tokenInfo);
        }
    }

    private void updateCourseDefine(final CourseDefine courseDefine, ArrayList<BasicNameValuePair> params, LoginInfoVo loginInfo, TokenInfo tokenInfo) {
        final String URL = ReqConstant.URL_BASE + "/course/define/update";

        showProgressDialog(getResources().getString(R.string.loading));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL ,params , tokenInfo.getToken(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
                        DBService.getInstance(AddCourseActivity.this).updateCourseDefine(courseDefine);
                        Toast.makeText(mContext, "保存成功" , Toast.LENGTH_LONG).show();
                        MainApplication.getInstance().popCurrentActivity();
                    }else {
                        String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
                    }
                }catch(Exception e){
                    hideProgressDialog();
                    showErrorMsgInfo(e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                hideProgressDialog();
                String errorContent = Utils.parseErrorMessage(mContext, content);
                showErrorMsgInfo(errorContent);
                Log.e(TAG, content);
            }
        });
        mThreadPool.execute(asyncHttpClient);
    }


    private void createCourseDefine(final CourseDefine courseDefine, ArrayList<BasicNameValuePair> params, LoginInfoVo loginInfo, TokenInfo tokenInfo) {
        final String URL = ReqConstant.URL_BASE + "/course/define/create";

        showProgressDialog(getResources().getString(R.string.loading));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL ,params , tokenInfo.getToken(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    String resultCode = contentObject.getString(ParamConstant.RESULT_CODE);
                    if (resultCode.equals(ResponseParseUtils.RESULT_CODE_SUCCESS) ){
                        JSONObject dataObj = contentObject.getJSONObject(ParamConstant.RESULT_DATA);
                        String courseDefineId = dataObj.getString("courseDefineId");
                        courseDefine.setCourseDefineIdR(Long.parseLong(courseDefineId));
                        DBService.getInstance(AddCourseActivity.this).addCourseDefine(courseDefine);
                        Toast.makeText(mContext, "保存成功" , Toast.LENGTH_LONG).show();
                        MainApplication.getInstance().popCurrentActivity();
                    }else {
                        String errorMessage = contentObject.getString(ParamConstant.RESULT_MSG);
                        AlertDialogUtil.showAlertWindow(mContext, -1, errorMessage , null );
                    }
                }catch(Exception e){
                    hideProgressDialog();
                    showErrorMsgInfo(e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                hideProgressDialog();
                String errorContent = Utils.parseErrorMessage(mContext, content);
                showErrorMsgInfo(errorContent);
                Log.e(TAG, content);
            }
        });
        mThreadPool.execute(asyncHttpClient);
    }
}
