package com.fxdsse.SEhomework.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hwding on 6/7/17.
 */

@Entity
public class UserToBookMapper {
    private Long userId;
    private Long bookId;

    @Generated(hash = 1100148141)
    public UserToBookMapper(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    @Generated(hash = 1414628207)
    public UserToBookMapper() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
