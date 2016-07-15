package com.straw.lession.physical.async;

import android.content.Context;
import com.straw.lession.physical.db.DBManager;

/**
 * Created by straw on 2016/7/15.
 */
public class CreateDBTask implements ITask{
    private Context context;
    private ITaskHandler handler;
    public CreateDBTask(Context context, ITaskHandler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public Object doRun() throws Exception {
        DBManager mgr = new DBManager(context);
        handler.handle();
        return null;
    }
}
