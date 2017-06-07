package com.fxdsse.SEhomework.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by hwding on 6/7/17.
 */

@Entity
public class UserToBookMapper {
    @Id
    @Generated
    private Long id;

    private Long userId;
    private Long bookId;
    private int quantity;

    @Generated(hash = 1637806133)
    public UserToBookMapper(Long id, Long userId, Long bookId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.quantity = quantity;
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

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
