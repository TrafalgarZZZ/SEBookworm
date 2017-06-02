package com.fxdsse.SEhomework;

import android.app.Application;

import com.fxdsse.SEhomework.dao.model.DaoMaster;
import com.fxdsse.SEhomework.dao.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by hwding on 5/30/17.
 */

public class App extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                this, "DB"
        );
        Database database = devOpenHelper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
