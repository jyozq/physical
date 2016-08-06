package com.straw.lession.physical.db;

import android.content.Context;

import com.straw.lession.physical.app.MainApplication;
import com.straw.lession.physical.constant.CommonConstants;
import com.straw.lession.physical.constant.CourseStatus;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.vo.*;
import com.straw.lession.physical.vo.db.*;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by straw on 2016/7/26.
 */
public class DBService {
    private static final int IS_DEL = 1;
    private static final int IS_DEL_NOT = 0;
    private static final String TAG = DBService.class.getSimpleName();
    private static DBService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private InstituteDao instituteDao;
    private ClassInfoDao classInfoDao;
    private StudentDao studentDao;
    private CourseDefineDao courseDefineDao;
    private CourseDao courseDao;
    private TeacherInstituteDao teacherInstituteDao;
    private StudentDeviceDao studentDeviceDao;
    private TeacherDao teacherDao;

    private DBService() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public static DBService getInstance(Context context) {
        if (instance == null) {
            instance = new DBService();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = MainApplication.getInstance().getDaoSession(context);
            instance.instituteDao = instance.mDaoSession.getInstituteDao();
            instance.classInfoDao = instance.mDaoSession.getClassInfoDao();
            instance.studentDao = instance.mDaoSession.getStudentDao();
            instance.courseDefineDao = instance.mDaoSession.getCourseDefineDao();
            instance.courseDao = instance.mDaoSession.getCourseDao();
            instance.teacherInstituteDao = instance.mDaoSession.getTeacherInstituteDao();
            instance.studentDeviceDao = instance.mDaoSession.getStudentDeviceDao();
            instance.teacherDao = instance.mDaoSession.getTeacherDao();
        }
        return instance;
    }

    public void refineInstituteData(List<InstituteVo> instituteVos) throws Exception {
        if(!Detect.notEmpty(instituteVos)){
            return;
        }
        //更新教师和机构的关联表
        refineTeacherInstituteData(instituteVos);

        List<Institute> allInstitutes = instituteDao.loadAll();
        for(InstituteVo instituteVO : instituteVos){
            boolean isExistInSQLite = false;
            for(Institute institute : allInstitutes){
                if(instituteVO.getInstituteId() == institute.getInstituteIdR()){
                    institute.setName(instituteVO.getInstituteName());
                    instituteDao.update(institute);
                    isExistInSQLite = true;
                    break;
                }
            }
            if(!isExistInSQLite){
                Institute institute = new Institute();
                institute.setInstituteIdR(instituteVO.getInstituteId());
                institute.setName(instituteVO.getInstituteName());
                institute.setIsDel(IS_DEL_NOT);
                instituteDao.insert(institute);
            }

            List<ClassInfoVo> classInfoVos = instituteVO.getClasses();
            if(Detect.notEmpty(classInfoVos)){
                refineClassInfoData(classInfoVos, instituteVO.getInstituteId());
            }

            List<CourseDefineVo> courseDefineVos = instituteVO.getCourseDefines();
            if(Detect.notEmpty(courseDefineVos)){
                refineCourseDefineData(courseDefineVos, instituteVO.getInstituteId());
            }
        }

        /*暂时不做删除
        for(Institute institute : allInstitutes){
            boolean isDel = true;
            for(InstituteVo instituteVO : instituteVos){
                if(instituteVO.getInstituteId() == institute.getInstituteIdR()){
                    isDel=false;
                    break;
                }
            }
            if(isDel){
                institute.setIsDel(IS_DEL);
                instituteDao.update(institute);
            }
        }
        */
    }

    private void refineTeacherInstituteData(List<InstituteVo> instituteVos) throws Exception {
        LoginInfoVo loginInfoVo = AppPreference.getLoginInfo();
        DeleteQuery deleteQuery = teacherInstituteDao.queryBuilder()
                                    .where(TeacherInstituteDao.Properties.TeacherIdR.eq(loginInfoVo.getUserId())).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();

        for(InstituteVo instituteVo : instituteVos){
            TeacherInstitute teacherInstitute = new TeacherInstitute();
            teacherInstitute.setTeacherIdR(loginInfoVo.getUserId());
            teacherInstitute.setInstituteIdR(instituteVo.getInstituteId());
            teacherInstituteDao.insert(teacherInstitute);
        }
    }

