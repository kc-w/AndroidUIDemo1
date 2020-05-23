package com.example.androiduidemo1;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.NavigationView navigationview;
    private android.support.v4.widget.DrawerLayout drawerlayout;

    /*创建一个Drawerlayout和Toolbar联动的开关*/
    private ActionBarDrawerToggle toggle;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    fragment1 f1;
    fragment2 f2;
    fragment3 f3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*初始化View*/
        initViews();
        /*隐藏滑动条*/
        hideScrollBar();
        /*设置ActionBar*/
        setActionBar();
        /*设置Drawerlayout开关*/
        setDrawerToggle();
        /*设置监听器*/
        setListener();
    }
    /*初始化View*/
    private void initViews() {
        //获取侧滑控件
        this.drawerlayout = findViewById(R.id.drawer_layout);
        //获取侧滑框
        this.navigationview = findViewById(R.id.navigation_view);
        //获取顶部标题
        this.toolbar =  findViewById(R.id.toolbar);

        f1 = new fragment1();

        //设置fragment
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_context,f1);
        fragmentTransaction.commit();



    }
    /*去掉navigation中的滑动条*/
    private void hideScrollBar() {
        navigationview.getChildAt(0).setVerticalScrollBarEnabled(false);
    }
    /*设置ActionBar*/
    private void setActionBar() {
        setSupportActionBar(toolbar);
        /*显示Home图标*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    /*设置Drawerlayout的开关,并且和Home图标联动*/
    private void setDrawerToggle() {
        toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, 0, 0);
        drawerlayout.addDrawerListener(toggle);
        /*同步drawerlayout的状态*/
        toggle.syncState();
    }
    /*设置监听器*/
    private void setListener() {
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.single_1:
                        f1 = new fragment1();
                        fragmentTransaction.replace(R.id.fragment_context,f1);
                        break;
                    case R.id.single_2:
                        f2 = new fragment2();
                        fragmentTransaction.replace(R.id.fragment_context,f2);
                        break;
                    case R.id.single_3:
                        f3 = new fragment3();
                        fragmentTransaction.replace(R.id.fragment_context,f3);
                        break;
                    case R.id.single_4:
                        break;
                    case R.id.item_1:
                        break;
                    case R.id.item_2:
                        break;
                    case R.id.item_3:
                        break;
                    case R.id.item_4:
                        finish();
                        break;
                }
                //提交事务
                fragmentTransaction.commit();
                //点击后关闭抽屉
                drawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }


    @Override//创建右上角菜单栏选项
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override//菜单点击监听
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings1:
                Toast.makeText(this,"点击了setting1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings2:
                Toast.makeText(this,"点击了setting2",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings3:
                Toast.makeText(this,"点击了setting3",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

}