package com.example.appchatit.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.adapters.CreateGroupAdapter;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoGroupActivity extends AppCompatActivity {
    private String chatId;
    private String userName;
    private String imagePath;
    private ImageView imgGroup;
    private TextView nameGroup;
    private ConstraintLayout function1;
    private List<GroupMemberModel> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_group);
        initializeViews();
        setupEventListeners();
        loadInfoGroup();
        getMemberList();
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
                bundle.putSerializable("memberList", (Serializable) memberList);
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

    private void getMemberList() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();

        if (chatId != null) {
            Call<List<GroupMemberModel>> call = service.getListMemberGroup("Bearer " + token, chatId);
            call.enqueue(new Callback<List<GroupMemberModel>>() {
                @Override
                public void onResponse(Call<List<GroupMemberModel>> call, Response<List<GroupMemberModel>> response) {
                    if (response.isSuccessful()) {
                        memberList = response.body();
                        Toast.makeText(InfoGroupActivity.this, "Get member list successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InfoGroupActivity.this, "Failed to fetch member list: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<GroupMemberModel>> call, Throwable t) {
                    Toast.makeText(InfoGroupActivity.this, "Error fetching member list: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}