    public void refineClassInfoData(List<ClassInfoVo> classInfoVos, long instituteId) throws Exception {
        List<ClassInfo> allClassInfos = classInfoDao.queryBuilder().where(ClassInfoDao.Properties.InstituteIdR.eq(instituteId)).list();
        for(ClassInfoVo classInfoVo : classInfoVos){
            boolean isExistInSQLite = false;
            for(ClassInfo classInfo : allClassInfos){
                if(classInfoVo.getClassId() == classInfo.getClassIdR()){
                    classInfo.setCode(classInfoVo.getClassCode());
                    classInfo.setName(classInfoVo.getClassName());
                    classInfo.setType(classInfoVo.getClassType());
                    classInfo.setTotalNum(classInfoVo.getTotalNum());
                    classInfoDao.update(classInfo);
                    isExistInSQLite = true;
                    break;
                }
            }
            if(!isExistInSQLite){
                ClassInfo newClassInfo = new ClassInfo();
                newClassInfo.setType(classInfoVo.getClassType());
                newClassInfo.setClassIdR(classInfoVo.getClassId());
                newClassInfo.setCode(classInfoVo.getClassCode());
                newClassInfo.setName(classInfoVo.getClassName());
                if(instituteId > 0) {
                    newClassInfo.setInstituteIdR(instituteId);
                }
                newClassInfo.setTotalNum(classInfoVo.getTotalNum());
                newClassInfo.setIsDel(IS_DEL_NOT);
                classInfoDao.insert(newClassInfo);
            }

            List<StudentVo> studentVos = classInfoVo.getStudents();
            if(Detect.notEmpty(studentVos)){
                refineStudentData(studentVos, classInfoVo.getClassId());
            }
        }
        /*暂时不做删除
        for(ClassInfo classInfo : allClassInfos){
            boolean isDel = true;
            for(ClassInfoVo classInfoVo : classInfoVos){
                if(classInfoVo.getClassId() == classInfo.getClassIdR()){
                    isDel=false;
                    break;
                }
            }
            if(isDel){
                classInfo.setIsDel(IS_DEL);
                classInfoDao.update(classInfo);
            }
        }
        */
    }

    public void refineStudentData(List<StudentVo> studentVos, long classInfoId) throws Exception {
        List<Student> allStudents = studentDao.loadAll();
        for(StudentVo studentVo : studentVos){
            boolean isExistInSQLite = false;
            for(Student student : allStudents){
                if(studentVo.getStudentId() == student.getStudentIdR()){
                    student.setCode(studentVo.getStudentCode());
                    student.setName(studentVo.getStudentName());
                    student.setGender(studentVo.getGender());
                    student.setBirthday(DateUtil.formatStrToDate(studentVo.getBirthday()));
                    studentDao.update(student);
                    isExistInSQLite = true;
                    break;
                }
            }
            if(!isExistInSQLite){
                Student newStudent = new Student();
                newStudent.setCode(studentVo.getStudentCode());
                newStudent.setName(studentVo.getStudentName());
                newStudent.setGender(studentVo.getGender());
                newStudent.setBirthday(DateUtil.formatStrToDate(studentVo.getBirthday()));
                if(classInfoId > 0) {
                    newStudent.setClassIdR(classInfoId);
                }
                newStudent.setStudentIdR(studentVo.getStudentId());
                studentDao.insert(newStudent);
            }
        }

        /*暂时不做删除
        for(Student student : allStudents){
            boolean isDel = true;
            for(StudentVo studentVo : studentVos){
                if(studentVo.getStudentId() == student.getStudentIdR()){
                    isDel=false;
                    break;
                }
            }
            if(isDel){
                student.setIsDel(IS_DEL);
                studentDao.update(student);
            }
        }
        */
    }

