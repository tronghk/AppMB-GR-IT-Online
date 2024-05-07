package com.example.appchatit.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatit.R;
import com.example.appchatit.adapters.AdminRoleAdapter;
import com.example.appchatit.adapters.CreateChatAdapter;
import com.example.appchatit.adapters.ManageRoleAdapter;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRoleActivity extends AppCompatActivity {
    private String chatId;
    private String userName;
    private String imagePath;
    private AdminRoleAdapter adminRoleAdapter;
    private RecyclerView recyclerView;
    private List<UserModel> userList = new ArrayList<>();
    private List<GroupMemberModel> memberList = new ArrayList<>();
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);
        setupRecyclerView();

        TextView txtMain = findViewById(R.id.txt_create_chat);
        txtMain.setText("Change admin group");
    }

    private void setupRecyclerView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            chatId = bundle.getString("chatId", "");
            userName = bundle.getString("userName", "");
            imagePath = bundle.getString("imagePath", "");
            memberList = (List<GroupMemberModel>) bundle.getSerializable("memberList");
        }

        recyclerView = findViewById(R.id.recycle_create_chat);
        adminRoleAdapter = new AdminRoleAdapter(this, userList, memberList, chatId, userName, imagePath);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adminRoleAdapter);

        if (memberList != null && !memberList.isEmpty()) {
            for (GroupMemberModel memberModel : memberList) {
                String userMemberId = memberModel.getUserId();
                getUserInfo(userMemberId);
            }
        } else {
            Toast.makeText(AdminRoleActivity.this, "Empty member", Toast.LENGTH_SHORT).show();
        }

        btnBack = findViewById(R.id.btn_back_create_chat);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getUserInfo(String userId) {
        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = userApiService.getUserBasic(userId);
        call.enqueue(new Callback<UserModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    if (user != null) {
                        userList.add(user);
                        adminRoleAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminRoleActivity.this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminRoleActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(AdminRoleActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}