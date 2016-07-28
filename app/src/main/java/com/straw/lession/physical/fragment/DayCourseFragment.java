package com.straw.lession.physical.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.straw.lession.physical.R;
import com.straw.lession.physical.fragment.base.BaseFragment;

/**
 * Created by straw on 2016/7/13.
 */
public class DayCourseFragment extends BaseFragment {
    private View layoutView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.day_fragment_course, container, false);
        return layoutView;
    }
}
