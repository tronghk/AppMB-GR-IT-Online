package com.example.appchatit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.MainActivity;
import com.example.appchatit.R;
import com.example.appchatit.adapters.ActiveAdapter;
import com.example.appchatit.adapters.ChatAdapter;
import com.example.appchatit.models.UserFriendModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;
import com.example.appchatit.services.UserApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private List<UserModel> userModelList = new ArrayList<>();
    private ActiveAdapter activeAdapter;
    private RecyclerView recyclerViewActive;
    private List<UserModel> friendList = new ArrayList<>();
    private Timer timer;
    private ImageView imgUser;
    private TextView nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchat);
        setupRecyclerView();
//        setupBottomNavigationView();

        ImageView btnCreateChat = findViewById(R.id.btn_create_chat);
        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        imgUser = findViewById(R.id.btn_back);
        nameUser = findViewById(R.id.txt_name_account);
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.list_chat);
        chatAdapter = new ChatAdapter(this, userModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        recyclerViewActive = findViewById(R.id.recycler_view_active);
        activeAdapter = new ActiveAdapter(this, friendList);
        recyclerViewActive.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewActive.setAdapter(activeAdapter);

        getInfo();
        loadListChat();
        loadListMessOrtherUser();
        loadListFriend();
    }

//    private void setupBottomNavigationView() {
//        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.nav_item_chat) {
//                return true;
//            } else if (itemId == R.id.nav_item_group) {
//                Intent intent = new Intent(ChatActivity.this, CreateChatActivity.class);
//                startActivity(intent);
//                return true;
//            } else {
//                return false;
//            }
//        });
//    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_create, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_item_create_chat) {
                startActivity(new Intent(ChatActivity.this, CreateChatActivity.class));
                return true;
            } else if (itemId == R.id.nav_item_create_group) {
                startActivity(new Intent(ChatActivity.this, CreateGroupActivity.class));
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    public void loadListChat() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();
        Call<List<UserModel>> call = service.getChat("Bearer " + token, userId);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> userList = response.body();
                    chatAdapter.setData(userList);
                } else {
//                    Toast.makeText(ChatActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//                Toast.makeText(ChatActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadListMessOrtherUser() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();
        Call<List<UserModel>> call = service.getListMessOtherUser("Bearer " + token, userId);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    chatAdapter.updateMessOrtherUser(response.body());
                } else {
//                    Toast.makeText(ChatActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//                Toast.makeText(ChatActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                            startLoadingMessagesPeriodically();
                        }
                    } else {
                        Toast.makeText(ChatActivity.this, "No friend", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(ChatActivity.this, "Lỗi khi lấy danh sách bạn bè", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserFriendModel>> call, Throwable t) {
//                Toast.makeText(ChatActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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
                        friendList.add(user);
                        activeAdapter.notifyDataSetChanged();
                    } else {
//                        Toast.makeText(ChatActivity.this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(ChatActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
//                Toast.makeText(ChatActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = userApiService.getUserBasic(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    nameUser.setText(user.getUserName());
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transforms(new CircleCrop());
                    Glide.with(ChatActivity.this).load(user.getImagePath()).apply(requestOptions).into(imgUser);
                } else {
                    //
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                //
            }
        });
    }

    private void startLoadingMessagesPeriodically() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadListChat();
            }
        }, 0, 5000);
    }
}