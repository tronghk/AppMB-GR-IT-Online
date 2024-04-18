package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.models.ImagePostModel;

import java.util.List;

public class DetailMarketplaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_marketplace);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String productName = bundle.getString("productName", "");
            String content = bundle.getString("content", "");
            List<ImagePostModel> imagePosts = (List<ImagePostModel>) bundle.getSerializable("imagePosts");
            float price = bundle.getFloat("price", 0.0f); // Giá trị mặc định là 0.0 nếu không tìm thấy giá trị

            TextView textView = findViewById(R.id.text_product_title);
            textView.setText(productName);

            TextView contentTextView = findViewById(R.id.text_product_description);
            contentTextView.setText(content);

            TextView priceTextView = findViewById(R.id.text_product_price);
            priceTextView.setText(String.valueOf(price));

//            for (ImagePostModel imagePost : imagePosts) {
//                ImageView imageView = new ImageView(this);
//                Glide.with(this).load(imagePost.getImagePath()).into(imageView);
//            }

            ImageView imageView = findViewById(R.id.image_product);
            ImagePostModel imagePost = imagePosts.get(0);
            Glide.with(this).load(imagePost.getImagePath()).into(imageView);
        }
    }
}