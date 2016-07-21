package com.straw.lession.physical.task;

import android.content.Context;
import com.straw.lession.physical.async.ITask;
import com.straw.lession.physical.async.TaskHandler;

/**
 * Created by straw on 2016/7/21.
 */
public abstract class BaseTask implements ITask {
    protected Context context;
    protected TaskHandler taskHandler;

    public BaseTask(Context context, TaskHandler taskHandler){
        this.context = context;
        this.taskHandler = taskHandler;
    }
}
