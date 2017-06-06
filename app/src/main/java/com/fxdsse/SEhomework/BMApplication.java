package com.fxdsse.SEhomework;

import android.app.Application;
import android.content.SharedPreferences;

import com.fxdsse.SEhomework.data.model.DaoMaster;
import com.fxdsse.SEhomework.data.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by hwding on 5/30/17.
 */

public class BMApplication extends Application {
    private DaoSession daoSession;
    private Long userId = -1l;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                this, "DB"
        );
        Database database = devOpenHelper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();

        SharedPreferences userLoginStatus = getSharedPreferences("user", MODE_PRIVATE);
        if (userLoginStatus.getLong("userId", -2l) == -2l) {
            userLoginStatus.edit().putLong("userId", -1l).apply();
        } else {
            userId = userLoginStatus.getLong("userId", -1l);
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
