package com.fxdsse.gui_design_homework;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LauncherActivity extends AppCompatActivity {

    private Animation iv_animation;
    private Animation txt1_animation;
    private Animation txt2_animation;
    private ImageView imageView;
    private TextView txt1,txt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(LauncherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();

        timer.schedule(task,3000);

        iv_animation = new AlphaAnimation(0.0f,1.0f);
        txt1_animation = new AlphaAnimation(0.0f,1.0f);
        txt2_animation = new AlphaAnimation(0.0f,1.0f);

        iv_animation.setDuration(2000);
        iv_animation.setFillAfter(true);

        txt1_animation.setDuration(1500);
        txt1_animation.setFillAfter(true);
        txt1_animation.setStartOffset(1000);

        txt2_animation.setDuration(1500);
        txt2_animation.setFillAfter(true);
        txt2_animation.setStartOffset(2000);

        imageView = (ImageView)findViewById(R.id.launcher_iv);
        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);


        imageView.startAnimation(iv_animation);
        txt1.startAnimation(txt1_animation);
        txt2.startAnimation(txt2_animation);




    }
}