    private void refineCourseDefineData(List<CourseDefineVo> courseDefineVos, long instituteId) throws Exception {
        LoginInfoVo loginInfoVO = AppPreference.getLoginInfo();
        List<CourseDefine> courseDefines = courseDefineDao.queryBuilder()
                .where(CourseDefineDao.Properties.InstituteIdR.eq(instituteId),
                        CourseDefineDao.Properties.TeacherIdR.eq(loginInfoVO.getUserId())).list();
        for(CourseDefineVo courseDefineVo : courseDefineVos){
            boolean isExistInSQLite = false;
            for(CourseDefine courseDefine : courseDefines){
                if(courseDefine.getCourseDefineIdR() == courseDefineVo.getCourseDefineId()){
                    courseDefine.setDate(DateUtil.formatStrToDate(courseDefineVo.getCourseDate()));
                    courseDefine.setClassIdR(courseDefineVo.getClassId());
                    courseDefine.setInstituteIdR(courseDefineVo.getInstituteId());
                    courseDefine.setCode(courseDefineVo.getCourseCode());
                    courseDefine.setName(courseDefineVo.getCourseName());
                    courseDefine.setSeq(courseDefineVo.getCourseSeq());
                    courseDefine.setLocation(courseDefineVo.getCourseLocation());
                    courseDefine.setType(courseDefineVo.getCourseType());
                    courseDefine.setUseOnce(courseDefineVo.getUseOnce());
                    courseDefine.setWeekDay(courseDefineVo.getWeekday());
                    courseDefineDao.update(courseDefine);
                    isExistInSQLite = true;
                    break;
                }
            }
            if(!isExistInSQLite){
                CourseDefine newCourseDefine = new CourseDefine();
                newCourseDefine.setDate(DateUtil.formatStrToDate(courseDefineVo.getCourseDate()));
                newCourseDefine.setClassIdR(courseDefineVo.getClassId());
                newCourseDefine.setInstituteIdR(courseDefineVo.getInstituteId());
                newCourseDefine.setCode(courseDefineVo.getCourseCode());
                newCourseDefine.setName(courseDefineVo.getCourseName());
                newCourseDefine.setSeq(courseDefineVo.getCourseSeq());
                newCourseDefine.setLocation(courseDefineVo.getCourseLocation());
                newCourseDefine.setType(courseDefineVo.getCourseType());
                newCourseDefine.setUseOnce(courseDefineVo.getUseOnce());
                newCourseDefine.setWeekDay(courseDefineVo.getWeekday());
                newCourseDefine.setCourseDefineIdR(courseDefineVo.getCourseDefineId());
                newCourseDefine.setIsDel(IS_DEL_NOT);
                newCourseDefine.setTeacherIdR(loginInfoVO.getUserId());
                courseDefineDao.insert(newCourseDefine);
            }
        }

        //教师的课程表可能会被修改 所以需要做删除处理
        for(CourseDefine courseDefine : courseDefines){
            boolean isDel = true;
            for(CourseDefineVo courseDefineVo : courseDefineVos){
                if(courseDefineVo.getCourseDefineId() == courseDefine.getCourseDefineIdR()){
                    isDel=false;
                    break;
                }
            }
            if(isDel){
                courseDefine.setIsDel(IS_DEL);
                courseDefineDao.update(courseDefine);
            }
        }
    }

    public List<ClassInfo> getClassByInstitute(Long instituteId) {
        Query query = classInfoDao.queryBuilder().where(ClassInfoDao.Properties.InstituteIdR.eq(instituteId)).build();
        return query.list();
    }

    public List<Student> getStudentByClass(long classId) {
        Query query = studentDao.queryBuilder().where(StudentDao.Properties.ClassIdR.eq(classId)).build();
        return query.list();
    }

    public List<Institute> getInsituteDataByTeacher(long teacherId) {
        List<Institute> institutes = new ArrayList<>();
        List<TeacherInstitute> tis = teacherInstituteDao.queryBuilder()
                .where(TeacherInstituteDao.Properties.TeacherIdR.eq(teacherId)).list();
        if(Detect.notEmpty(tis)){
            for(TeacherInstitute teacherInstitute : tis){
                institutes.add(teacherInstitute.getInstitute());
            }
        }
        return institutes;
    }

