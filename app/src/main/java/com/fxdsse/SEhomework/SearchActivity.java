package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxdsse.SEhomework.data.BookDetail;
import com.fxdsse.SEhomework.data.model.Book;
import com.fxdsse.SEhomework.data.model.BookDao;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.util.BookDetailDisassembler;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ImageView back_img;
    private EditText searchEditText;
    private DaoSession daoSession;
    private BookDao bookDao;
    private LinearLayout searchResultLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        daoSession = ((BMApplication) getApplication()).getDaoSession();
        bookDao = daoSession.getBookDao();

        searchEditText = (EditText) findViewById(R.id.search_edittxt);
        back_img = (ImageView) findViewById(R.id.search_activity_back);
        searchResultLinearLayout = (LinearLayout) findViewById(R.id.search_result_ll);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    boolean flag = false;
                    List<Book> allBooks = bookDao.queryBuilder().list();
                    searchResultLinearLayout.removeAllViews();
                    for (Book book : allBooks) {
                        if (book.getName().contains(v.getText())) {
                            flag = true;
                            RelativeLayout bookItem = (RelativeLayout) LayoutInflater.from(SearchActivity.this).inflate(R.layout.book_item, null);
                            bookItem.setTag(book.getId());
                            ImageView imgBook = (ImageView) bookItem.findViewById(R.id.book_pic);
                            TextView txtName = (TextView) bookItem.findViewById(R.id.book_name);
                            TextView txtPress = (TextView) bookItem.findViewById(R.id.book_press);
                            TextView txtPrice = (TextView) bookItem.findViewById(R.id.book_price);
                            TextView txtAuthor = (TextView) bookItem.findViewById(R.id.book_author);

                            BookDetail detail = BookDetailDisassembler.disassembleDetail(book.getDetail());


                            Picasso.with(SearchActivity.this).load(book.getImageURL()).into(imgBook);
                            txtName.setText(book.getName());
                            txtPress.setText(detail.getPress());
                            txtPrice.setText(book.getPrice());

                            StringBuffer sb = new StringBuffer();
                            List<String> authors = detail.getAuthors();
                            int authors_size = authors.size();
                            for (int i = 0; i < authors_size; i++) {
                                sb.append(authors.get(i));
                                if (i != authors_size - 1) {
                                    sb.append(";");
                                }
                            }
                            txtAuthor.setText(sb);


                            bookItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                                    intent.putExtra("book_id", (Long) v.getTag());
                                    startActivity(intent);
                                }
                            });

                            searchResultLinearLayout.addView(bookItem);
                        }
                    }
                    if (!flag) {
                        Toast.makeText(SearchActivity.this, "未找到相关图书", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

    }

}
