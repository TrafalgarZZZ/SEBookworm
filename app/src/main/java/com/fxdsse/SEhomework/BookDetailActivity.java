package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class BookDetailActivity extends AppCompatActivity {
    private Button btn_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

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
