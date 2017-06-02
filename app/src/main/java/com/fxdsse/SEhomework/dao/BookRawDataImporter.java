package com.fxdsse.SEhomework.dao;

import android.content.Context;

import com.fxdsse.SEhomework.App;
import com.fxdsse.SEhomework.dao.model.Book;
import com.fxdsse.SEhomework.dao.model.BookDao;
import com.fxdsse.SEhomework.dao.model.DaoSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hwding on 5/30/17.
 */

public class BookRawDataImporter {

    public static void importToDatabase(Context context) throws Exception {
        InputStream inputStream =
                context.getApplicationContext().getAssets().open("rawData.json");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

        String jsonString = "";
        String thisString;
        while ((thisString = bufferedReader.readLine()) != null)
            jsonString += thisString;

        JSONArray jsonArray = new JSONArray(jsonString);
        DaoSession daoSession = ((App) context.getApplicationContext()).getDaoSession();
        BookDao bookDao = daoSession.getBookDao();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject thisBook = (JSONObject) jsonArray.get(i);
            bookDao.insert(
                    new Book()
                            .setDetail(thisBook.getString("details"))
                            .setImageURL(thisBook.getString("img"))
                            .setName(thisBook.getString("name"))
                            .setPrice(thisBook.getString("price"))
            );
        }
    }
}
