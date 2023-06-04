package com.appsnipp.homedesign2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import com.example.namespace.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fm;
    private FragmentTransaction tran;
    private User user;
    private Content_main content_main;
    Streching streching;

    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.navigationMyProfile) {
                fm = getSupportFragmentManager();
                tran = fm.beginTransaction();
                tran.replace(R.id.main_frame, user);
                tran.commit();
                return true; // 선택된 아이템을 표시하도록 true를 반환
            } else if (id == R.id.navigationHome) {
                fm = getSupportFragmentManager();
                tran = fm.beginTransaction();
                tran.replace(R.id.main_frame, content_main);
                tran.commit();
                return true; // 선택된 아이템을 표시하도록 true를 반환
            } else if (id == R.id.navigationStreching) {
                fm = getSupportFragmentManager();
                tran = fm.beginTransaction();
                tran.replace(R.id.main_frame, streching);
                tran.commit();
                return true; // 선택된 아이템을 표시하도록 true를 반환
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return false; // 선택된 아이템이 없으므로 false를 반환
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();
        content_main = new Content_main();
        streching=new Streching();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView = findViewById(R.id.navigation); // 초기화
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // 아래 코드에서 오류가 발생할 수 있으므로 주석 처리합니다.
        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigationMyProfile) {
            fm = getSupportFragmentManager();
            tran = fm.beginTransaction();
            tran.replace(R.id.main_frame, user);
            tran.commit();
            return true; // 선택된 아이템을 표시하도록 true를 반환
        } else if (id == R.id.navigationHome) {
            fm = getSupportFragmentManager();
            tran = fm.beginTransaction();
            tran.replace(R.id.main_frame, content_main);
            tran.commit();
            return true; // 선택된 아이템을 표시하도록 true를 반환
        } else if (id == R.id.navigationStreching) {
            fm = getSupportFragmentManager();
            tran = fm.beginTransaction();
            tran.replace(R.id.main_frame, streching);
            tran.commit();
            return true; // 선택된 아이템을 표시하도록 true를 반환
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false; // 선택된 아이템이 없으므로 false를 반환
    }
}