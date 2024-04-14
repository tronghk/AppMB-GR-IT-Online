package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appgrit.R;
import com.example.appgrit.adapters.PostExpressionsAdapter;
import com.example.appgrit.models.PostIconsModel;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.network.ApiServiceProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUserLike extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostExpressionsAdapter adapter;
    private List<PostIconsModel> userLikeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_like);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostExpressionsAdapter(userLikeList);
        recyclerView.setAdapter(adapter);

//        String postId = getIntent().getStringExtra("postId");
//        String postId = "-NvLBSGqygEi3b967NU2";

        String postId = getIntent().getStringExtra("postId");
        getPostExpressions(postId);
    }

    private void getPostExpressions(String postId) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
//        String postId = prefs.getString("postId", "");
        Call<List<PostIconsModel>> call = ApiServiceProvider.getPostApiService().getPostExpressions(token, postId);
        call.enqueue(new Callback<List<PostIconsModel>>() {
            @Override
            public void onResponse(Call<List<PostIconsModel>> call, Response<List<PostIconsModel>> response) {
                if (response.isSuccessful()) {
                    List<PostIconsModel> postExpressions = response.body();
                    for (PostIconsModel postExpression : postExpressions) {
                        if (postExpression.getExpression() == 1) {
                            userLikeList.add(postExpression);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    // Xử lý lỗi khi gọi API thất bại
                    Toast.makeText(ListUserLike.this, "Failed to get post expressions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostIconsModel>> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Toast.makeText(ListUserLike.this, "Failed to get post expressions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}