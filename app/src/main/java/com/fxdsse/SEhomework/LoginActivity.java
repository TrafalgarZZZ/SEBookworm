package com.fxdsse.SEhomework;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.service.UserService;


public class LoginActivity extends AppCompatActivity {
    private Button signupButton;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private CheckBox pwd_visible;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        //set fonts.
        TextView loginLogo = (TextView) findViewById(R.id.logo_login);
        String fonts = "fonts/logoFonts.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), fonts);
        loginLogo.setTypeface(typeface);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_pwd);
        signupButton = (Button) findViewById(R.id.btn_signUp);
        loginButton = (Button) findViewById(R.id.btn_login);
        pwd_visible = (CheckBox) findViewById(R.id.iv_display);

        username.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        resultIntent = new Intent();

        this.setResult(0);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "账户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = UserService.login(username.getText().toString(), password.getText().toString(), LoginActivity.this);
                if (user == null) {
                    Toast.makeText(LoginActivity.this, "账户名或密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "已登录", Toast.LENGTH_SHORT).show();
                    resultIntent.putExtra("userId", user.getId());
                    LoginActivity.this.setResult(1, resultIntent);
                    finish();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "账户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.getText().toString().length() > 10) {
                    Toast.makeText(LoginActivity.this, "用户名过长", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().length() > 10) {
                    Toast.makeText(LoginActivity.this, "密码过长", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = UserService.signUp(username.getText().toString(), password.getText().toString(), LoginActivity.this);
                if (user == null) {
                    Toast.makeText(LoginActivity.this, "账户名已经存在", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "已成功注册", Toast.LENGTH_SHORT).show();
                    resultIntent.putExtra("userId", user.getId());
                    LoginActivity.this.setResult(1, resultIntent);
                    finish();
                }
            }
        });

        pwd_visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }
}
