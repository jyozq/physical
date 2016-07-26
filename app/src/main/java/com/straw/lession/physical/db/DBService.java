package com.straw.lession.physical.db;

import android.content.Context;
import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.vo.ClassInfoVo;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.ClassInfo;
import org.greenrobot.greendao.query.Query;

import java.io.IOException;
import java.util.List;

/**
 * Created by straw on 2016/7/26.
 */
public class DbService {
    private static final String TAG = DbService.class.getSimpleName();
    private static DbService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private InstituteDao instituteDao;
    private ClassInfoDao classInfoDao;
    private StudentDao studentDao;
    private CourseDefineDao courseDefineDao;
    private CourseDao courseDao;


    private DbService() {
    }

    public static DbService getInstance(Context context) {
        if (instance == null) {
            instance = new DbService();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = MainApplication.getInstance().getDaoSession(context);
            instance.instituteDao = instance.mDaoSession.getInstituteDao();
            instance.classInfoDao = instance.mDaoSession.getClassInfoDao();
            instance.studentDao = instance.mDaoSession.getStudentDao();
            instance.courseDefineDao = instance.mDaoSession.getCourseDefineDao();
            instance.courseDao = instance.mDaoSession.getCourseDao();
        }
        return instance;
    }

    public void refineClassInfo(List<ClassInfoVo> classInfoVos) throws IOException {
        List<ClassInfo> allClassInfos = classInfoDao.loadAll();
        for(ClassInfoVo classInfoVo : classInfoVos){
            boolean isExistInSQLite = false;
            for(ClassInfo classInfo : allClassInfos){
                if(classInfoVo.getClassId() == classInfo.getClassIdR()){
                    classInfo.setCode(classInfoVo.getClassCode());
                    classInfo.setName(classInfoVo.getClassName());
                    classInfo.setType(classInfoVo.getClassType());
                    classInfoDao.update(classInfo);
                    isExistInSQLite = true;
                    break;
                }
            }
            if(!isExistInSQLite){
                LoginInfoVo loginInfoVo = AppPreference.getLoginInfo();
                ClassInfo newClassInfo = new ClassInfo();
                newClassInfo.setInstituteId(loginInfoVo.getCurrentInstituteId());
                newClassInfo.setType(classInfoVo.getClassType());
                newClassInfo.setClassIdR(classInfoVo.getClassId());
                newClassInfo.setCode(classInfoVo.getClassCode());
                newClassInfo.setInstituteIdR(loginInfoVo.getCurrentInstituteIdR());
                newClassInfo.setLoginId(loginInfoVo.getTeacherId());
                newClassInfo.setName(classInfoVo.getClassName());
                classInfoDao.insert(newClassInfo);
            }
        }
    }

    public List<ClassInfo> getAllClass(long teacherId, Long instituteId) {
        Query query = classInfoDao.queryBuilder().where(ClassInfoDao.Properties.LoginId.eq(teacherId),
                                                        ClassInfoDao.Properties.InstituteId.eq(instituteId)
                                                        ).build();
        return query.list();
    }
}
