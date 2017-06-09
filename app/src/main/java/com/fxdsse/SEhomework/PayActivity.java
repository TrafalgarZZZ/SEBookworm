package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.Order;
import com.fxdsse.SEhomework.data.model.OrderDao;
import com.fxdsse.SEhomework.data.model.OrderToBookMapper;
import com.fxdsse.SEhomework.data.model.OrderToBookMapperDao;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;
import com.fxdsse.SEhomework.data.model.UserToBookMapper;
import com.fxdsse.SEhomework.data.model.UserToBookMapperDao;
import com.fxdsse.SEhomework.data.model.UserToOrderMapper;
import com.fxdsse.SEhomework.data.model.UserToOrderMapperDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PayActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private OrderDao orderDao;
    private UserDao userDao;
    private UserToBookMapperDao userToBookMapperDao;
    private UserToOrderMapperDao userToOrderMapperDao;
    private OrderToBookMapperDao orderToBookMapperDao;
    private BookDao bookDao;
    private ImageView back_img;
    private TextView payTime;
    private TextView payConfirmPrice;
    private LinearLayout goodsLinearLayout;
    private LinearLayout payConfirmLinearLayout;
    private float totalPrice = 0.0f;
    private boolean isBuyFromCart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        PayActivity.this.setResult(0);
        isBuyFromCart = true;

        totalPrice = 0.0f;
        payTime = (TextView) findViewById(R.id.pay_time);
        payConfirmPrice = (TextView) findViewById(R.id.pay_confirm_price);
        goodsLinearLayout = (LinearLayout) findViewById(R.id.goods_ll);
        payConfirmLinearLayout = (LinearLayout) findViewById(R.id.pay_confirm_ll);
        back_img = (ImageView) findViewById(R.id.search_activity_back);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        daoSession = ((BMApplication) getApplication()).getDaoSession();
        orderDao = daoSession.getOrderDao();
        bookDao = daoSession.getBookDao();
        userDao = daoSession.getUserDao();
        userToBookMapperDao = daoSession.getUserToBookMapperDao();
        userToOrderMapperDao = daoSession.getUserToOrderMapperDao();
        orderToBookMapperDao = daoSession.getOrderToBookMapperDao();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        final List<UserToBookMapper> cart_list;
        Long bookId = getIntent().getLongExtra("book_id", -1);
        if (bookId == -1) {
            cart_list = userToBookMapperDao.queryBuilder().where(UserToBookMapperDao.Properties.UserId.eq(((BMApplication) getApplication()).getUserId())).list();
        } else {
            isBuyFromCart = false;
            cart_list = new ArrayList<>();
            UserToBookMapper userToBookMapper = new UserToBookMapper();
            userToBookMapper.setQuantity(1);
            userToBookMapper.setBookId(bookId);
            userToBookMapper.setUserId(((BMApplication) getApplication()).getUserId());
            cart_list.add(userToBookMapper);
        }

        for (UserToBookMapper relation : cart_list) {
            LinearLayout goodItem = (LinearLayout) LayoutInflater.from(PayActivity.this).inflate(R.layout.goods_item, null);
            TextView goods_name = (TextView) goodItem.findViewById(R.id.gi_name);
            TextView goods_price = (TextView) goodItem.findViewById(R.id.gi_price);
            TextView goods_quantity = (TextView) goodItem.findViewById(R.id.gi_quantity);
            Book goods_book = bookDao.queryBuilder().where(BookDao.Properties.Id.eq(relation.getBookId())).unique();
            float pbook = Float.parseFloat(goods_book.getPrice().replace("￥", ""));
            totalPrice += pbook * relation.getQuantity();
            goods_name.setText(String.format(Locale.CHINA, "%s", goods_book.getName()));
            goods_price.setText(String.format(Locale.CHINA, "￥ %.2f", pbook * relation.getQuantity()));
            goods_quantity.setText(String.format(Locale.CHINA, "x%d", relation.getQuantity()));

            goodsLinearLayout.addView(goodItem);
        }

        payConfirmPrice.setText(String.format(Locale.CHINA, "确认并支付     ￥ %.2f", totalPrice));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        payTime.setText(String.format(Locale.CHINA, "%s-%s-%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)));

        payConfirmLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();
                if (user.getBalance() < totalPrice) {
                    Toast.makeText(PayActivity.this, "余额不足，请充值", Toast.LENGTH_SHORT).show();
                    return;
                }
                Order neworder = new Order();
                Date date = new Date(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
                neworder.setDate(date);
                neworder.setAddress("陕西省西安市长安区123号XX小区");
                neworder.setUserId(((BMApplication) getApplication()).getUserId());
                orderDao.save(neworder);

                UserToOrderMapper userToOrderMapper = new UserToOrderMapper();
                userToOrderMapper.setUserId(((BMApplication) getApplication()).getUserId());
                userToOrderMapper.setOrderId(neworder.getId());
                userToOrderMapperDao.save(userToOrderMapper);

//                List<UserToBookMapper> cart_list = userToBookMapperDao.queryBuilder().where(UserToBookMapperDao.Properties.UserId.eq(((BMApplication) getApplication()).getUserId())).list();
                for (UserToBookMapper relation : cart_list) {
                    OrderToBookMapper orderToBookMapper = new OrderToBookMapper();
                    orderToBookMapper.setOrderId(neworder.getId());
                    orderToBookMapper.setBookId(relation.getBookId());
                    orderToBookMapper.setQuantity(relation.getQuantity());
                    orderToBookMapperDao.save(orderToBookMapper);

                    if (isBuyFromCart) {
                        userToBookMapperDao.delete(relation);
                    }
                }

                user.setBalance(user.getBalance() - totalPrice);

                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                LinearLayout paySucceedLinearLayout = (LinearLayout) LayoutInflater.from(PayActivity.this).inflate(R.layout.dialog_succ, null);
                Button toOrdersButton = (Button) paySucceedLinearLayout.findViewById(R.id.to_orders_btn);
                Button okButton = (Button) paySucceedLinearLayout.findViewById(R.id.ok_btn);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PayActivity.this.setResult(1);
                        PayActivity.this.finish();
                    }
                });

                toOrdersButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PayActivity.this, OrdersActivity.class);
                        startActivity(intent);
                        PayActivity.this.setResult(-1);
                        PayActivity.this.finish();
                    }
                });

                builder.setView(paySucceedLinearLayout);
                builder.show();
            }
        });
    }
}
