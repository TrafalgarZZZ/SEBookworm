package com.fxdsse.SEhomework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.Order;
import com.fxdsse.SEhomework.data.model.OrderDao;
import com.fxdsse.SEhomework.data.model.OrderToBookMapper;
import com.fxdsse.SEhomework.data.model.OrderToBookMapperDao;
import com.fxdsse.SEhomework.data.model.UserToBookMapper;
import com.fxdsse.SEhomework.data.model.UserToBookMapperDao;
import com.fxdsse.SEhomework.data.model.UserToOrderMapper;
import com.fxdsse.SEhomework.data.model.UserToOrderMapperDao;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PayActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private OrderDao orderDao;
    private UserToBookMapperDao userToBookMapperDao;
    private UserToOrderMapperDao userToOrderMapperDao;
    private OrderToBookMapperDao orderToBookMapperDao;
    private BookDao bookDao;
    private TextView payTime;
    private TextView payConfirmPrice;
    private LinearLayout goodsLinearLayout;
    private LinearLayout payConfirmLinearLayout;
    private float totalPrice = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        PayActivity.this.setResult(0);

        totalPrice = 0.0f;
        payTime = (TextView) findViewById(R.id.pay_time);
        payConfirmPrice = (TextView) findViewById(R.id.pay_confirm_price);
        goodsLinearLayout = (LinearLayout) findViewById(R.id.goods_ll);
        payConfirmLinearLayout = (LinearLayout) findViewById(R.id.pay_confirm_ll);

        daoSession = ((BMApplication) getApplication()).getDaoSession();
        orderDao = daoSession.getOrderDao();
        bookDao = daoSession.getBookDao();
        userToBookMapperDao = daoSession.getUserToBookMapperDao();
        userToOrderMapperDao = daoSession.getUserToOrderMapperDao();
        orderToBookMapperDao = daoSession.getOrderToBookMapperDao();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        List<UserToBookMapper> cart_list = userToBookMapperDao.queryBuilder().where(UserToBookMapperDao.Properties.UserId.eq(((BMApplication) getApplication()).getUserId())).list();
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
            goods_quantity.setText(String.format(Locale.CHINA, "X %d", relation.getQuantity()));

            goodsLinearLayout.addView(goodItem);
        }

        payConfirmPrice.setText(String.format(Locale.CHINA, "确认并支付     ￥ %.2f", totalPrice));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        payTime.setText(String.format(Locale.CHINA, "%s-%s-%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)));

        payConfirmLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order neworder = new Order();
                neworder.setDate(Calendar.getInstance().getTime());
                neworder.setAddress("陕西省西安市长安区123号XX小区");
                neworder.setUserId(((BMApplication) getApplication()).getUserId());
                orderDao.save(neworder);

                Log.e(">>", String.valueOf(neworder.getId()));

                UserToOrderMapper userToOrderMapper = new UserToOrderMapper();
                userToOrderMapper.setUserId(((BMApplication) getApplication()).getUserId());
                userToOrderMapper.setOrderId(neworder.getId());
                userToOrderMapperDao.save(userToOrderMapper);

                List<UserToBookMapper> cart_list = userToBookMapperDao.queryBuilder().where(UserToBookMapperDao.Properties.UserId.eq(((BMApplication) getApplication()).getUserId())).list();
                for (UserToBookMapper relation : cart_list) {
                    OrderToBookMapper orderToBookMapper = new OrderToBookMapper();
                    orderToBookMapper.setOrderId(neworder.getId());
                    orderToBookMapper.setBookId(relation.getBookId());
                    orderToBookMapper.setQuantity(relation.getQuantity());
                    orderToBookMapperDao.save(orderToBookMapper);

                    userToBookMapperDao.delete(relation);
                }
                PayActivity.this.setResult(1);
                PayActivity.this.finish();
            }
        });
    }
}
