package com.rocktech.boarddriver;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.rocktech.boarddriver.dao.DaoMaster;
import com.rocktech.boarddriver.dao.DaoSession;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.CrashHandler;

public class BoardDriverApp extends Application {


    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constant.DB_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

}
