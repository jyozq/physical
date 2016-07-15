package com.straw.lession.physical.async;

import android.os.Handler;

/**
 * Created by straw on 2016/7/15.
 */
public class CreateDBTaskHandler implements ITaskHandler{
    private Handler mHandler;
    public CreateDBTaskHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void handle() {
        mHandler.sendEmptyMessageDelayed(0, 0);
    }
}
