package com.fxdsse.SEhomework.data.util;

import android.content.Context;

import com.fxdsse.SEhomework.BMApplication;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by hwding on 6/5/17.
 */

public class UserSessionManager {
    public static User login(
            String username,
            String password,
            Context context) {
        DaoSession daoSession = ((BMApplication) context.getApplicationContext()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        return userDao.queryBuilder()
                .where(UserDao.Properties.Username.eq(username))
                .where(UserDao.Properties.PasswordHash.eq(new String(Hex.encodeHex(DigestUtils.md5(password)))))
                .build().unique();
    }

    public static User signUp(
            String username,
            String password,
            Context context) {
        DaoSession daoSession = ((BMApplication) context.getApplicationContext()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        if (userDao.queryBuilder()
                .where(UserDao.Properties.Username.eq(username))
                .build()
                .unique() == null) {
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(new String(Hex.encodeHex(DigestUtils.md5(password))));
            userDao.save(user);
            return user;
        } else
            return null;
    }

    public static User delete(
            String username,
            String password,
            Context context
    ) {
        DaoSession daoSession = ((BMApplication) context.getApplicationContext()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(new String(Hex.encodeHex(DigestUtils.md5(password))));
        if (userDao.queryBuilder()
                .where(UserDao.Properties.Username.eq(username))
                .where(UserDao.Properties.PasswordHash.eq(new String(Hex.encodeHex(DigestUtils.md5(password)))))
                .build().unique() != null) {
            userDao.delete(user);
            return user;
        } else
            return null;
    }
}
