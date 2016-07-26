package com.straw.lession.physical.task;

import android.content.Context;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.constant.TaskConstant;
import com.straw.lession.physical.db.*;
import com.straw.lession.physical.vo.LoginInfoVo;
import org.greenrobot.greendao.query.DeleteQuery;

/**
 * Created by straw on 2016/7/21.
 */
public class DeleteAllDataTask extends BaseTask{
    private LoginInfoVo loginInfo;
    public DeleteAllDataTask(Context context, TaskHandler taskHandler, LoginInfoVo loginInfo){
        super(context, taskHandler);
        this.loginInfo = loginInfo;
    }

    @Override
    public Object doRun() {
        try {
            DaoSession session = MainApplication.getInstance().getDaoSession(context);
            StudentDao studentDao = session.getStudentDao();
            DeleteQuery deleteQuery = studentDao.queryBuilder().where(
                    StudentDao.Properties.LoginId.eq(loginInfo.getTeacherId())).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();

            ClassInfoDao classInfoDao = session.getClassInfoDao();
            deleteQuery = classInfoDao.queryBuilder().where(
                    ClassInfoDao.Properties.LoginId.eq(loginInfo.getTeacherId())).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();

            CourseDefineDao courseDefineDao = session.getCourseDefineDao();
            deleteQuery = courseDefineDao.queryBuilder().where(CourseDefineDao.Properties.LoginId.eq(loginInfo.getTeacherId())).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();

            InstituteDao instituteDao = session.getInstituteDao();
            deleteQuery = instituteDao.queryBuilder().where(InstituteDao.Properties.LoginId.eq(loginInfo.getTeacherId())).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();

            CourseDao courseDao = session.getCourseDao();
            deleteQuery = courseDao.queryBuilder().where(CourseDao.Properties.LoginId.eq(loginInfo.getTeacherId())).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();

            TaskResult result = new TaskResult();
            result.setResultCode(TaskConstant.SUCCESS_CODE);
            result.setResultMsg("数据删除成功");
            taskHandler.sendSuccessMessage(result);
        }catch (Exception ex){
            taskHandler.sendFailureMessage(ex, "数据删除失败");
        }
        return null;
    }
}
