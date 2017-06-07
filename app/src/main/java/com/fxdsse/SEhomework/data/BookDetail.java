package com.fxdsse.SEhomework.data;

import java.util.List;

/**
 * Created by hwding on 6/5/17.
 */

public class BookDetail {
    private List<String> authors;
    private String press;
    private String ISBN;
    private String publishedDate;
    private String stackedDate;

    public List<String> getAuthors() {
        return authors;
    }

    public BookDetail setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    public String getPress() {
        return press;
    }

    public BookDetail setPress(String press) {
        this.press = press;
        return this;
    }

    public String getISBN() {
        return ISBN;
    }

    public BookDetail setISBN(String ISBN) {
        this.ISBN = ISBN;
        return this;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public BookDetail setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
        return this;
    }

    public String getStackedDate() {
        return stackedDate;
    }

    public BookDetail setStackedDate(String stackedDate) {
        this.stackedDate = stackedDate;
        return this;
    }
}
