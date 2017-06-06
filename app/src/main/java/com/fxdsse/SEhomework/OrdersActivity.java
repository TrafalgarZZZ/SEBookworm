package com.fxdsse.SEhomework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.Order;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class OrdersActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private UserDao userDao;
    private LinearLayout orderList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        orderList = (LinearLayout) findViewById(R.id.orders_list_ll);
        daoSession = ((BMApplication) getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();
        user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();
        List<Order> list = user.getOrders();
        for (Order order : list) {
            LinearLayout orderItem = (LinearLayout) LayoutInflater.from(OrdersActivity.this).inflate(R.layout.order_item, null);

            TextView txtDate = (TextView) orderItem.findViewById(R.id.order_date);
            TextView txtTitle = (TextView) orderItem.findViewById(R.id.book_title);
            TextView txtBookDetail = (TextView) orderItem.findViewById(R.id.book_detail);
            TextView txtOrderDetail = (TextView) orderItem.findViewById(R.id.order_detail);
            ImageView imgBook = (ImageView) orderItem.findViewById(R.id.order_pic);

            List<Book> books = order.getBooks();
            txtDate.setText(order.getDate().toString());
            txtTitle.setText(books.get(0).getName());
            txtBookDetail.setText(books.get(0).getDetail());
            txtOrderDetail.setText(String.format(Locale.CHINA, "共%d本图书 实付款: %s", books.size(), books.get(0).getPrice()));
            Picasso.with(this).load(books.get(0).getImageURL()).into(imgBook);

            orderList.addView(orderItem);

        }

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

    }

}
