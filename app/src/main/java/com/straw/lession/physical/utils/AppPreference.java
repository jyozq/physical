package com.straw.lession.physical.utils;

import android.content.Context;
import com.anupcowkur.reservoir.Reservoir;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.db.DaoSession;
import com.straw.lession.physical.db.TeacherDao;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.TokenInfo;
import com.straw.lession.physical.vo.db.Teacher;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AppPreference {
    private static final String TAG = "AppPreference";
    public static final String LOGIN_INFO_KEY = "logininfo";
    public static final String TOKEN_INFO_KEY = "tokeninfo";

    public static void saveLoginInfo(Context context, final LoginInfoVo loginInfo) throws Exception {
        DaoSession session = MainApplication.getInstance().getDaoSession(context);
        TeacherDao teacherDao = session.getTeacherDao();
        Query query = teacherDao.queryBuilder()
                .where(TeacherDao.Properties.Mobile.eq(loginInfo.getMobile()))
                .build();
        List<Teacher> teachers = query.list();
        if(teachers.size() > 0){
            DeleteQuery deleteQuery = teacherDao.queryBuilder()
                    .where(TeacherDao.Properties.Mobile.eq(loginInfo.getMobile()))
                    .buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
        }
        Teacher teacher = new Teacher();
        teacher.setLast_login_time(new Date());
        teacher.setMobile(loginInfo.getMobile());
        teacher.setName(loginInfo.getPersonName());
        teacher.setTeacherIdR(loginInfo.getUserId());
        loginInfo.setTeacherId(teacherDao.insertOrReplace(teacher));
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