    /**
     * 获取教师当天课程，除未开始课程以外的课程
     * @param teacherId
     * @param currentInstituteIdR
     * @return
     */
    public List<Course> getTodayCourses(long teacherId, Long currentInstituteIdR) {
        QueryBuilder qb = courseDao.queryBuilder();
        WhereCondition wc = qb.and(CourseDao.Properties.TeacherIdR.eq(teacherId),
                                    CourseDao.Properties.InstituteIdR.eq(currentInstituteIdR),
                                    CourseDao.Properties.Weekday.eq(DateUtil.getCurrentWeekday()),
                                    CourseDao.Properties.UseOnce.eq(0));
        List<Course> courses = qb.where(wc).list();

        qb = courseDao.queryBuilder();
        wc = qb.and(CourseDao.Properties.TeacherIdR.eq(teacherId),
                CourseDao.Properties.InstituteIdR.eq(currentInstituteIdR),
                CourseDao.Properties.UseOnce.eq(1));

        List<Course> tempCourses = qb.where(wc).list();

        for(Course course : tempCourses){
            if(DateUtil.dateToStr(course.getDate()).equals(DateUtil.dateToStr(new Date()))){
                courses.add(course);
            }
        }
        return courses;
    }

    public List<CourseDefine> getUnStartedTodayCourses(long teacherId, long currentInstituteIdR) {
        QueryBuilder qb = courseDefineDao.queryBuilder();
        WhereCondition wc = qb.and(CourseDefineDao.Properties.TeacherIdR.eq(teacherId),
                                    CourseDefineDao.Properties.InstituteIdR.eq(currentInstituteIdR),
                                    CourseDefineDao.Properties.WeekDay.eq(DateUtil.getCurrentWeekday()),
                                                CourseDefineDao.Properties.UseOnce.eq(0));
        List<CourseDefine> courseDefines = qb.where(wc).list();

        qb = courseDefineDao.queryBuilder();
        wc = qb.and(CourseDefineDao.Properties.TeacherIdR.eq(teacherId),
                    CourseDefineDao.Properties.InstituteIdR.eq(currentInstituteIdR),
                    CourseDefineDao.Properties.UseOnce.eq(1));

        List<CourseDefine> tempCourseDefines = qb.where(wc).list();
        for(CourseDefine courseDefine : tempCourseDefines){
            if(DateUtil.dateToStr(courseDefine.getDate()).equals(DateUtil.dateToStr(new Date()))){
                courseDefines.add(courseDefine);
            }
        }
        return courseDefines;
    }

    public CourseDefine findCourseDefineById(long courseDefineId) {
        return courseDefineDao.load(courseDefineId);
    }

    public void addCourse(Course course) {
        courseDao.insert(course);
    }

    public Course findCourseById(long courseId) {
        return courseDao.load(courseId);
    }

    public void updateCourse(Course course) {
        courseDao.update(course);
    }

    public List<Course> getUnUploadedData(long userId) {
        return courseDao.queryBuilder().where(CourseDao.Properties.TeacherIdR.eq(userId),
                                                CourseDao.Properties.Status.eq(CourseStatus.OVER.getValue()),
                                                CourseDao.Properties.IsUploaded.eq(false)).list();
    }

    public List<Course> getUploadedData(long userId) {
        return courseDao.queryBuilder().where(CourseDao.Properties.TeacherIdR.eq(userId),
                CourseDao.Properties.IsUploaded.eq(true)).list();
    }

    public List<Course> getCourseExceptUnstarted(long userId) {
        return courseDao.queryBuilder().where(CourseDao.Properties.TeacherIdR.eq(userId)).list();
    }

