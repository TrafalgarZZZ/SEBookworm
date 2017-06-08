package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;
import com.fxdsse.SEhomework.data.model.UserToBookMapper;
import com.fxdsse.SEhomework.data.model.UserToBookMapperDao;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private LinearLayout cartLinearLayout;
    private DaoSession daoSession;
    private UserDao userDao;
    private BookDao bookDao;
    private User user;
    private UserToBookMapperDao userToBookMapperDao;
    private TextView cartPrice;
    private TextView cartPay;
    private float totalPrice = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));



        cartPrice = (TextView) findViewById(R.id.pay_price);
        cartPay = (TextView) findViewById(R.id.pay_btn);
        cartLinearLayout = (LinearLayout) findViewById(R.id.cart_ll);
        daoSession = ((BMApplication) getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();
        bookDao = daoSession.getBookDao();
        userToBookMapperDao = daoSession.getUserToBookMapperDao();
        user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();

        refreshCart();

        cartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalPrice != 0.0f) {
                    Intent intent = new Intent(CartActivity.this, PayActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(CartActivity.this, "无商品可结算", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void refreshCart() {
        totalPrice = 0.0f;
        cartLinearLayout.removeAllViews();
        List<UserToBookMapper> cartBookRelations = userToBookMapperDao.queryBuilder().where(UserToBookMapperDao.Properties.UserId.eq(user.getId())).list();
        for (UserToBookMapper relation : cartBookRelations) {
            final Book book = bookDao.queryBuilder().where(BookDao.Properties.Id.eq(relation.getBookId())).unique();
            SwipeMenuLayout bookItem = (SwipeMenuLayout) LayoutInflater.from(CartActivity.this).inflate(R.layout.cart_item, null);
            bookItem.setTag(book.getId());
            ImageView imgBook = (ImageView) bookItem.findViewById(R.id.book_pic);
            TextView txtName = (TextView) bookItem.findViewById(R.id.book_name);
            TextView txtDescrption = (TextView) bookItem.findViewById(R.id.book_description);
            final TextView txtPrice = (TextView) bookItem.findViewById(R.id.book_price);
            TextView txtQuantity = (TextView) bookItem.findViewById(R.id.book_quantity);
            Button delButton = (Button) bookItem.findViewById(R.id.book_del);
            final RelativeLayout relativeLayout = (RelativeLayout) bookItem.findViewById(R.id.book);
            delButton.setTag(relation.getId());
            Picasso.with(CartActivity.this).load(book.getImageURL()).into(imgBook);
            txtName.setText(book.getName());
            txtDescrption.setText(book.getDetail());
            txtPrice.setText(book.getPrice());
            if (relation.getQuantity() > 1) {
                txtQuantity.setText(String.format(Locale.CHINA, "X %d", relation.getQuantity()));
            } else {
                txtQuantity.setText("");
            }

            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserToBookMapper relation = userToBookMapperDao.queryBuilder().where(UserToBookMapperDao.Properties.Id.eq(v.getTag())).unique();
                    Book book = bookDao.queryBuilder().where(BookDao.Properties.Id.eq(relation.getBookId())).unique();
                    totalPrice -= Float.parseFloat(book.getPrice().replace("￥", "")) * relation.getQuantity();
                    cartPrice.setText(String.format(Locale.CHINA, "￥ %.2f", totalPrice));
                    userToBookMapperDao.deleteByKey((Long) v.getTag());
                    cartLinearLayout.removeView((SwipeMenuLayout) v.getParent());

                }
            });

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, BookDetailActivity.class);
                    intent.putExtra("book_id", (Long) ((View) v.getParent()).getTag());
                    startActivity(intent);
                }
            });
            cartLinearLayout.addView(bookItem);

            totalPrice += Float.parseFloat(book.getPrice().replace("￥", "")) * relation.getQuantity();
        }
        cartPrice.setText(String.format(Locale.CHINA, "￥ %.2f", totalPrice));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == 1) {
                refreshCart();
            }
        }
    }
}
