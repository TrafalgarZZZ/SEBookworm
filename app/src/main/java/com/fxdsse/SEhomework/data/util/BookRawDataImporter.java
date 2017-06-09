package com.fxdsse.SEhomework.data.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fxdsse.SEhomework.BMApplication;
import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;

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

    public static boolean importToDatabase(Context context) throws Exception {
        InputStream inputStream =
                context.getApplicationContext().getAssets().open("rawData.json");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

        String jsonString = "";
        String thisString;
        while ((thisString = bufferedReader.readLine()) != null)
            jsonString += thisString;

        JSONObject jsonObject = new JSONObject(jsonString);
        String version = jsonObject.getString("version");
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("rawDataVersion", Context.MODE_PRIVATE);

        if (!version.equals(sharedPreferences.getString("version", ""))) {
            JSONArray jsonArray = jsonObject.getJSONArray("books");
            DaoSession daoSession = ((BMApplication) context.getApplicationContext()).getDaoSession();
            BookDao bookDao = daoSession.getBookDao();

            bookDao.deleteAll();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject thisBook = (JSONObject) jsonArray.get(i);
                bookDao.insert(
                        new Book()
                                .setCategory(thisBook.getString("category"))
                                .setDetail(thisBook.getString("details"))
                                .setImageURL(thisBook.getString("img"))
                                .setName(thisBook.getString("name"))
                                .setPrice(thisBook.getString("price"))
                                .setIntroduction(thisBook.getString("intro"))
                );
            }
            sharedPreferences.edit().putString("version", version).apply();
            return true;
        }
        return false;
    }
}
