package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_Post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post);
        setupBottomNavigationView();
    }
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_statistical) {
                Intent intent = new Intent(getApplicationContext(), Admin_Home.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_unlock) {
                Intent intent = new Intent(getApplicationContext(), Admin_Unlock.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_post) {

                return true;
            } else {
                return false;
            }
        });
    }
}