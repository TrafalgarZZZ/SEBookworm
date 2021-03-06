package com.fxdsse.SEhomework.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by hwding on 6/7/17.
 */

@Entity
public class OrderToBookMapper {
    @Id
    @Generated
    private Long id;

    private Long orderId;
    private Long bookId;
    private int quantity;

    @Generated(hash = 1360585113)
    public OrderToBookMapper(Long id, Long orderId, Long bookId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
