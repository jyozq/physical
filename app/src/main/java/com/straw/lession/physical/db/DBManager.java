package com.straw.lession.physical.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.straw.lession.physical.vo.db.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by straw on 2016/7/15.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     * @param courses
     */
    public void add(List<Course> courses) {
        db.beginTransaction();  //开始事务
        try {
            for (Course course : courses) {
                db.execSQL("INSERT INTO course_define(courseCode,courseName) VALUES(?, ?)", new Object[]{course.getCourseCode(), course.getCourseName()});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update person's age
     * @param person
     */
    public void updateName(Course person) {
        ContentValues cv = new ContentValues();
        cv.put("courseName", person.getCourseName());
        db.update("person", cv, "_id = ?", new String[]{String.valueOf(person.getCourseDefineId())});
    }

    /**
     * delete old person
     * @param person
     */
    public void deleteOldPerson(Course person) {
        db.delete("person", "_id = ?", new String[]{String.valueOf(person.getCourseDefineId())});
    }

    /**
     * query all persons, return list
     * @return List<Person>
     */
    public List<Course> query() {
        ArrayList<Course> courses = new ArrayList<Course>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Course course = new Course();
            course.setCourseDefineId(c.getInt(c.getColumnIndex("_id")));
            course.setCourseName(c.getString(c.getColumnIndex("courseName")));
            course.setCourseCode(c.getString(c.getColumnIndex("courseCode")));
            courses.add(course);
        }
        c.close();
        return courses;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM course_define", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
