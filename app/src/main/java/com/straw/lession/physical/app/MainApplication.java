package com.straw.lession.physical.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.anupcowkur.reservoir.Reservoir;
import com.straw.lession.physical.db.DaoMaster;
import com.straw.lession.physical.db.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by straw on 2016/7/2.
 */
public class MainApplication extends Application{
    private static final String TAG = "MainApplication";
    // 全局线程池
    private ThreadPoolExecutor mThreadPool;
    private final int core_thread_num = 5;
    private final int max_thread_num = 10;
    private final int keep_alive_time = 5000;
    private final int block_queue_size = 20;
    private final BlockingQueue<Runnable> blockQueue = new ArrayBlockingQueue<Runnable>(block_queue_size);

    private static List<Activity> activitys = null;
    private static MainApplication instance;

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static Database db;
    //数据库名，表名是自动被创建的
    public static final String DB_NAME = "sport4kid.db";

    public static MainApplication getInstance() {
        if (null == instance) {
            instance = new MainApplication();
        }
        if (null == activitys ){
            activitys = new LinkedList<Activity>();
        }
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        //MobCreditEase.setDebug(true);

        activitys = new LinkedList<Activity>();
        instance = this;
        init();
    }

    public static MainApplication getApplication() {
        return instance;
    }

    private void init() {
        if (mThreadPool == null) {
            mThreadPool = new ThreadPoolExecutor(core_thread_num,
                    max_thread_num, keep_alive_time, TimeUnit.MILLISECONDS,
                    blockQueue, new ThreadPoolExecutor.DiscardOldestPolicy());
        }

        try{
            Reservoir.init(this, 5120);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        QueryBuilder.LOG_SQL=true;
        QueryBuilder.LOG_VALUES=true;
    }

    public ThreadPoolExecutor getThreadPool() {
        return mThreadPool;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();   //告诉系统回收
    }

    public void exitApp() {
        if (mThreadPool != null) {
            mThreadPool.shutdown();
        }
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if(!activitys.contains(activity)){
                activitys.add(activity);
            }
        }else{
            activitys.add(activity);
        }
    }

    //移除某个Activity
    public void removeActivity(Class cls){
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                if (activity.getClass().equals(cls)){
                    activity.finish();
                    activitys.remove(activity);
                    activity = null;
                    break;
                }
            }
        }
    }

    public void popActivity(Activity activity){
        if (activity !=null){
            activity.finish();
            activitys.remove(activity);
            activity = null;
        }
    }

    public void popCurrentActivity(){
        Activity activity =  activitys.get(activitys.size() - 1);
        if (activity !=null){
            activity.finish();
            activitys.remove(activity);
            activity = null;
        }
    }

    public Activity getLastCurrentActivity(){
        Activity activity =  activitys.get(activitys.size() - 1);
        if (activity !=null){
            return activity;
        }
        return null;
    }

    public void exit2() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
    }

    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
                activity = null;
            }
            activitys.clear();
        }
    }

    public DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }


    public DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    public Database getSQLDatebase(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            db = daoMaster.getDatabase();
        }
        return db;
    }

}
