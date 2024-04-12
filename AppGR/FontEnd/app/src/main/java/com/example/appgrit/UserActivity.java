package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appgrit.adapters.UserAdapter;
import com.example.appgrit.models.UserFriendsModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        getUserFriends();
    }

    private void getUserFriends() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<List<UserFriendsModel>> call = userApiService.getListUserFriend(userId);
        call.enqueue(new Callback<List<UserFriendsModel>>() {
            @Override
            public void onResponse(Call<List<UserFriendsModel>> call, Response<List<UserFriendsModel>> response) {
                if (response.isSuccessful()) {
                    List<UserFriendsModel> userFriendsList = response.body();
                    if (userFriendsList != null && !userFriendsList.isEmpty()) {
                        for (UserFriendsModel userFriend : userFriendsList) {
                            String userFriendId = userFriend.getUserFriendId();
                            getUserInfo(userFriendId);
                        }
                    } else {
                        // Danh sách bạn bè trống
                        Toast.makeText(UserActivity.this, "Không có bạn bè", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi nhận phản hồi không thành công
                    Toast.makeText(UserActivity.this, "Lỗi khi lấy danh sách bạn bè", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserFriendsModel>> call, Throwable t) {
                // Xử lý khi có lỗi mạng
                Toast.makeText(UserActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                Log.e("UserActivity", "Lỗi: " + t.getMessage());
            }
        });
    }

    private void getUserInfo(String userFriendId) {
        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = userApiService.getUserInfo(userFriendId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    if (user != null) {
                        userList.add(user);
                        userAdapter.notifyDataSetChanged();
                    } else {
                        // Xử lý khi không có dữ liệu user
                        Toast.makeText(UserActivity.this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi nhận phản hồi không thành công
                    Toast.makeText(UserActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // Xử lý khi có lỗi mạng
                Toast.makeText(UserActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                Log.e("UserActivity", "Lỗi: " + t.getMessage());
            }
        });
    }
}