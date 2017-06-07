package com.fxdsse.SEhomework.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hwding on 6/7/17.
 */

@Entity
public class UserToOrderMapper {
    private Long userId;
    private Long orderId;

    @Generated(hash = 2022897986)
    public UserToOrderMapper(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }

    @Generated(hash = 455708516)
    public UserToOrderMapper() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
