package com.straw.lession.physical.utils;

import android.content.Intent;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.straw.lession.physical.activity.MainActivity;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.ParamConstant;
import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.http.AsyncHttpClient;
import com.straw.lession.physical.http.AsyncHttpResponseHandler;
import com.straw.lession.physical.http.HttpResponseBean;
import com.straw.lession.physical.vo.LoginInfo;
import com.straw.lession.physical.vo.TokenInfo;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2015/12/21.
 */
public class AppPreference {
    public static final String LOGIN_INFO_KEY = "logininfo";
    public static final String TOKEN_INFO_KEY = "tokeninfo";

    public static void saveLoginInfo(LoginInfo loginInfo) throws Exception {
        Reservoir.put(LOGIN_INFO_KEY, loginInfo);
    }

    public static void saveToken(TokenInfo tokenInfo) throws Exception{
        Reservoir.put(TOKEN_INFO_KEY, tokenInfo);
    }

    public static TokenInfo getUserToken() throws IOException {
        return Reservoir.get(TOKEN_INFO_KEY, TokenInfo.class);
    }
}
