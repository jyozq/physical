package com.straw.lession.physical.db;

import android.content.Context;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskResult;

/**
 * Created by straw on 2016/7/15.
 */
public class CreateDBTask implements ITask {
    public static final int SUCCESS_CODE=0;
    private Context context;
    private TaskHandler handler;
    public CreateDBTask(Context context, TaskHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Object doRun() {
        try {
            CourseDefineDao mgr = new CourseDefineDao(context);
            TaskResult result = new TaskResult();
            result.setResultCode(SUCCESS_CODE);
            result.setResultMsg("创建数据库成功");
            handler.sendSuccessMessage(result);
        }catch (Exception ex){
            handler.sendFailureMessage(ex, "创建数据库失败");
        }
        return null;
    }
}
