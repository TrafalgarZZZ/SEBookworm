package com.fxdsse.SEhomework.dao;

import android.content.Context;

import com.fxdsse.SEhomework.App;
import com.fxdsse.SEhomework.dao.model.DaoSession;
import com.fxdsse.SEhomework.dao.model.User;
import com.fxdsse.SEhomework.dao.model.UserDao;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by hwding on 6/5/17.
 */

public class UserSessionManager {

    public static User login(
            String username,
            String password,
            Context context) {
        DaoSession daoSession = ((App) context.getApplicationContext()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        return userDao.queryBuilder()
                .where(UserDao.Properties.Username.eq(username))
                .where(UserDao.Properties.PasswordHash.eq(DigestUtils.md5Hex(password)))
                .build().unique();
    }

    public static User signup(
            String username,
            String password,
            Context context) {
        DaoSession daoSession = ((App) context.getApplicationContext()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        if (userDao.queryBuilder()
                .where(UserDao.Properties.Username.eq(username))
                .build()
                .unique() == null) {
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(DigestUtils.md5Hex(password));
            userDao.save(user);

            return user;
        } else
            return null;
    }
}
