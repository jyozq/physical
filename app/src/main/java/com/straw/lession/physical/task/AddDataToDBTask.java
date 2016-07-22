package com.straw.lession.physical.task;

import android.content.Context;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.constant.TaskConstant;
import com.straw.lession.physical.db.*;
import com.straw.lession.physical.vo.db.ClassInfo;
import com.straw.lession.physical.vo.db.CourseDefine;
import com.straw.lession.physical.vo.db.Institute;
import com.straw.lession.physical.vo.db.Student;

import java.util.List;

/**
 * Created by straw on 2016/7/21.
 */
public class AddDataToDBTask extends BaseTask{
    private List data;
    private Class type;
    public AddDataToDBTask(Context context, TaskHandler taskHandler, List data, Class type){
        super(context, taskHandler);
        this.data = data;
        this.type = type;
    }

    @Override
    public Object doRun() {
        try {
            DaoSession daoSession = MainApplication.getInstance().getDaoSession(context);
            if(type.equals(Institute.class)){
                InstituteDao instituteDao = daoSession.getInstituteDao();
                instituteDao.insertInTx(data,true);
            }else if(type.equals(ClassInfo.class)){
                ClassInfoDao classInfoDao = daoSession.getClassInfoDao();
                classInfoDao.insertInTx(data,true);
            }else if(type.equals(Student.class)){
                StudentDao studentDao = daoSession.getStudentDao();
                studentDao.insertInTx(data,true);
            }else if(type.equals(CourseDefine.class)){
                CourseDefineDao courseDefineDao = daoSession.getCourseDefineDao();
                courseDefineDao.insertInTx(data,true);
            }
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
