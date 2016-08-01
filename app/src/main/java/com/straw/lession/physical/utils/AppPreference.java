package com.straw.lession.physical.utils;

import android.content.Context;
import com.anupcowkur.reservoir.Reservoir;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.db.DaoSession;
import com.straw.lession.physical.db.TeacherDao;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.db.Teacher;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AppPreference {
    private static final String TAG = "AppPreference";
    public static final String LOGIN_INFO_KEY = "logininfo";
    public static final String TOKEN_INFO_KEY = "tokeninfo";

    public static void saveLoginInfoToDB(Context context, final LoginInfoVo loginInfo) throws Exception {
        DBService.getInstance(context).saveLoginInfoToDB(loginInfo);
        Reservoir.put(LOGIN_INFO_KEY, loginInfo);
    }

    public static void saveLoginInfoWithoutDB(LoginInfoVo loginInfo) throws IOException {
        Reservoir.put(LOGIN_INFO_KEY, loginInfo);
    }

    public static void saveToken(TokenInfo tokenInfo) throws Exception{
        Reservoir.put(TOKEN_INFO_KEY, tokenInfo);
    }

    public static TokenInfo getUserToken() throws IOException {
        return Reservoir.get(TOKEN_INFO_KEY, TokenInfo.class);
    }

    public static LoginInfoVo getLoginInfo() throws IOException{
        return Reservoir.get(LOGIN_INFO_KEY, LoginInfoVo.class);
    }

    public static void logout() throws IOException {
        Reservoir.clear();
    }
}
