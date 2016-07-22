package com.straw.lession.physical.task;

import android.content.Context;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.async.TaskHandler;
import com.straw.lession.physical.async.TaskResult;
import com.straw.lession.physical.constant.TaskConstant;
import com.straw.lession.physical.db.DaoSession;
import com.straw.lession.physical.db.TeacherDao;
import com.straw.lession.physical.vo.LoginInfo;
import com.straw.lession.physical.vo.db.Teacher;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by straw on 2016/7/21.
 */
public class SaveLoginInfoTask extends BaseTask{
    private LoginInfo loginInfo;

    public SaveLoginInfoTask(Context context, TaskHandler taskHandler, LoginInfo loginInfo) {
        super(context, taskHandler);
        this.loginInfo = loginInfo;
    }

    @Override
    public Object doRun() {
        try {
            DaoSession session = MainApplication.getInstance().getDaoSession(context);
            TeacherDao teacherDao = session.getTeacherDao();
            Query query = teacherDao.queryBuilder()
                    .where(TeacherDao.Properties.Mobile.eq(loginInfo.getMobile()))
                    .build();
            List<Teacher> teachers = query.list();
            if(teachers.size() > 0){
                DeleteQuery deleteQuery = teacherDao.queryBuilder()
                        .where(TeacherDao.Properties.Mobile.eq(loginInfo.getMobile()))
                        .buildDelete();
                deleteQuery.executeDeleteWithoutDetachingEntities();
            }
            Teacher teacher = new Teacher();
            teacher.setLast_login_time(new Date());
            teacher.setMobile(loginInfo.getMobile());
            teacher.setName(loginInfo.getPersonName());
            teacher.setTeacherIdR(loginInfo.getUserId());
            loginInfo.setTeacherId(teacherDao.insertOrReplace(teacher));
            TaskResult result = new TaskResult();
            result.setResultCode(TaskConstant.SUCCESS_CODE);
            result.setResultMsg("登录信息入库成功");
            taskHandler.sendSuccessMessage(result);
        }catch (Exception ex){
            taskHandler.sendFailureMessage(ex, "登录信息入库失败");
        }
        return null;
    }
}
