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
 * Created by straw on 2016/7/22.
 */
public class AddAllDataToDBTask extends BaseTask{
    private List<Institute> institutes;
    private List<ClassInfo> classes;
    private List<Student> students;
    private List<CourseDefine> courseDefines;

    public AddAllDataToDBTask(Context context, TaskHandler taskHandler, List<Institute> institutes,
                              List<ClassInfo> classes, List<Student> students, List<CourseDefine> courseDefines){
        super(context,taskHandler);
        this.institutes = institutes;
        this.classes = classes;
        this.students = students;
        this.courseDefines = courseDefines;
    }

    @Override
    public Object doRun() {
        try {
            DaoSession daoSession = MainApplication.getInstance().getDaoSession(context);
            InstituteDao instituteDao = daoSession.getInstituteDao();
            instituteDao.insertInTx(institutes, true);

            for(int i = 0; i < classes.size(); i ++){
                ClassInfo classInfo = classes.get(i);
                for(int j = 0; j < institutes.size(); j ++){
                    Institute institute = institutes.get(j);
                    if(classInfo.getInstituteIdR() == institute.getInstituteIdR()){
                        classInfo.setInstituteId(institute.getId());
                        break;
                    }
                }
            }
            ClassInfoDao classInfoDao = daoSession.getClassInfoDao();
            classInfoDao.insertInTx(classes,true);

            for(int i = 0; i < students.size(); i ++){
                Student student = students.get(i);
                for(int j = 0; j < classes.size(); j ++){
                    ClassInfo classInfo = classes.get(j);
                    if(student.getClassIdR() == classInfo.getClassIdR()){
                        student.setClassId(classInfo.getId());
                        break;
                    }
                }
            }
            StudentDao studentDao = daoSession.getStudentDao();
            studentDao.insertInTx(students, true);

            for(int i = 0; i < courseDefines.size(); i ++){
                CourseDefine courseDefine = courseDefines.get(i);
                for(int j = 0; j < institutes.size(); j ++){
                    Institute institute = institutes.get(j);
                    if(courseDefine.getInstituteIdR() == institute.getInstituteIdR()){
                        courseDefine.setInstituteId(institute.getId());
                        break;
                    }
                }

                for(int j = 0; j < classes.size(); j ++){
                    ClassInfo classInfo = classes.get(j);
                    if(courseDefine.getClassIdR() == classInfo.getClassIdR()){
                        courseDefine.setClassId(classInfo.getId());
                        break;
                    }
                }
            }
            CourseDefineDao courseDefineDao = daoSession.getCourseDefineDao();
            courseDefineDao.insertInTx(courseDefines,true);

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
