package com.example.appchatit.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;

public class InfoGroupActivity extends AppCompatActivity {
    private String chatId;
    private String userName;
    private String imagePath;
    private ImageView imgGroup;
    private TextView nameGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_group);
        initializeViews();
        loadInfoGroup();
    }

    private void initializeViews(){
        imgGroup = findViewById(R.id.img_info_gr);
        nameGroup = findViewById(R.id.txt_name_gr);
    }

    private void loadInfoGroup() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            chatId = bundle.getString("chatId", "");
            userName = bundle.getString("userName", "");
            imagePath = bundle.getString("imagePath", "");
        }

        nameGroup.setText(userName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CircleCrop());
        Glide.with(this).load(imagePath).apply(requestOptions).into(imgGroup);
    }
}