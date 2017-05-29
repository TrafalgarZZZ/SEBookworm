package com.fxdsse.SEhomework;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        findViewById(R.id.tv7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(PayActivity.this)
                        .setView(R.layout.dialog_succ)
                        .show();
            }
        });
    }
}
