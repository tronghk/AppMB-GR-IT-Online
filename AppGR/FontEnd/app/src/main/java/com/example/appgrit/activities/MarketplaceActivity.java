package com.example.appgrit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgrit.R;
import com.example.appgrit.adapters.MarketplaceAdapter;
import com.example.appgrit.models.PostSellProductModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.services.MarketplaceApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketplaceActivity extends AppCompatActivity {
    private MarketplaceAdapter marketplaceAdapter;
    private RecyclerView recyclerView;
    private List<PostSellProductModel> postList = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        Button btnUpload = findViewById(R.id.btn_upload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketplaceActivity.this, CreateMarketplaceActivity.class);
                startActivity(intent);
            }
        });
        setupRecyclerView();
//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        postList = new ArrayList<>();
//        marketplaceAdapter = new MarketplaceAdapter(this, postList);
//        recyclerView.setAdapter(marketplaceAdapter);
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        marketplaceAdapter = new MarketplaceAdapter(this, postList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(marketplaceAdapter);
        loadPosts();
    }

    private void loadPosts() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", ""); // Lấy userId từ SharedPreferences
        MarketplaceApiService service = ApiServiceProvider.getMarketplaceApiService();
        Call<List<PostSellProductModel>> call = service.getPostSell("Bearer " + token, userId);
        call.enqueue(new Callback<List<PostSellProductModel>>() {
            @Override
            public void onResponse(Call<List<PostSellProductModel>> call, Response<List<PostSellProductModel>> response) {
                if (response.isSuccessful()) {
                    List<PostSellProductModel> postList = response.body();
                    marketplaceAdapter.setData(postList);
//                    recyclerView = findViewById(R.id.recycler_view);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//                    marketplaceAdapter = new MarketplaceAdapter(this, postList);
//                    recyclerView.setAdapter(marketplaceAdapter);
                } else {
                    Toast.makeText(MarketplaceActivity.this, "Failed to fetch posts: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostSellProductModel>> call, Throwable t) {
                Toast.makeText(MarketplaceActivity.this, "Error fetching posts: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching posts: ", t);
            }
        });
    }
}