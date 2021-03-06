package com.fxdsse.SEhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fxdsse.SEhomework.data.util.BookRawDataImporter;

import java.util.Timer;
import java.util.TimerTask;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.prg_bar);
        TextView textView = (TextView) findViewById(R.id.txt);

        try {
            if (BookRawDataImporter.importToDatabase(this)) {
                progressBar.setVisibility(View.GONE);
                textView.setText("数据更新成功");
            } else {
                progressBar.setVisibility(View.GONE);
                textView.setText("数据已是最新版本");
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            textView.setText("数据导入失败");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
            e.printStackTrace();
        }
    }
}
