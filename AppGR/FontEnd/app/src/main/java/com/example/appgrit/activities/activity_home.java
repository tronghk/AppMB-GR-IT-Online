package com.example.appgrit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.appgrit.HomeFragment;
import com.example.appgrit.ProfileOther;
import com.example.appgrit.R;
import com.example.appgrit.UserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Thiết lập BottomNavigationView và xử lý sự kiện chọn item
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                Intent intent;

                // Sử dụng if-else để xử lý chọn item
                if (item.getItemId() == R.id.nav_home) {
                    // Chuyển đến ProfileOther Activity
                    intent = new Intent(activity_home.this, activity_home.class);
                    startActivity(intent);
                    return true; // Không cần thực hiện các bước sau khi đã chuyển Activity
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Chuyển đến ProfileOther Activity
                    intent = new Intent(activity_home.this, ProfileOther.class);
                    startActivity(intent);
                    return true; // Không cần thực hiện các bước sau khi đã chuyển Activity
                } else if (item.getItemId() == R.id.nav_users) {
                    // Chuyển đến UserActivity
                    intent = new Intent(activity_home.this, UserActivity.class);
                    startActivity(intent);
                    return true;
                }// Không cần thực hiện các bước sau khi đã chuyển Activity
//                } else if (item.getItemId() == R.id.nav_chat) {
//                    // Thay thế bằng fragment Chat của bạn
//                    selectedFragment = new ChatFragment();
//                } else if (item.getItemId() == R.id.nav_more) {
//                    // Thay thế bằng fragment More của bạn
//                    selectedFragment = new MoreFragment();
//                }

                // Thực hiện việc thay thế Fragment nếu selectedFragment khác null
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }

                return false;
            }
        });
    }
}
