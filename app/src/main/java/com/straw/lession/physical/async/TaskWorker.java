package com.straw.lession.physical.async;

import java.util.concurrent.Callable;

/**
 * Created by straw on 2016/7/15.
 */
public class TaskWorker implements Callable{
    private ITask task;

    public TaskWorker(ITask task){
        this.task = task;
    }

    @Override
    public Object call() throws Exception {
        return task.doRun();
    }
}
