package com.straw.lession.physical.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by straw on 2016/7/15.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "sport4kid.db";
    private static final int DATABASE_VERSION = 1;

    private static DBHelper instance=null;
    public static DBHelper getInstance(Context context){
        if(instance==null){
            instance=new DBHelper(context);
        }
        return instance;
    }
    private DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS institute" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, code VARCHAR, name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS class" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, code VARCHAR, name VARCHAR, " +
                "type INTEGER, totalNum INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS student" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, code VARCHAR, name VARCHAR, " +
                "gender INTEGER, birthday VARCHAR, classId INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS course_define" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, code VARCHAR, type VARCHAR, " +
                "name VARCHAR, instituteId INTEGER, classId INTEGER, teacherId INTEGER, weekDay INTEGER," +
                "courseDate DATE, courseSeq INTEGER, courseLocation VARCHAR, startTime TIME, endTime TIME " +
                "useOnce INTEGER)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE course_define ADD COLUMN other STRING");
    }
}
