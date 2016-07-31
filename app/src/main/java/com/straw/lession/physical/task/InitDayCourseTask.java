package com.straw.lession.physical.task;

import android.content.Context;

import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.constant.TaskConstant;
import com.straw.lession.physical.constant.Weekday;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.vo.UploadCourseDataResultVo;
import com.straw.lession.physical.vo.db.CourseDefine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by straw on 2016/7/30.
 */
public class InitDayCourseTask extends BaseTask{
    private static final String TAG = "InitDayCourseTask";
    private long teacherId;
    private long instituteId;
    private int weekday;

    public InitDayCourseTask(Context context, TaskHandler taskHandler, long teacherId, long instituteId, int weekday){
        super(context, taskHandler);
        this.teacherId = teacherId;
        this.weekday = weekday;
        this.instituteId = instituteId;
    }

    @Override
    public Object doRun() {
        try {
            List<CourseDefine> courseDefineList = DBService.getInstance(context).initDayCourseData(teacherId,instituteId,weekday);
            assembleData(courseDefineList);
            TaskResult result = new TaskResult();
            result.setResultCode(TaskConstant.SUCCESS_CODE);
            result.setResultMsg("获取"+ Weekday.getName(weekday)+"的课程表记录成功");
            Map<String, Object> model = new HashMap<String,Object>();
            model.put("data", courseDefineList);
            result.setData(model);
            taskHandler.sendSuccessMessage(result);
        }catch(Exception ex){
            taskHandler.sendFailureMessage(ex, "获取"+ Weekday.getName(weekday)+"的课程表记录失败");
        }
        return null;
    }

    private void assembleData(List<CourseDefine> courseDefineList) {
        for(int i = 1; i <= 8; i++){
            boolean isExist = false;
            for(CourseDefine courseDefine : courseDefineList){
                if(courseDefine.getSeq() == i){
                    isExist = true;
                    break;
                }
            }
            if(!isExist){
                CourseDefine newCourseDefine = new CourseDefine();
                newCourseDefine.setCourseDefineIdR(-1l);
                newCourseDefine.setSeq(i);
                newCourseDefine.setWeekDay(weekday);
                courseDefineList.add(newCourseDefine);
            }
        }
    }
}
