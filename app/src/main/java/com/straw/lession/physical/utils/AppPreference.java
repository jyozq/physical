package com.straw.lession.physical.utils;

import android.content.Context;
import android.util.Log;
import com.anupcowkur.reservoir.Reservoir;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.async.TaskWorker;
import com.straw.lession.physical.task.SaveLoginInfoTask;
import com.straw.lession.physical.vo.LoginInfo;
import com.straw.lession.physical.vo.TokenInfo;
import java.io.IOException;

public class AppPreference {
    private static final String TAG = "AppPreference";
    public static final String LOGIN_INFO_KEY = "logininfo";
    public static final String TOKEN_INFO_KEY = "tokeninfo";

    public static void saveLoginInfo(Context context, final LoginInfo loginInfo) throws Exception {
        ITask task = new SaveLoginInfoTask(context, new TaskHandler() {
            @Override
            public void onSuccess(TaskResult result) {
                try {
                    Reservoir.put(LOGIN_INFO_KEY, loginInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,"",e);
                    throw new IllegalStateException(e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.e(TAG,"",error);
                throw new IllegalStateException(error.getMessage());
            }

            @Override
            protected void onSelf() {

            }
        }, loginInfo);
        TaskWorker taskWorker = new TaskWorker(task);
        MainApplication.getInstance().getThreadPool().submit(taskWorker);
    }

    public static void saveToken(TokenInfo tokenInfo) throws Exception{
        Reservoir.put(TOKEN_INFO_KEY, tokenInfo);
    }

    public static TokenInfo getUserToken() throws IOException {
        return Reservoir.get(TOKEN_INFO_KEY, TokenInfo.class);
    }

    public static LoginInfo getLoginInfo() throws IOException{
        return Reservoir.get(LOGIN_INFO_KEY, LoginInfo.class);
    }
}
