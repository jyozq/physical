package com.straw.lession.physical.activity.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.crouton.Crouton;
import com.straw.lession.physical.custom.ProgressDialogFragment;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ThreadToolBarBaseActivity extends ToolBarActivity {
    private final static String TAG = "ThreadToolBarBaseActivity";
    protected MainApplication mApp;
    protected ThreadPoolExecutor mThreadPool;
    protected ProgressDialogFragment mProgressDialog;

    protected int pageNumber = 1;
    protected final static int pageCount = 20;

//    protected MessageBar mMessageBar = null;

    private View viewTip ;


    //this, getContext(), getApplicationgContext()；希望能帮到你
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mApp = (MainApplication) mContext.getApplication();
        mThreadPool = mApp.getThreadPool();
        viewTip = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showProgressDialog(String msg){
        hideProgressDialog();
        mProgressDialog = ProgressDialogFragment.getInstance(ProgressDialogFragment.DEFAULT, msg);
        //mProgressDialog.show(getFragmentManager(), null);
        mProgressDialog.show(getSupportFragmentManager() , null);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismissAllowingStateLoss();
            //mProgressDialog.dismiss();
        }
    }

    /*public void showErrorMsgInfo(String errorContent){
        if (null == mMessageBar){
            mMessageBar = new MessageBar(mContext);
        }
        mMessageBar.show(errorContent);
    }*/

    /*public void showErrorMsgInfo(String errmsg) {
        View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        TextView textView =(TextView) view.findViewById(R.id.errorText);
        textView.setText(errmsg);
        final Crouton crouton;
        crouton = Crouton.make(mContext, view);
        crouton.show();
    }*/


    public void showErrorMsgInfo(String errmsg) {
        //View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        TextView textView =(TextView) viewTip.findViewById(R.id.errorText);
        textView.setText(errmsg);
        final Crouton crouton;
        crouton = Crouton.make(mContext, viewTip);
        crouton.show();
    }
}
