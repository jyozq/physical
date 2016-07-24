package com.straw.lession.physical.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.MainActivity;
import com.straw.lession.physical.fragment.base.BaseFragment;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.vo.LoginInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by straw on 2016/7/7.
 */
public class ProfileFragment extends BaseFragment{
    private static final String TAG = "ProfileFragment";
    private View layoutView;
    private LoginInfo loginInfo;

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
        TextView profile_institute = (TextView)layoutView.findViewById(R.id.profile_institute);
        profile_name.setText(loginInfo.getPersonName());
        List<LoginInfo.Institute> institutes = loginInfo.getInstitutes();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < institutes.size(); i ++){
            if( i == institutes.size() - 1) {
                sb.append(institutes.get(i).getInsName());
            }else{
                sb.append(institutes.get(i).getInsName()).append(",");
            }
        }
        profile_institute.setText(sb.toString());
    }
}
