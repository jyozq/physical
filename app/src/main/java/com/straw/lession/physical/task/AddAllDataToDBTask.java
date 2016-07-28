package com.straw.lession.physical.task;

import android.content.Context;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.constant.TaskConstant;
import com.straw.lession.physical.db.*;
import com.straw.lession.physical.vo.CourseDefineVo;
import com.straw.lession.physical.vo.InstituteVo;
import com.straw.lession.physical.vo.db.*;

import java.util.List;

/**
 * Created by straw on 2016/7/22.
 */
public class AddAllDataToDBTask extends BaseTask{
    private List<InstituteVo> instituteVos;

    public AddAllDataToDBTask(Context context, TaskHandler taskHandler, List<InstituteVo> instituteVos){
        super(context,taskHandler);
        this.instituteVos = instituteVos;
    }

    @Override
    public Object doRun() {
        try {
            DbService.getInstance(context).refineInstituteData(instituteVos);

            TaskResult result = new TaskResult();
            result.setResultCode(TaskConstant.SUCCESS_CODE);
            result.setResultMsg("数据添加成功");
            taskHandler.sendSuccessMessage(result);
        }catch (Exception ex){
            taskHandler.sendFailureMessage(ex, "数据添加失败");
        }
        return null;
    }
}
