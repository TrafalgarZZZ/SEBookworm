package com.fxdsse.SEhomework;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxdsse.SEhomework.Fragments.CategoryFragment;
import com.fxdsse.SEhomework.Fragments.HomeFragment;
import com.fxdsse.SEhomework.data.model.DaoSession;
import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.model.UserDao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CategoryFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener {

    private long backPressedTimeAtMills = 0;
    private TabLayout tabLayout;
    private Fragment current_fragment;
    private FragmentManager fragmentManager;
    private ImageView imageViewUser;
    private TextView txtUser;
    private DaoSession daoSession;
    private UserDao userDao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.category_tablayout);
        fragmentManager = getSupportFragmentManager();
        setSupportActionBar(toolbar);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        prepareTabLayout();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        daoSession = ((BMApplication) getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();

        imageViewUser = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_user);
        txtUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_user);

        if (((BMApplication) getApplication()).getUserId() >= 0) {
            Long userId = ((BMApplication) getApplication()).getUserId();
            user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(userId)).unique();
            txtUser.setText(user.getUsername());
        }

        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(">>", String.valueOf(((BMApplication) getApplication()).getUserId()));
                if (((BMApplication) getApplication()).getUserId() < 0) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(loginIntent, 0);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("注销当前账户");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "已注销", Toast.LENGTH_SHORT).show();
                            txtUser.setText("未登录");
                            ((BMApplication) getApplication()).setUserId(-1l);
                            getApplication().getSharedPreferences("user", MODE_PRIVATE).edit().putLong("userId", -1l).apply();

                        }
                    });
                    builder.show();

                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == 1) {
                    Long userId = data.getLongExtra("userId", -1);
                    if (userId != -1) {
                        ((BMApplication) getApplication()).setUserId(userId);
                        user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(userId)).unique();
                        txtUser.setText(user.getUsername());
                        getApplication().getSharedPreferences("user", MODE_PRIVATE).edit().putLong("userId", user.getId()).apply();
                    }

                }
        }
    }

    private void prepareTabLayout() {
        final Fragment fragment_home = HomeFragment.newInstance("", "");
        final Fragment fragment_category = CategoryFragment.newInstance("", "");
        switch_fragment(fragment_home);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        switch_fragment(fragment_home);
                        break;
                    case 1:
                        switch_fragment(fragment_category);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void switch_fragment(Fragment to_fragment) {
        if (current_fragment != to_fragment) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (current_fragment != null)
                fragmentTransaction.hide(current_fragment);

            if (!to_fragment.isAdded())
                fragmentTransaction.add(R.id.frag_container, to_fragment);
            else
                fragmentTransaction.show(to_fragment);

            fragmentTransaction.commit();
            current_fragment = to_fragment;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTimeAtMills == 0) {
                backPressedTimeAtMills = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "再按一次返回退出", Toast.LENGTH_SHORT).show();
            } else if (System.currentTimeMillis() - backPressedTimeAtMills <= 3000) {
                super.onBackPressed();
            } else {
                backPressedTimeAtMills = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "再按一次返回退出", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            if (((BMApplication) getApplication()).getUserId() < 0) {
                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_orders) {
            if (((BMApplication) getApplication()).getUserId() < 0) {
                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(MainActivity.this, WalletActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_position) {
            Intent intent = new Intent(MainActivity.this, PositionActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
