package com.fxdsse.SEhomework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class FavorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.enterinto_favor_food1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavorActivity.this,ShopActivity.class);
                startActivity(intent);
            }
        });
    }
}
