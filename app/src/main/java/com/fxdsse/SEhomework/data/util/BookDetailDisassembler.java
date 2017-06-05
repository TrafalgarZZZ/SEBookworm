package com.fxdsse.SEhomework.data.util;

import com.fxdsse.SEhomework.data.model.BookDetail;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hwding on 6/5/17.
 */

public class BookDetailDisassembler {

    public static BookDetail disassembleDetail(String detailsString) {
        String[] details = detailsString.split(" \\| ");

        return new BookDetail()
                .setAuthors(readAuthors(details[0]))
                .setPress(details[1])
                .setISBN(details[2])
                .setPublishedDate(readPublishedDate(details[3]))
                .setStackedDate(readStackedDate(details[4]));
    }

    private static List<String> readAuthors(String authorsString) {
        String authorsStringFormatted = authorsString
                .substring(0, authorsString.lastIndexOf(" （"));
        return Arrays.asList(authorsStringFormatted.split("、"));
    }

    private static String readPublishedDate(String publishedDateString) {
        return publishedDateString.substring(0, publishedDateString.indexOf("出版"));
    }

    private static String readStackedDate(String stackedDateString) {
        return stackedDateString.substring(0, stackedDateString.indexOf(" 上架"));
    }
}
