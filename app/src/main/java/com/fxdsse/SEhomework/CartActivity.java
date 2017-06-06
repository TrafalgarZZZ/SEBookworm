package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private LinearLayout cartLinearLayout;
    private DaoSession daoSession;
    private UserDao userDao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        cartLinearLayout = (LinearLayout) findViewById(R.id.cart_ll);
        daoSession = ((BMApplication) getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();
        user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();

        //// TODO: 2017/6/7  bug to be fixed
        List<Book> cartBooks = user.getBooks();
        Log.e(">>", String.valueOf(cartBooks.size()));
        for (Book book : cartBooks) {
            RelativeLayout bookItem = (RelativeLayout) LayoutInflater.from(CartActivity.this).inflate(R.layout.book_item, null);
            bookItem.setTag(book.getId());
            ImageView imgBook = (ImageView) bookItem.findViewById(R.id.book_pic);
            TextView txtName = (TextView) bookItem.findViewById(R.id.book_name);
            TextView txtDescrption = (TextView) bookItem.findViewById(R.id.book_description);
            TextView txtPrice = (TextView) bookItem.findViewById(R.id.book_price);
            Picasso.with(CartActivity.this).load(book.getImageURL()).into(imgBook);
            txtName.setText(book.getName());
            txtDescrption.setText(book.getDetail());
            txtPrice.setText(book.getPrice());


            bookItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, BookDetailActivity.class);
                    intent.putExtra("book_id", (Long) v.getTag());
                    startActivity(intent);
                }
            });

            cartLinearLayout.addView(bookItem);


        }
    }
}