    public List<StudentDevice> getStudentDeviceInfoNotUploaded(long studentIdR, long userId, long courseDefindIdR) {
        return studentDeviceDao.queryBuilder().where(StudentDeviceDao.Properties.TeacherIdR.eq(userId),
                StudentDeviceDao.Properties.CourseDefineIdR.eq(courseDefindIdR),
                StudentDeviceDao.Properties.StudentIdR.eq(studentIdR),
                StudentDeviceDao.Properties.IsUploaded.eq(false)).list();
    }

    public void addStudentDevice(StudentDevice studentDevice) {
        studentDeviceDao.insert(studentDevice);
    }

    public List<StudentDevice> getStudentDeviceByCourseDefine(long courseDefineId, long userId) {
        return studentDeviceDao.queryBuilder().where(StudentDeviceDao.Properties.CourseDefineIdR.eq(courseDefineId),
                                                    StudentDeviceDao.Properties.TeacherIdR.eq(userId),
                                                    StudentDeviceDao.Properties.IsUploaded.eq(false)).orderAsc().list();
    }

    public void updateStudentDevices(List<StudentDevice> studentDevices) {
        studentDeviceDao.updateInTx(studentDevices);
    }

    public List<Course> getStartedCourseByTeacher(long userId, Long currentInstituteIdR) {
        return courseDao.queryBuilder().where(CourseDao.Properties.TeacherIdR.eq(userId),
                                                CourseDao.Properties.Status.eq(CourseStatus.STARTED.getValue()),
                                                CourseDao.Properties.IsUploaded.eq(false),
                CourseDao.Properties.InstituteIdR.eq(currentInstituteIdR)).list();
    }

    public int countBindedStudentNumByCourse(long userId, Long courseId) {
        return (int)studentDeviceDao.queryBuilder().where(StudentDeviceDao.Properties.CourseId.eq(courseId),
                                                    StudentDeviceDao.Properties.TeacherIdR.eq(userId)).count();
    }

    public Course getUnUploadCourseById(long courseId, long userId) {
        List<Course> courses = courseDao.queryBuilder().where(CourseDao.Properties.Id.eq(courseId),
                                                CourseDao.Properties.TeacherIdR.eq(userId),
                                                CourseDao.Properties.IsUploaded.eq(false)).list();
        if(Detect.notEmpty(courses)){
            return courses.get(0);
        }
        return null;
    }

    public List<StudentDevice> getUnUploadStudentDeviceByCourse(long courseId, long userId) {
        return studentDeviceDao.queryBuilder().where(StudentDeviceDao.Properties.CourseId.eq(courseId),
                                                StudentDeviceDao.Properties.TeacherIdR.eq(userId),
                                                StudentDeviceDao.Properties.IsUploaded.eq(false)).list();
    }

    public void updateUploadResult(UploadCourseDataResultVo resultVo, long userId) {
        List<Course> courses = courseDao.queryBuilder().where(CourseDao.Properties.Id.eq(resultVo.getLocalCourseSeq()),
                                        CourseDao.Properties.TeacherIdR.eq(userId)).list();
        boolean isSuccess = resultVo.getSyncResult().equals("S");
        if(Detect.notEmpty(courses)){
            Course course = courses.get(0);
            course.setIsUploaded(isSuccess);
            if(!isSuccess){
                course.setSyncMsg(resultVo.getSyncResultMsg());
            }
            List<StudentDevice> studentDevices = studentDeviceDao.queryBuilder().where(
                    StudentDeviceDao.Properties.CourseId.eq(resultVo.getLocalCourseSeq()),
                    StudentDeviceDao.Properties.TeacherIdR.eq(userId)
            ).list();
            for(StudentDevice studentDevice : studentDevices){
                studentDevice.setIsUploaded(isSuccess);
            }
            courseDao.update(course);
            studentDeviceDao.updateInTx(studentDevices);
        }
    }

    public StudentDevice getStudentDeviceByStudent(Long studentIdR, long userId, long courseDefineId) {
        List<StudentDevice> studentDevices = studentDeviceDao.queryBuilder()
                .where(StudentDeviceDao.Properties.StudentIdR.eq(studentIdR),
                      StudentDeviceDao.Properties.TeacherIdR.eq(userId),
                        StudentDeviceDao.Properties.CourseDefineIdR.eq(courseDefineId)).list();
        if(Detect.notEmpty(studentDevices)){
            return studentDevices.get(0);
        }
        return null;
    }

