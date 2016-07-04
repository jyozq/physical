package com.straw.lession.physical.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.crouton.Crouton;
import com.straw.lession.physical.custom.ProgressDialogFragment;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.utils.ResponseParseUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by straw on 2016/7/2.
 */
public class ThreadBaseActivity extends AppCompatActivity {
    private final static String TAG = "ThreadBaseActivity";
    protected Activity mContext = ThreadBaseActivity.this;
    protected MainApplication mApp;
    protected ThreadPoolExecutor mThreadPool;
    protected ProgressDialogFragment mProgressDialog;

    protected int pageNumber = 1;
    protected final static int pageCount = 20;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mApp = (MainApplication) mContext.getApplication();
        mThreadPool = mApp.getThreadPool();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         */
        //MobCreditEase.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         */
        //MobCreditEase.onResume(this);
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


    public void isShowProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isHidden()){
            }else {
            }
            if (mProgressDialog.isVisible()){
            }else{
            }
        }
    }

    /*public void showErrorMsgInfo(String errorContent){
        if (null == mMessageBar){
            mMessageBar = new MessageBar(mContext);
        }
        mMessageBar.show(errorContent);
    }*/


    public void showErrorMsgInfo(String errmsg) {
        View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        TextView textView =(TextView) view.findViewById(R.id.errorText);
        textView.setText(errmsg);
        final Crouton crouton;
        //if (displayOnTop.isChecked()) {

        //} else {
        //   crouton = Crouton.make(mContext, view, R.id.alternate_view_group);
        //}

        crouton = Crouton.make(mContext, view);
        crouton.show();
    }



    //字典
    protected void sxdOrderQueryDictionaryRequest(){
        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("model", "Order"  ));
        params.add(new BasicNameValuePair("column", ""  ));
        final String METHOD = "sxdcommon.query.dictionary";
        final String URL = ReqConstant.URL_BASE +"?method=" + METHOD  ;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(AsyncHttpClient.RequestType.POST, URL ,params , new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(HttpResponseBean httpResponseBean) {
                super.onSuccess(httpResponseBean);
                try{
                    //hideProgressDialog();
                    JSONObject contentObject = new JSONObject(httpResponseBean.content);
                    boolean success = contentObject.getBoolean(ResponseParseUtils.success);
                    if (success ){
                        if (!contentObject.isNull("data")){
//                            JSONObject dataObject = contentObject.getJSONObject("data");
//                            if (dataObject.has("MARRIAGESTATUS")){
//                                if (!dataObject.isNull("MARRIAGESTATUS")){
//                                    JSONArray marriage_status_array = dataObject.getJSONArray("MARRIAGESTATUS");
//                                    if (null !=marriage_status_array && marriage_status_array.length() > 0){
//                                        MainApplication.getInstance().marriage_status_label = new String [marriage_status_array.length()];
//                                        MainApplication.getInstance().marriage_status_value = new String [marriage_status_array.length()];
//                                        for (int i =0 ; i< marriage_status_array.length() ; i++){
//                                            JSONObject marriage_status_object = marriage_status_array.getJSONObject(i);
//                                            String namecn = marriage_status_object.getString("namecn");
//                                            int value = marriage_status_object.getInt("value");
//                                            MainApplication.getInstance().marriage_status_label[i] = namecn;
//                                            MainApplication.getInstance().marriage_status_value[i] = String.valueOf(value ) ;
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (dataObject.has("CHILDRENSTATUS")){
//                                if (!dataObject.isNull("CHILDRENSTATUS")){
//                                    JSONArray children_status_array = dataObject.getJSONArray("CHILDRENSTATUS");
//                                    if (null !=children_status_array && children_status_array.length() > 0){
//                                        MainApplication.getInstance().children_status_label = new String [children_status_array.length()];
//                                        MainApplication.getInstance().children_status_value = new String [children_status_array.length()];
//                                        for (int i=0 ; i < children_status_array.length() ; i++){
//                                            JSONObject children_status_object = children_status_array.getJSONObject(i);
//                                            String namecn = children_status_object.getString("namecn");
//                                            int value = children_status_object.getInt("value");
//                                            MainApplication.getInstance().children_status_label[i] = namecn;
//                                            MainApplication.getInstance().children_status_value[i] = String.valueOf(value);
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (dataObject.has("ISCENTRALPURCHASE")){
//                                if (!dataObject.isNull("ISCENTRALPURCHASE")){
//                                    JSONArray is_central_purchase_array = dataObject.getJSONArray("ISCENTRALPURCHASE");
//                                    if (null !=is_central_purchase_array && is_central_purchase_array.length() > 0){
//                                        MainApplication.getInstance().is_central_purchase_label = new String [is_central_purchase_array.length()];
//                                        MainApplication.getInstance().is_central_purchase_value = new String [is_central_purchase_array.length()];
//                                        for (int i =0 ; i<is_central_purchase_array.length() ; i++){
//                                            JSONObject is_central_purchase_object = is_central_purchase_array.getJSONObject(i);
//                                            String namecn = is_central_purchase_object.getString("namecn");
//                                            int value = is_central_purchase_object.getInt("value");
//
//                                            MainApplication.getInstance().is_central_purchase_label[i] = namecn;
//                                            MainApplication.getInstance().is_central_purchase_value[i] = String.valueOf(value);
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (dataObject.has("HASHOUSE")){
//                                if (!dataObject.isNull("HASHOUSE")){
//                                    JSONArray has_house_array = dataObject.getJSONArray("HASHOUSE");
//                                    if (null != has_house_array && has_house_array.length() > 0){
//                                        MainApplication.getInstance().has_house_label = new String [has_house_array.length()];
//                                        MainApplication.getInstance().has_house_value = new String [has_house_array.length()];
//                                        for (int i=0 ; i< has_house_array.length() ; i++ ){
//                                            JSONObject has_house_object = has_house_array.getJSONObject(i);
//                                            String namecn = has_house_object.getString("namecn");
//                                            int value = has_house_object.getInt("value");
//                                            MainApplication.getInstance().has_house_label[i] =  namecn;
//                                            MainApplication.getInstance().has_house_value[i] = String.valueOf(value);
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (dataObject.has("OPERATETYPE")){
//                                if (!dataObject.isNull("OPERATETYPE")){
//                                    JSONArray main_business_array = dataObject.getJSONArray("OPERATETYPE");
//                                    if (null !=main_business_array && main_business_array.length() > 0 ){
//                                        MainApplication.getInstance().operate_type_label = new String [main_business_array.length()];
//                                        MainApplication.getInstance().operate_type_value = new String [main_business_array.length()];
//                                        for (int i =0 ; i< main_business_array.length() ; i++ ){
//                                            JSONObject main_business_object = main_business_array.getJSONObject(i);
//                                            String namecn = main_business_object.getString("namecn");
//                                            int value= main_business_object.getInt("value");
//                                            MainApplication.getInstance().operate_type_label[i] = namecn;
//                                            MainApplication.getInstance().operate_type_value[i] = String.valueOf(value);
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (dataObject.has("RELATIONTYPE")){
//                                if (!dataObject.isNull("RELATIONTYPE")){
//                                    JSONArray relation_type_array = dataObject.getJSONArray("RELATIONTYPE");
//                                    if (null !=relation_type_array && relation_type_array.length() > 0){
//                                        MainApplication.getInstance().relation_type_label = new String [relation_type_array.length()];
//                                        MainApplication.getInstance().relation_type_value = new String [relation_type_array.length()];
//                                        for (int i =0 ; i < relation_type_array.length() ; i++ ){
//                                            JSONObject relation_type_object = relation_type_array.getJSONObject(i);
//                                            String namecn = relation_type_object.getString("namecn");
//                                            int value = relation_type_object.getInt("value");
//                                            MainApplication.getInstance().relation_type_label[i] = namecn;
//                                            MainApplication.getInstance().relation_type_value[i] = String.valueOf(value);
//                                        }
//                                    }
//
//                                    int numColumns = 4;
//                                    if (null !=relation_type_array && relation_type_array.length() > 0 && relation_type_array.length() > 6 ){
//                                        int mod = relation_type_array.length() % numColumns ;   //补齐单元格
//                                        if(mod !=0){
//                                            int yu = numColumns - mod;
//                                            MainApplication.getInstance().relation_type_label = new String [relation_type_array.length() + yu];
//                                            MainApplication.getInstance().relation_type_value = new String [relation_type_array.length() + yu];
//                                            for (int i = 0 ;i < relation_type_array.length(); i++){
//                                                JSONObject relation_type_object = relation_type_array.getJSONObject(i);
//                                                String namecn = relation_type_object.getString("namecn");
//                                                int value = relation_type_object.getInt("value");
//                                                MainApplication.getInstance().relation_type_label[i] = namecn;
//                                                MainApplication.getInstance().relation_type_value[i] = String.valueOf(value);
//                                            }
//                                            for (int i = 0 ; i < yu ; i++){
//                                                MainApplication.getInstance().relation_type_label[relation_type_array.length() + i] = "";
//                                                MainApplication.getInstance().relation_type_value[relation_type_array.length() + i] = "";
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (dataObject.has("POSCARDORG")){
//                                if (!dataObject.isNull("POSCARDORG")){
//                                    JSONArray pos_card_org_array = dataObject.getJSONArray("POSCARDORG");
//                                    if (null != pos_card_org_array && pos_card_org_array.length() > 0){
//                                        MainApplication.getInstance().pos_card_org_label = new String [pos_card_org_array.length() + 1];
//                                        MainApplication.getInstance().pos_card_org_value = new String [pos_card_org_array.length() + 1];
//                                        MainApplication.getInstance().pos_card_org_label[0] = "请选择";
//                                        MainApplication.getInstance().pos_card_org_value[0] = "";
//
//                                        for (int i= 0 ; i < pos_card_org_array.length() ; i++){
//                                            JSONObject pos_card_org_object = pos_card_org_array.getJSONObject(i);
//                                            String namecn = pos_card_org_object.getString("namecn");
//                                            int value = pos_card_org_object.getInt("value");
//                                            MainApplication.getInstance().pos_card_org_label[i+1] =namecn;
//                                            MainApplication.getInstance().pos_card_org_value[i+1] = String.valueOf(value);
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (dataObject.has("STATUS")){
//                                if (!dataObject.isNull("STATUS")){
//                                    JSONArray status_array = dataObject.getJSONArray("STATUS");
//                                    if (null != status_array && status_array.length() > 0){
//                                        MainApplication.getInstance().status_label = new String[status_array.length() +1 ];
//                                        MainApplication.getInstance().status_value = new String [status_array.length()+1];
//                                        for (int i = 0 ; i < status_array.length() ; i++){
//                                            JSONObject status_object = status_array.getJSONObject(i);
//                                            String namecn = status_object.getString("namecn");
//                                            int value = status_object.getInt("value");
//                                            MainApplication.getInstance().status_label[i] =namecn ;
//                                            MainApplication.getInstance().status_value[i] =String.valueOf(value);
//                                            MainApplication.getInstance().status_map.put(value , namecn);
//                                        }
//                                        //特殊增加一项
//                                        MainApplication.getInstance().status_label[status_array.length()] ="审核中" ;
//                                        MainApplication.getInstance().status_value[status_array.length()] ="250";
//                                        MainApplication.getInstance().status_map.put(250 , "审核中");
//                                    }
//                                }
//                            }
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                Log.e("TAG", content);
            }
        });
        mThreadPool.execute(asyncHttpClient);
    }
}

