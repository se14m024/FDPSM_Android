package com.fdpsm.exercise;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.fdpsm.exercise.dao.DaoMaster;
import com.fdpsm.exercise.dao.DaoSession;

public class ExerciseApplication extends Application{

    public DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //debugging
        //getApplicationContext().deleteDatabase(MovieConstants.DATABASE_NAME);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, MovieConstants.DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
