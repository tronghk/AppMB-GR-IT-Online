package com.example.appgrit.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Lấy đường dẫn của hình ảnh từ intent
        String imagePath = getIntent().getStringExtra("imagePath");

        // Tìm ImageView trong layout
        ImageView imageView = findViewById(R.id.imageView);

        // Sử dụng thư viện Glide để tải và hiển thị hình ảnh từ đường dẫn
        Glide.with(this)
                .load(imagePath)
                .into(imageView);
    }
}
