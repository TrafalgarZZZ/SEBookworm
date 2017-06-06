package com.fxdsse.SEhomework;

import android.app.Application;

import com.fxdsse.SEhomework.data.model.DaoMaster;
import com.fxdsse.SEhomework.data.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by hwding on 5/30/17.
 */

public class BMApplication extends Application {
    private DaoSession daoSession;
    private boolean isLogin = false;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

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
