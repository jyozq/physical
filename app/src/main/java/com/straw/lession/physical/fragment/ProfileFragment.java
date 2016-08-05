package com.straw.lession.physical.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.LoginActivity;
import com.straw.lession.physical.activity.ResetPassWordActivity;
import com.straw.lession.physical.activity.UploadDataActivity;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.vo.db.Course;
import com.straw.lession.physical.vo.db.Institute;

import java.io.IOException;
import java.util.List;

/**
 * Created by straw on 2016/7/7.
 */
public class ProfileFragment extends BaseFragment{
    private static final String TAG = "ProfileFragment";
    private View layoutView;
    private TextView profile_upload;
    private TextView profile_resetPwdText;
    private Button profile_exit;
    private TextView profile_name;
    private TextView profile_institute;
    private ImageView has_unupload_flag;
    private MyClickListener listener = new MyClickListener();
    private boolean hasUnuploadData;
    private Dialog dialog;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        }else{
            myinit();
        }
    }

    private void myinit() {
        getLoginAndToken();
        Institute institute = DBService.getInstance(getContext()).findInstituteById(loginInfo.getCurrentInstituteIdR());
        profile_name.setText(loginInfo.getPersonName());
        profile_institute.setText(institute.getName());

        List<Course> unUploadDatas = DBService.getInstance(getContext()).getUnUploadedData(loginInfo.getUserId());
        hasUnuploadData = Detect.notEmpty(unUploadDatas);
        has_unupload_flag.setVisibility(hasUnuploadData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        myinit();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_profile, container, false);
        return layoutView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        profile_name = (TextView)layoutView.findViewById(R.id.profile_name);
        profile_institute = (TextView)layoutView.findViewById(R.id.profile_institute);
//        Institute institute = DBService.getInstance(getContext()).findInstituteById(loginInfo.getCurrentInstituteIdR());
//        profile_institute.setText(institute.getName());

        profile_upload = (TextView) layoutView.findViewById(R.id.profile_upload);
        profile_resetPwdText = (TextView) layoutView.findViewById(R.id.profile_resetPwdText);
        profile_exit = (Button) layoutView.findViewById(R.id.profile_exit);
        has_unupload_flag = (ImageView) layoutView.findViewById(R.id.has_unupload_flag);
        profile_upload.setOnClickListener(listener);
        profile_resetPwdText.setOnClickListener(listener);
        profile_exit.setOnClickListener(listener);
    }

    @Override
    protected void loadDataFromLocal() {

    }

    @Override
    protected void loadDataFromService() {

    }

    @Override
    public void doAfterGetToken() {

    }

    public class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.profile_upload:
                    startActivity(new Intent(ProfileFragment.this.getActivity(),UploadDataActivity.class));
                    break;
                case R.id.profile_resetPwdText:
                    startActivity(new Intent(ProfileFragment.this.getActivity(), ResetPassWordActivity.class));
                    break;
                case R.id.profile_exit:
                    String content = null;
                    final String btn1Txt;
                    String btn2Txt = null;
                    if(hasUnuploadData){
                        content = "您有未上传的课程数据，请确认所有课程都上传完成。";
                        btn1Txt = "立刻上传";
                        btn2Txt = "登出账户";
                    }else{
                        content = "请确认是否退出？";
                        btn1Txt = "取消";
                        btn2Txt = "确定";
                    }

                    dialog = AlertDialogUtil.showAlertWindow2Button(getContext(), content, btn1Txt, btn2Txt, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if(btn1Txt.equals("立刻上传")){
                                startActivity(new Intent(ProfileFragment.this.getActivity(),UploadDataActivity.class));
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                AppPreference.logout();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, "", e);
                                Toast.makeText(getActivity(),"登出出错，请重试！",Toast.LENGTH_LONG).show();
                                return;
                            }
                            dialog.dismiss();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    });
                    break;
            }
        }
    }
}
