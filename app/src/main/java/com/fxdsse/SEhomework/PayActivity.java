package com.fxdsse.SEhomework;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.Order;
import com.fxdsse.SEhomework.data.model.OrderDao;

import java.util.Date;
import java.util.List;

public class PayActivity extends AppCompatActivity {
    DaoSession daoSession;
    OrderDao orderDao;
    BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        daoSession = ((BMApplication) getApplication()).getDaoSession();
        orderDao = daoSession.getOrderDao();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        findViewById(R.id.tv7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order();
                order.setDate(new Date());
                order.setAddress("陕西省西安市长安区123号XX小区");
                List<Book> list = order.getBooks();

                new AlertDialog.Builder(PayActivity.this)
                        .setView(R.layout.dialog_succ)
                        .show();
            }
        });
    }
}
