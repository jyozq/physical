package com.straw.lession.physical.task;

import android.content.Context;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.vo.item.CourseDefineItemInfo;

/**
 * Created by straw on 2016/8/1.
 */
public class DeleteCourseDefineTask extends BaseTask{
    private CourseDefineItemInfo delCourseDefine;
    public DeleteCourseDefineTask(Context context, TaskHandler taskHandler, CourseDefineItemInfo delCourseDefine) {
        super(context, taskHandler);
        this.delCourseDefine = delCourseDefine;
    }

    @Override
    public Object doRun() {

        return null;
    }
}
