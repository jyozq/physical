package com.straw.lession.physical.task;

import android.content.Context;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.constant.TaskConstant;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.vo.UploadCourseDataResultVo;

import java.util.List;

/**
 * Created by straw on 2016/7/29.
 */
public class UpdateUploadResultTask extends BaseTask{
    private List<UploadCourseDataResultVo> resultVoList;
    private long teacherIdR;

    public UpdateUploadResultTask(Context context, TaskHandler taskHandler, List<UploadCourseDataResultVo> resultVoList,long teacherIdR) {
        super(context, taskHandler);
        this.resultVoList = resultVoList;
        this.teacherIdR = teacherIdR;
    }

    @Override
    public Object doRun() {
        try {
            for (UploadCourseDataResultVo resultVo : resultVoList) {
                DBService.getInstance(context).updateUploadResult(resultVo, teacherIdR);
            }
            TaskResult result = new TaskResult();
            result.setResultCode(TaskConstant.SUCCESS_CODE);
            result.setResultMsg("更新本地缓存记录成功");
            taskHandler.sendSuccessMessage(result);
        }catch(Exception ex){
            taskHandler.sendFailureMessage(ex, "更新本地缓存记录失败");
        }
        return null;
    }
}
