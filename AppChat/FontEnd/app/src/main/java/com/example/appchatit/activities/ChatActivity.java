package com.example.appchatit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.appchatit.MainActivity;
import com.example.appchatit.R;
import com.example.appchatit.adapters.ChatAdapter;
import com.example.appchatit.models.SignInModel;
import com.example.appchatit.models.TokenModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;
import com.example.appchatit.services.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private List<UserModel> userModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchat);

        signIn("abc@gmail.com", "123456");

        setupRecyclerView();
    }

    private void signIn(String email, String password) {
        SignInModel signInModel = new SignInModel(email, password);
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<TokenModel> call = service.signIn(signInModel);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String accessToken = response.body().getAccessToken();
                        String refreshToken = response.body().getRefreshToken();
                        String date = response.body().getExpiration();

                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                        prefs.edit().putString("accessToken", accessToken).apply();
                        prefs.edit().putString("refreshToken", refreshToken).apply();
                        prefs.edit().putString("expiration", date).apply();

                        getUserInfo(accessToken);

                        Toast.makeText(ChatActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Log.d("Error: ", t.getMessage());
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo(String accessToken) {
        JWT parsedJWT = new JWT(accessToken);
        Claim subscriptionMetaData = parsedJWT.getClaim("userId");
        String userId = subscriptionMetaData.asString();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefs.edit().putString("userId", userId).apply();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.list_chat);
        chatAdapter = new ChatAdapter(this, userModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
        loadListChat();
    }

    private void loadListChat() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();
        Call<List<UserModel>> call = service.getListMessOtherUser("Bearer " + token, userId);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> userList = response.body();
                    chatAdapter.setData(userList);
//                    recyclerView = findViewById(R.id.recycler_view);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//                    marketplaceAdapter = new MarketplaceAdapter(this, postList);
//                    recyclerView.setAdapter(marketplaceAdapter);
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching users: ", t);
            }
        });
    }
}