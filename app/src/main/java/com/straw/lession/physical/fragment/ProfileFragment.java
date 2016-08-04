package com.straw.lession.physical.fragment;

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
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.Institute;

import java.io.IOException;
import java.util.List;

/**
 * Created by straw on 2016/7/7.
 */
public class ProfileFragment extends BaseFragment{
    private static final String TAG = "ProfileFragment";
    private View layoutView;
    private LoginInfoVo loginInfo;
    private TextView profile_upload;
    private TextView profile_resetPwdText;
    private Button profile_exit;
    private TextView profile_institute;
    private MyClickListener listener = new MyClickListener();

    @Override
    public void onResume() {
        super.onResume();
        Institute institute = DBService.getInstance(getContext()).findInstituteById(loginInfo.getCurrentInstituteIdR());
        profile_institute.setText(institute.getName());
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
        try {
            loginInfo = AppPreference.getLoginInfo();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"",e);
            return;
        }

        TextView profile_name = (TextView)layoutView.findViewById(R.id.profile_name);
        profile_institute = (TextView)layoutView.findViewById(R.id.profile_institute);
        profile_name.setText(loginInfo.getPersonName());
        Institute institute = DBService.getInstance(getContext()).findInstituteById(loginInfo.getCurrentInstituteIdR());
        profile_institute.setText(institute.getName());

        profile_upload = (TextView) layoutView.findViewById(R.id.profile_upload);
        profile_resetPwdText = (TextView) layoutView.findViewById(R.id.profile_resetPwdText);
        profile_exit = (Button) layoutView.findViewById(R.id.profile_exit);
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
                    try {
                        AppPreference.logout();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "", e);
                        Toast.makeText(getActivity(),"登出出错，请重试！",Toast.LENGTH_LONG).show();
                        return;
                    }
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    MainApplication.getInstance().exit();
                    break;
            }
        }
    }

    public void toggleUploadNotificationFlag(boolean isShow){
        ImageView notifyFlag = (ImageView)getActivity().findViewById(R.id.has_unupload_flag);
        notifyFlag.setVisibility(isShow?View.VISIBLE:View.GONE);
    }
}
