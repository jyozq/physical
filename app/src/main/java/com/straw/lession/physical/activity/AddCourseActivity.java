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
import android.widget.Toast;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadToolBarBaseActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.ActionSheetTwoColumnGridDialog;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.ClassInfoDao;
import com.straw.lession.physical.db.CourseDefineDao;
import com.straw.lession.physical.db.DaoSession;
import com.straw.lession.physical.dictionary.CourseDictionary;
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
import com.straw.lession.physical.vo.item.SelClassItemInfo;

import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.greendao.query.Query;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/11.
 */
public class AddCourseActivity extends ThreadToolBarBaseActivity{
    private static final String TAG = "AddCourseActivity";
    private final int RESULT_CODE=101;
    private final int REQUEST_CODE=1;
    private LoginInfoVo loginInfo;
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

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_course);
        initToolBar(getResources().getString(R.string.toolbar_new_class));
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
        saveCourse();
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

        weekdayArr = getResources().getStringArray(R.array.weekday);
        weekday_val_arr = getResources().getIntArray(R.array.weekday_value);
        seqArr = getResources().getStringArray(R.array.seq);
        seq_val_arr = getResources().getIntArray(R.array.seq_value);
        typeArr = getResources().getStringArray(R.array.course_type);
        type_val_arr = getResources().getIntArray(R.array.course_type_val);
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
            weekdayTxt.setTag(String.valueOf(weekday_value));
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
                case R.id.course_date:
                    final AlertDialog dialog = new AlertDialog.Builder(AddCourseActivity.this).create();
                    dialog.show();
                    DatePicker picker = new DatePicker(AddCourseActivity.this);
                    picker.setDate(2015, 10);
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
        final CourseDefine courseDefine = new CourseDefine();
        if(dateTxt.getText() == null){
            Toast.makeText(mContext, "请选择日期" , Toast.LENGTH_LONG).show();
            return;
        }else{
            dateStr = dateTxt.getText().toString();
            courseDefine.setDate(DateUtil.formatStrToDate(dateStr));
        }
        if(weekdayTxt.getTag() == null){
            Toast.makeText(mContext, "请选择时间" , Toast.LENGTH_LONG).show();
            return;
        }else{
            weekday = Integer.parseInt((String)weekdayTxt.getTag());
            courseDefine.setWeekDay(weekday);
        }
        if(seqTxt.getTag() == null){
            Toast.makeText(mContext, "请选择班次" , Toast.LENGTH_LONG).show();
            return;
        }else{
            seq = Integer.parseInt((String)seqTxt.getTag());
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
        if(typeTxt.getTag() == null){
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
        courseDefine.setInstituteId(loginInfo.getCurrentInstituteId());
        courseDefine.setInstituteIdR(loginInfo.getCurrentInstituteIdR());
        courseDefine.setTeacherId(loginInfo.getUserId());
        courseDefine.setLoginId(loginInfo.getTeacherId());
        courseDefine.setUseOnce(CourseDictionary.USE_ONCE);

        final String URL = ReqConstant.URL_BASE + "/course/define/create";
        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("courseType", String.valueOf(type)));
        params.add(new BasicNameValuePair("courseName", String.valueOf(courseName)));
        params.add(new BasicNameValuePair("instituteId", String.valueOf(loginInfo.getCurrentInstituteIdR())));
        params.add(new BasicNameValuePair("classId", String.valueOf(selectClassId)));
        params.add(new BasicNameValuePair("weekday", String.valueOf(weekday)));
        params.add(new BasicNameValuePair("courseDate", dateStr));
        params.add(new BasicNameValuePair("courseSeq", String.valueOf(seq)));
        params.add(new BasicNameValuePair("courseLocation", location));
        params.add(new BasicNameValuePair("useOnce", String.valueOf(CourseDictionary.USE_ONCE)));
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

                        DaoSession daoSession = MainApplication.getInstance()
                                                .getDaoSession(AddCourseActivity.this);
                        ClassInfoDao classInfoDao = daoSession.getClassInfoDao();
                        Query query = classInfoDao.queryBuilder()
                                .where(ClassInfoDao.Properties.ClassIdR.eq(courseDefine.getClassIdR()))
                                .build();
                        List<ClassInfo> classInfos = query.list();
                        if(Detect.notEmpty(classInfos)){
                            courseDefine.setClassId(classInfos.get(0).getId());
                        }

                        CourseDefineDao courseDefineDao = daoSession.getCourseDefineDao();
                        courseDefineDao.insert(courseDefine);

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
