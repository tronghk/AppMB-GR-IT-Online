package com.example.appchatit.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private ConstraintLayout function1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_group);
        initializeViews();
        setupEventListeners();
        loadInfoGroup();
    }

    private void initializeViews() {
        imgGroup = findViewById(R.id.img_info_gr);
        nameGroup = findViewById(R.id.txt_name_gr);
        function1  = findViewById(R.id.function_1);
    }

    private void setupEventListeners() {
        function1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoGroupActivity.this, CreateGroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEdit", true);
                bundle.putString("chatId", chatId);
                bundle.putString("userName", userName);
                bundle.putString("imagePath", imagePath);
                intent.putExtras(bundle);
                startActivity(intent);
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = animation.getAnimatedFraction();
                        int alpha = (int) (255 * (1 - progress));
                        int color = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
                        function1.setBackgroundColor(color);
                    }
                });
                animator.start();
            }
        });
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