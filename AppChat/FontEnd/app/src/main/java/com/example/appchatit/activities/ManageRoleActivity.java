package com.example.appchatit.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatit.R;
import com.example.appchatit.adapters.CreateChatAdapter;
import com.example.appchatit.adapters.ManageRoleAdapter;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.UserFriendModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;
import com.example.appchatit.services.OnRoleSelectedListener;
import com.example.appchatit.services.UserApiService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRoleActivity extends AppCompatActivity {
    private String chatId;
    private String userName;
    private String imagePath;
    private ManageRoleAdapter manageRoleAdapter;
    private RecyclerView recyclerView;
    private List<UserModel> userList = new ArrayList<>();
    private List<GroupMemberModel> memberList;
    private ImageView btnUpdateRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_role);
        setupRecyclerView();
        btnUpdateRole = findViewById(R.id.btn_done_manage_role);
        btnUpdateRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRole();
            }
        });

        manageRoleAdapter.setOnRoleSelectedListener(new OnRoleSelectedListener() {
            @Override
            public void onRoleSelected(String userId, int position, String role) {
                for (GroupMemberModel member : manageRoleAdapter.getMemberList()) {
                    if (member.getUserId().equals(userId)) {
                        switch (role) {
                            case "Manager":
                                member.setRole("GR_MANAGER");
                                break;
                            case "Sub-manager":
                                member.setRole("GR_SUB_MANAGER");
                                break;
                            case "Member":
                                member.setRole("GR_MEMBER");
                                break;
                        }
                        break;
                    }
                }
            }
        });
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

        recyclerView = findViewById(R.id.recycle_manage_role);
        manageRoleAdapter = new ManageRoleAdapter(this, userList, memberList, chatId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(manageRoleAdapter);

        if (memberList != null && !memberList.isEmpty()) {
            for (GroupMemberModel memberModel : memberList) {
                String userMemberId = memberModel.getUserId();
                getUserInfo(userMemberId);
            }
        } else {
            Toast.makeText(ManageRoleActivity.this, "Empty member", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRole() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();
        if (chatId != null) {
            final AtomicInteger completedCalls = new AtomicInteger(0);
            final int totalCalls = memberList.size();

            for (GroupMemberModel member : memberList) {
                Call<GroupMemberModel> call = service.updateRoleGroupMember("Bearer " + token, chatId, member.getUserId(), member.getRole());
                call.enqueue(new Callback<GroupMemberModel>() {
                    @Override
                    public void onResponse(Call<GroupMemberModel> call, Response<GroupMemberModel> response) {
                        if (response.isSuccessful()) {
                            int count = completedCalls.incrementAndGet();
                            if (count == totalCalls) {
                                startInfoGroupActivity();
                            }
                        } else {
                            Toast.makeText(ManageRoleActivity.this, "Failed to update member role: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GroupMemberModel> call, Throwable t) {
                        Toast.makeText(ManageRoleActivity.this, "Error updating member role: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void startInfoGroupActivity() {
        Intent intent = new Intent(ManageRoleActivity.this, InfoGroupActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("chatId", chatId);
        bundle.putString("userName", userName);
        bundle.putString("imagePath", imagePath);

        intent.putExtras(bundle);
        startActivity(intent);
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
                        manageRoleAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ManageRoleActivity.this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ManageRoleActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(ManageRoleActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}