package com.straw.lession.physical.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.vo.db.CourseDefine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/15.
 */
public class CourseDefineDao {
    private DBHelper helper;
//    private SQLiteDatabase db;

    public CourseDefineDao(Context context) {
        helper = DBHelper.getInstance(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
//        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     * @param courseDefines
     */
    public void add(List<CourseDefine> courseDefines) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO course_define(code, type, name, instituteId, classId, teacherId, ")
                .append("weekDay,courseDate, courseSeq, courseLocation, startTime, endTime, useOnce) ")
                .append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        try {
            for (CourseDefine courseDefine : courseDefines) {
                db.execSQL(sb.toString(),
                        new Object[]{courseDefine.getCode(), courseDefine.getType(), courseDefine.getName(),
                                courseDefine.getInstituteId(), courseDefine.getClassId(), courseDefine.getTeacherId(),
                                courseDefine.getWeekDay(), courseDefine.getDate(), courseDefine.getSeq(),
                                courseDefine.getLocation(), courseDefine.getStartTime(), courseDefine.getEndTime(),
                                courseDefine.getUseOnce()
                        });
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
            db.close();
        }
    }

    /**
     * update courseDefine's age
     * @param courseDefine
     */
    public void updateName(CourseDefine courseDefine) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("courseName", courseDefine.getName());
        try {
            db.update("courseDefine", cv, "_id = ?", new String[]{String.valueOf(courseDefine.getCourseDefineId())});
        }finally {
            db.close();
        }
    }

    /**
     * delete old courseDefine
     * @param courseDefine
     */
    public void deleteOldPerson(CourseDefine courseDefine) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.delete("course_define", "_id = ?", new String[]{String.valueOf(courseDefine.getCourseDefineId())});
        }finally {
            db.close();
        }
    }

    /**
     * query all persons, return list
     * @return List<Person>
     */
    public List<CourseDefine> queryAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<CourseDefine> courseDefines = new ArrayList<CourseDefine>();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM course_define", null);
            while (c.moveToNext()) {
                courseDefines.add(toCourseDefine(c));
            }
        }finally {
            c.close();
            db.close();
        }
        return courseDefines;
    }

    private CourseDefine toCourseDefine(Cursor c) {
        CourseDefine courseDefine = new CourseDefine();
        courseDefine.setCourseDefineId(c.getInt(c.getColumnIndex("_id")));
        courseDefine.setName(c.getString(c.getColumnIndex("name")));
        courseDefine.setCode(c.getString(c.getColumnIndex("code")));
        courseDefine.setType(c.getString(c.getColumnIndex("type")));
        courseDefine.setInstituteId(c.getLong(c.getColumnIndex("instituteId")));
        courseDefine.setClassId(c.getLong(c.getColumnIndex("classId")));
        courseDefine.setTeacherId(c.getLong(c.getColumnIndex("teacherId")));
        courseDefine.setWeekDay(c.getInt(c.getColumnIndex("weekDay")));
        String courseDateStr = c.getString(c.getColumnIndex("courseDate"));
        courseDefine.setDate(DateUtil.formatStrToDate(courseDateStr));
        courseDefine.setSeq(c.getInt(c.getColumnIndex("courseSeq")));
        courseDefine.setLocation(c.getString(c.getColumnIndex("courseLocation")));
        String startTimeStr = c.getString(c.getColumnIndex("startTime"));
        courseDefine.setStartTime(DateUtil.formatStrToDateTime(startTimeStr));
        String endTimeStr = c.getString(c.getColumnIndex("startTime"));
        courseDefine.setEndTime(DateUtil.formatStrToDateTime(endTimeStr));
        courseDefine.setUseOnce(c.getInt(c.getColumnIndex("useOnce")));
        return courseDefine;
    }

    public List<CourseDefine> query(boolean distinct, String table, String[] columns,
                                    String selection, String[] selectionArgs, String groupBy,
                                    String having, String orderBy, String limit) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<CourseDefine> courseDefines = new ArrayList<CourseDefine>();
        Cursor c = null;
        try {
            c = db.query(distinct, "course_define", columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            while (c.moveToNext()) {
                courseDefines.add(toCourseDefine(c));
            }
        }finally {
            c.close();
            db.close();
        }

        return courseDefines;
    }
}
