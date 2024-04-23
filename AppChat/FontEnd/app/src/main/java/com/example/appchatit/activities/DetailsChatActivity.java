package com.example.appchatit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatit.R;
import com.example.appchatit.adapters.ChatAdapter;
import com.example.appchatit.adapters.DetailsChatAdapter;
import com.example.appchatit.models.DetailsChatModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsChatActivity extends AppCompatActivity {
    private String userId;
    private String chatId;
    private String userName;
    private String imagePath;
    private DetailsChatAdapter detailsChatAdapter;
    private RecyclerView recyclerView;
    private List<DetailsChatModel> detailsChatModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxchat);
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "");

        recyclerView = findViewById(R.id.box_chat);
        detailsChatAdapter = new DetailsChatAdapter(this, detailsChatModelList, userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(detailsChatAdapter);
        loadListMessage();
    }

    private void loadListMessage() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            chatId = bundle.getString("chatId", "");
            userName = bundle.getString("userName", "");
            imagePath = bundle.getString("imagePath", "");
        }

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();
        Call<List<DetailsChatModel>> call = service.getListDetailsChat("Bearer " + token, chatId);
        call.enqueue(new Callback<List<DetailsChatModel>>() {
            @Override
            public void onResponse(Call<List<DetailsChatModel>> call, Response<List<DetailsChatModel>> response) {
                if (response.isSuccessful()) {
                    List<DetailsChatModel> detailsChatModelList = response.body();
                    Collections.sort(detailsChatModelList, new Comparator<DetailsChatModel>() {
                        @Override
                        public int compare(DetailsChatModel detailChat1, DetailsChatModel detailChat2) {
                            return detailChat1.getTime().compareTo(detailChat2.getTime());
                        }
                    });
                    for (DetailsChatModel mess : detailsChatModelList) {
                        Log.d("time", mess.getTime());
                    }
                    detailsChatAdapter.setData(detailsChatModelList);
                } else {
                    Toast.makeText(DetailsChatActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetailsChatModel>> call, Throwable t) {
                Toast.makeText(DetailsChatActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching users: ", t);
            }
        });
    }
}