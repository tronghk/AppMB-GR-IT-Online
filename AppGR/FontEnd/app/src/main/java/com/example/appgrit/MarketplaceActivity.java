package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appgrit.activities.CreateMarketplace;

public class MarketplaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        // Ánh xạ nút "Đăng bài"
        Button btnUpload = findViewById(R.id.btn_upload);

        // Thiết lập sự kiện lắng nghe cho nút "Đăng bài"
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang trang CreateMarketplace
                Intent intent = new Intent(MarketplaceActivity.this, CreateMarketplace.class);
                startActivity(intent);
            }
        });
    }
}
