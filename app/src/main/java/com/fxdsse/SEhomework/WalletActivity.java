package com.fxdsse.SEhomework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;

import java.util.Locale;


public class WalletActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private UserDao userDao;
    private TextView balanceTextView;
    private TextView depositTextView;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        balanceTextView = (TextView) findViewById(R.id.wallet_balance);
        depositTextView = (TextView) findViewById(R.id.wallet_deposit);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        daoSession = ((BMApplication) getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();

        user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(((BMApplication) getApplication()).getUserId())).unique();
        balanceTextView.setText(String.format(Locale.CHINA, "%.2f", user.getBalance()));

        depositTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setBalance(user.getBalance() + 100.0f);
                userDao.update(user);
                balanceTextView.setText(String.format(Locale.CHINA, "%.2f", user.getBalance()));
            }
        });

    }
}
