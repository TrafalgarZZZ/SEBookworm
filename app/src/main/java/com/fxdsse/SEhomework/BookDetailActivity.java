package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxdsse.SEhomework.data.BookDetail;
import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;
import com.fxdsse.SEhomework.data.model.UserToBookMapper;
import com.fxdsse.SEhomework.data.model.UserToBookMapperDao;
import com.fxdsse.SEhomework.data.util.BookDetailDisassembler;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class BookDetailActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private BookDao bookDao;
    private UserDao userDao;
    private UserToBookMapperDao userToBookMapperDao;
    private User user = null;
    private long bookId;
    private RelativeLayout toCartRelativeLayout;
    private RelativeLayout toSimilarRelativeLayout;
    private TextView txtCartQuantity;
    private TextView btn_buy;
    private TextView btn_buy_now;
    private TextView txtPress;
    private TextView txtTitle;
    private TextView txtAuthor;
    private TextView txtPrice;
    private TextView txtISBN;
    private TextView txtPubTime;
    private TextView txtShelfTime;
    private ImageView imgBook;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        txtTitle = (TextView) findViewById(R.id.bd_title);
        txtPrice = (TextView) findViewById(R.id.bd_price);
        txtAuthor = (TextView) findViewById(R.id.bd_author);
        txtPress = (TextView) findViewById(R.id.bd_press);
        txtISBN = (TextView) findViewById(R.id.bd_isbn);
        txtPubTime = (TextView) findViewById(R.id.bd_pubtime);
        txtShelfTime = (TextView) findViewById(R.id.bd_shelftime);
        imgBook = (ImageView) findViewById(R.id.bd_img);

        toCartRelativeLayout = (RelativeLayout) findViewById(R.id.cart_rl);
        toSimilarRelativeLayout = (RelativeLayout) findViewById(R.id.similar_rl);
        txtCartQuantity = (TextView) findViewById(R.id.cart_quantity);


        daoSession = ((BMApplication) getApplication()).getDaoSession();
        bookDao = daoSession.getBookDao();
        userDao = daoSession.getUserDao();
        userToBookMapperDao = daoSession.getUserToBookMapperDao();
        if (((BMApplication) getApplication()).getUserId() >= 0) {
            user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();
        }
        bookId = getIntent().getLongExtra("book_id", -1);
        book = bookDao.queryBuilder().where(BookDao.Properties.Id.eq(bookId)).unique();
        if (book == null) {
            Toast.makeText(BookDetailActivity.this, "获取书籍信息失败", Toast.LENGTH_SHORT).show();
            finish();
        }
        txtTitle.setText(book.getName());
        BookDetail bookDetail = BookDetailDisassembler.disassembleDetail(book.getDetail());
        List<String> listAuthor = bookDetail.getAuthors();
        txtAuthor.setText(listAuthor.get(0));
        txtPrice.setText(book.getPrice());
        txtTitle.setText(book.getName());
        txtPress.setText(bookDetail.getPress());
        txtISBN.setText(bookDetail.getISBN());
        txtPubTime.setText(bookDetail.getPublishedDate());
        txtShelfTime.setText(bookDetail.getStackedDate());

        Picasso.with(this).load(book.getImageURL()).resize(450, 650).into(imgBook);

        refreshCartQuantity();

        btn_buy = (TextView) findViewById(R.id.bd_buy);
        btn_buy_now = (TextView) findViewById(R.id.bd_buy_now);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    UserToBookMapper relation = userToBookMapperDao
                            .queryBuilder()
                            .where(UserToBookMapperDao.Properties.BookId.eq(book.getId()))
                            .where(UserToBookMapperDao.Properties.UserId.eq(user.getId()))
                            .unique();
                    if (relation == null) {
                        UserToBookMapper newRelation = new UserToBookMapper();
                        newRelation.setUserId(user.getId());
                        newRelation.setBookId(book.getId());
                        newRelation.setQuantity(1);
                        userToBookMapperDao.save(newRelation);
                    } else {
                        relation.setQuantity(relation.getQuantity() + 1);
                        userToBookMapperDao.update(relation);
                    }
                    refreshCartQuantity();
                    Toast.makeText(BookDetailActivity.this, "已成功添加至购物车", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Intent intent = new Intent(BookDetailActivity.this, PayActivity.class);
                    intent.putExtra("book_id", bookId);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(BookDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });

        toCartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Intent intent = new Intent(BookDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(BookDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toSimilarRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this, CategoryActivity.class);
                intent.putExtra("category", book.getCategory());
                startActivity(intent);
            }
        });

    }

    private void refreshCartQuantity() {
        if (user != null) {
            List<UserToBookMapper> list = userToBookMapperDao.queryBuilder().where(UserToBookMapperDao.Properties.UserId.eq(user.getId())).list();
            if (list.size() == 0) {
                txtCartQuantity.setVisibility(View.INVISIBLE);
            } else {
                txtCartQuantity.setVisibility(View.VISIBLE);
                txtCartQuantity.setText(String.format(Locale.CHINA, "  %d  ", list.size()));
            }
        } else {
            txtCartQuantity.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            switch (resultCode) {
                case -1:
                    finish();
                    break;
                case 1:
                    refreshCartQuantity();
                    break;
            }
        }
    }
}