    public List<CourseDefine> initDayCourseData(long teacherId, long instituteId, int weekday) {
        return courseDefineDao.queryBuilder().where(CourseDefineDao.Properties.TeacherIdR.eq(teacherId),
                CourseDefineDao.Properties.InstituteIdR.eq(instituteId),
                CourseDefineDao.Properties.WeekDay.eq(weekday),
                CourseDefineDao.Properties.IsDel.eq(0),
                CourseDefineDao.Properties.UseOnce.eq(CommonConstants.UseOnce.USE_ONCE_NOT.getValue())
        ).orderAsc(CourseDefineDao.Properties.WeekDay).list();
    }

    public List<CourseDefine> getTemporaryCourseDefine(long teacherId, Long instituteId) {
        return courseDefineDao.queryBuilder().where(CourseDefineDao.Properties.TeacherIdR.eq(teacherId),
                CourseDefineDao.Properties.InstituteIdR.eq(instituteId),
                CourseDefineDao.Properties.UseOnce.eq(CommonConstants.UseOnce.USE_ONCE.getValue()),
                CourseDefineDao.Properties.IsDel.eq(0)).orderAsc(CourseDefineDao.Properties.WeekDay).list();
    }

    public void addCourseDefine(CourseDefine courseDefine) {
        courseDefineDao.insert(courseDefine);
    }

    public void updateCourseDefine(CourseDefine courseDefine) {
        courseDefineDao.update(courseDefine);
    }

    public void saveLoginInfoToDB(LoginInfoVo loginInfo) {
        QueryBuilder qb = teacherDao.queryBuilder()
                .where(TeacherDao.Properties.TeacherIdR.eq(loginInfo.getUserId()));
        if(qb.count() > 0){
            DeleteQuery deleteQuery = qb.buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
        }
        Teacher teacher = new Teacher();
        teacher.setLast_login_time(new Date());
        teacher.setMobile(loginInfo.getMobile());
        teacher.setName(loginInfo.getPersonName());
        teacher.setTeacherIdR(loginInfo.getUserId());
        teacherDao.insert(teacher);
    }

    public void updateTeacherInfo(Teacher teacher){
        teacherDao.update(teacher);
    }

    public Teacher getTeacherById(long userId) {
        return teacherDao.load(userId);
    }

    public void delteCourseDefine(long courseDefineId, long userId) {
        List<CourseDefine> courseDefines = courseDefineDao.queryBuilder()
                                                .where(CourseDefineDao.Properties.CourseDefineIdR.eq(courseDefineId),
                                                        CourseDefineDao.Properties.TeacherIdR.eq(userId)).list();
        if(Detect.notEmpty(courseDefines)){
            courseDefineDao.delete(courseDefines.get(0));
        }
    }

    public List<ClassInfo> findAllClass() {
        return classInfoDao.loadAll();
    }

    public Institute findInstituteById(Long currentInstituteIdR) {
        return instituteDao.load(currentInstituteIdR);
    }

    public void updateStudentDevice(StudentDevice studentDevice) {
        studentDeviceDao.update(studentDevice);
    }

    public List<StudentDevice> getStudentDeviceInfoByDeviceNo(long userId, long courseDefindIdR, long studentIdR, String result) {
        return studentDeviceDao.queryBuilder().where(StudentDeviceDao.Properties.TeacherIdR.eq(userId),
                StudentDeviceDao.Properties.CourseDefineIdR.eq(courseDefindIdR),
                StudentDeviceDao.Properties.DeviceNo.eq(result),
                StudentDeviceDao.Properties.StudentIdR.notEq(studentIdR),
                StudentDeviceDao.Properties.IsUploaded.eq(false)).list();
    }

    public void delteStudentDevice(StudentDevice studentDevice) {
        studentDeviceDao.delete(studentDevice);
    }
}
