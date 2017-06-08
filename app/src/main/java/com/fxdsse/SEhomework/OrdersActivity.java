package com.fxdsse.SEhomework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.Order;
import com.fxdsse.SEhomework.data.model.OrderDao;
import com.fxdsse.SEhomework.data.model.OrderToBookMapper;
import com.fxdsse.SEhomework.data.model.OrderToBookMapperDao;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;
import com.fxdsse.SEhomework.data.model.UserToOrderMapper;
import com.fxdsse.SEhomework.data.model.UserToOrderMapperDao;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class OrdersActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private UserDao userDao;
    private UserToOrderMapperDao userToOrderMapperDao;
    private OrderToBookMapperDao orderToBookMapperDao;
    private OrderDao orderDao;
    private BookDao bookDao;
    private LinearLayout orderList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        orderList = (LinearLayout) findViewById(R.id.orders_list_ll);
        daoSession = ((BMApplication) getApplication()).getDaoSession();
        orderDao = daoSession.getOrderDao();
        userDao = daoSession.getUserDao();
        bookDao = daoSession.getBookDao();
        orderToBookMapperDao = daoSession.getOrderToBookMapperDao();
        userToOrderMapperDao = daoSession.getUserToOrderMapperDao();
        user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();
        List<UserToOrderMapper> list = userToOrderMapperDao.queryBuilder().where(UserToOrderMapperDao.Properties.UserId.eq(user.getId())).list();
        for (UserToOrderMapper order : list) {
            LinearLayout orderItem = (LinearLayout) LayoutInflater.from(OrdersActivity.this).inflate(R.layout.order_item, null);
            TextView txtDate = (TextView) orderItem.findViewById(R.id.order_date);
            TextView txtOrderDetail = (TextView) orderItem.findViewById(R.id.order_detail);
            LinearLayout orderContentLinearLayout = (LinearLayout) orderItem.findViewById(R.id.order_content_ll);

            Order neworder = orderDao.queryBuilder().where(OrderDao.Properties.Id.eq(order.getOrderId())).unique();
            txtDate.setText(String.format(Locale.CHINA, "%d-%d-%d", neworder.getDate().getYear(), neworder.getDate().getMonth(), neworder.getDate().getDate()));

            List<OrderToBookMapper> order_books = orderToBookMapperDao.queryBuilder().where(OrderToBookMapperDao.Properties.OrderId.eq(order.getId())).list();
            int quantity = 0;
            float price = 0.0f;
            for (OrderToBookMapper order_book : order_books) {
                Book book = bookDao.queryBuilder().where(BookDao.Properties.Id.eq(order_book.getBookId())).unique();
                RelativeLayout oc_book = (RelativeLayout) LayoutInflater.from(OrdersActivity.this).inflate(R.layout.order_content_item, null);
                ImageView img_book = (ImageView) oc_book.findViewById(R.id.oc_pic);
                TextView oc_name = (TextView) oc_book.findViewById(R.id.oc_book_name);
                TextView oc_price = (TextView) oc_book.findViewById(R.id.oc_price);
                TextView oc_quantity = (TextView) oc_book.findViewById(R.id.oc_quantity);

                oc_name.setText(book.getName());
                oc_price.setText(book.getPrice());
                oc_quantity.setText(String.format("x%d", order_book.getQuantity()));
                orderContentLinearLayout.addView(oc_book);
                Picasso.with(OrdersActivity.this).load(book.getImageURL()).into(img_book);

                quantity += order_book.getQuantity();
                price += Float.parseFloat(book.getPrice().replace("￥", "")) * order_book.getQuantity();
            }
            txtOrderDetail.setText(String.format(Locale.CHINA, "共%d本图书 实付款: ￥ %.2f", quantity, price));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 55, 0, 0);
            orderList.addView(orderItem, params);
        }

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

    }

}
