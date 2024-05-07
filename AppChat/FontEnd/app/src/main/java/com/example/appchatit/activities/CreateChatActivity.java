package com.example.appchatit.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatit.R;
import com.example.appchatit.adapters.CreateChatAdapter;
import com.example.appchatit.models.UserFriendModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChatActivity extends AppCompatActivity {
    private CreateChatAdapter createChatAdapter;
    private RecyclerView recyclerView;
    private List<UserModel> userList = new ArrayList<>();
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycle_create_chat);
        createChatAdapter = new CreateChatAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(createChatAdapter);
        loadListFriend();

        btnBack = findViewById(R.id.btn_back_create_chat);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadListFriend() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<List<UserFriendModel>> call = userApiService.getListUserFriend(userId);
        call.enqueue(new Callback<List<UserFriendModel>>() {
            @Override
            public void onResponse(Call<List<UserFriendModel>> call, Response<List<UserFriendModel>> response) {
                if (response.isSuccessful()) {
                    List<UserFriendModel> userFriendsList = response.body();
                    if (userFriendsList != null && !userFriendsList.isEmpty()) {
                        for (UserFriendModel userFriend : userFriendsList) {
                            String userFriendId = userFriend.getUserFriendId();
                            getUserInfo(userFriendId);
                        }
                    } else {
                        Toast.makeText(CreateChatActivity.this, "Không có bạn bè", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateChatActivity.this, "Lỗi khi lấy danh sách bạn bè", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserFriendModel>> call, Throwable t) {
                Toast.makeText(CreateChatActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo(String userFriendId) {
        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = userApiService.getUserBasic(userFriendId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    if (user != null) {
                        userList.add(user);
                        createChatAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CreateChatActivity.this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateChatActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(CreateChatActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}