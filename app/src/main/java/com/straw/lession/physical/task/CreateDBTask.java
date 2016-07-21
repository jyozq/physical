package com.straw.lession.physical.task;

import android.content.Context;

import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.constant.TaskConstant;

/**
 * Created by straw on 2016/7/15.
 */
public class CreateDBTask extends BaseTask {
    public CreateDBTask(Context context, TaskHandler handler){
        super(context, handler);
    }

    @Override
    public Object doRun() {
        try {
            MainApplication.getInstance().getSQLDatebase(context);
            TaskResult result = new TaskResult();
            result.setResultCode(TaskConstant.SUCCESS_CODE);
            result.setResultMsg("创建数据库成功");
            taskHandler.sendSuccessMessage(result);
        }catch (Exception ex){
            taskHandler.sendFailureMessage(ex, "创建数据库失败");
        }
        return null;
    }
}
