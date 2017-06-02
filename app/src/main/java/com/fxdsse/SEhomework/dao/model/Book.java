package com.fxdsse.SEhomework.dao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by hwding on 5/30/17.
 */

@Entity
public class Book {

    @Id
    @Generated
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String detail;

    @NotNull
    private String price;

    @NotNull
    private String imageURL;

    @Generated(hash = 1023702191)
    public Book(Long id, @NotNull String name, @NotNull String detail,
                @NotNull String price, @NotNull String imageURL) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.imageURL = imageURL;
    }

    @Generated(hash = 1839243756)
    public Book() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public String getDetail() {
        return this.detail;
    }

    public Book setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getPrice() {
        return this.price;
    }

    public Book setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public Book setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }
}
