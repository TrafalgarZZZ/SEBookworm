package com.fxdsse.SEhomework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

    }
}
