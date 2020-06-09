package com.gosea.captain.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gosea.captain.R;
import com.gosea.captain.controller.adapter.ViewPagerAdapter;
import com.gosea.captain.ui.fragment.AccountFragment;
import com.gosea.captain.ui.fragment.HomeFragment;
import com.gosea.captain.ui.fragment.QueueFragment;
import com.gosea.captain.ui.fragment.TripsFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
    }

    private void initUi() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
            boolean existTrip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
            if (existTrip) {
                int id = sharedPreferences.getInt(getString(R.string.trip_id), 0);
                Intent intent = new Intent(this, TripDetailsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new AccountFragment());
        viewPagerAdapter.addFragment(new QueueFragment());
        viewPagerAdapter.addFragment(new TripsFragment());
        viewPager.setAdapter(viewPagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.home) {
                    viewPager.setCurrentItem(0);
                } else if (item.getItemId()==R.id.account) {
                    viewPager.setCurrentItem(1);
                } else if (item.getItemId()==R.id.queue) {
                    viewPager.setCurrentItem(2);
                } else if (item.getItemId() == R.id.trips) {
                    viewPager.setCurrentItem(3);
                } else {
                    return false;
                }
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d("App Destroyed", "App Destroyed");
        editor.putBoolean(getString(R.string.trip_time), false);
        editor.apply();

    }
}