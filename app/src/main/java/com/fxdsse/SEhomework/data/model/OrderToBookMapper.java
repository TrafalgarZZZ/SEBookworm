package com.fxdsse.SEhomework.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hwding on 6/7/17.
 */

@Entity
public class OrderToBookMapper {
    private Long orderId;
    private Long bookId;

    @Generated(hash = 2128184403)
    public OrderToBookMapper(Long orderId, Long bookId) {
        this.orderId = orderId;
        this.bookId = bookId;
    }

    @Generated(hash = 1904098330)
    public OrderToBookMapper() {
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
