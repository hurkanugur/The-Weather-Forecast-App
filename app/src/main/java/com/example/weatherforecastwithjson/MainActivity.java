package com.example.weatherforecastwithjson;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new First_Page()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.first_navigation:
                    if(bottomNavigationView.getSelectedItemId() != R.id.first_navigation)
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new First_Page()).commit();
                    break;
                case R.id.second_navigation:
                    if(bottomNavigationView.getSelectedItemId() != R.id.second_navigation)
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Second_Page()).commit();
                    break;
                case R.id.third_navigation:
                    if(bottomNavigationView.getSelectedItemId() != R.id.third_navigation)
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Third_Page()).commit();
                    break;
                case R.id.fourth_navigation:
                    if(bottomNavigationView.getSelectedItemId() != R.id.fourth_navigation)
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fourth_Page()).commit();
                    break;
            }
            return true;
        }
    };
}