package com.straw.lession.physical.utils;

import com.anupcowkur.reservoir.Reservoir;
import com.straw.lession.physical.vo.LoginInfo;

/**
 * Created by Administrator on 2015/12/21.
 */
public class AppPreference {
    public static final String LOGIN_INFO_KEY = "logininfo";

    public static void saveLoginInfo(LoginInfo loginInfo) throws Exception {
        Reservoir.put(LOGIN_INFO_KEY, loginInfo);
    }
}
