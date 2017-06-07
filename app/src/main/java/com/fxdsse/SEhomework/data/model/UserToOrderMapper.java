package com.fxdsse.SEhomework.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by hwding on 6/7/17.
 */

@Entity
public class UserToOrderMapper {
    @Id
    @Generated
    private Long id;

    private Long userId;
    private Long orderId;

    @Generated(hash = 1529807471)
    public UserToOrderMapper(Long id, Long userId, Long orderId) {
        this.id = id;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
