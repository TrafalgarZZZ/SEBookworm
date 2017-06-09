package com.fxdsse.SEhomework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class BookDetailActivity extends AppCompatActivity {
    private Button btn_buy;
    private DaoSession daoSession;
    private BookDao bookDao;
    private UserDao userDao;
    private UserToBookMapperDao userToBookMapperDao;
    private User user = null;
    private long bookId;
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

        daoSession = ((BMApplication) getApplication()).getDaoSession();
        bookDao = daoSession.getBookDao();
        userDao = daoSession.getUserDao();
        userToBookMapperDao = daoSession.getUserToBookMapperDao();
        if (((BMApplication) getApplication()).getUserId() >= 0) {
            user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();
        }
        bookId = getIntent().getLongExtra("book_id", -1);
        book = bookDao.queryBuilder().where(BookDao.Properties.Id.eq(bookId)).unique();
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

        Picasso.with(this).load(book.getImageURL()).resize(540, 780).into(imgBook);

        btn_buy = (Button) findViewById(R.id.bd_buy);
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
                    Toast.makeText(BookDetailActivity.this, "已成功添加至购物车", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
