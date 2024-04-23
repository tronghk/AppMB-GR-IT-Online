package com.example.appchatit.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appchatit.R;

public class DetailsChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxchat);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String userId = bundle.getString("userId", "");
            String chatId = bundle.getString("chatId", "");
            String userName = bundle.getString("userName", "");
            String imagePath = bundle.getString("imagePath", "");
        }
    }
}