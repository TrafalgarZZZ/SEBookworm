package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.BookDetail;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.util.BookDetailDisassembler;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private Button btn_buy;
    private DaoSession daoSession;
    private BookDao bookDao;
    private long bookId;
    private TextView txtTitle;
    private TextView txtAuthor;
    private TextView txtPrice;
    private ImageView imgBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        txtTitle = (TextView) findViewById(R.id.bd_title);
        txtPrice = (TextView) findViewById(R.id.bd_price);
        txtAuthor = (TextView) findViewById(R.id.bd_author);
        imgBook = (ImageView) findViewById(R.id.bd_img);

        daoSession = ((App) getApplication()).getDaoSession();
        bookDao = daoSession.getBookDao();
        bookId = getIntent().getLongExtra("book_id", -1);
        Book book = bookDao.queryBuilder().where(BookDao.Properties.Id.eq(bookId)).unique();
        txtTitle.setText(book.getName());
        BookDetail bookDetail = BookDetailDisassembler.disassembleDetail(book.getDetail());
        StringBuilder sb = new StringBuilder();
        List<String> listAuthor = bookDetail.getAuthors();
        for (String str : listAuthor) {
            sb.append(str + ";");
        }
        txtAuthor.setText(sb);
        txtPrice.setText(book.getPrice());
        txtTitle.setText(book.getName());
        Picasso.with(this).load(book.getImageURL()).into(imgBook);

        btn_buy = (Button) findViewById(R.id.bd_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });
    }
}
