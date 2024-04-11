// activity_home.java
package com.example.appgrit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgrit.ForgotPasswordActivity;
import com.example.appgrit.MarketplaceActivity;
import com.example.appgrit.ProfileOther;
import com.example.appgrit.R;
import com.example.appgrit.UserActivity;
import com.example.appgrit.adapters.PostAdapter;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_home extends AppCompatActivity {
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<PostModel> postList = new ArrayList<>();
    private boolean isLiked = false; // Biến để xác định trạng thái like

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupRecyclerView();
        setupBottomNavigationView();

        // Gán sự kiện onClick cho các ImageView như btn_add_post, btn_noti_post, v.v...
        setupImageViewClickListeners();
    }

    private void setupRecyclerView() {
        recyclerViewPosts = findViewById(R.id.recycler_view_story);
        postAdapter = new PostAdapter(this, postList);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPosts.setAdapter(postAdapter);
        loadPosts();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Giữ nguyên màn hình hiện tại
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(activity_home.this, ProfileOther.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_users) {
                Intent intent = new Intent(activity_home.this, UserActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreMenu(findViewById(R.id.nav_more));
                return true;
            } else {
                return false;
            }
        });
    }

    private void setupImageViewClickListeners() {
        ImageView btnAddPost = findViewById(R.id.btn_add_post);
        if (btnAddPost != null) {
            btnAddPost.setOnClickListener(v -> {
                Intent intent = new Intent(activity_home.this, CreatePostHome.class);
                startActivity(intent);
            });
        } else {
            Log.e("activity_home", "btn_add_post not found in the layout.");
        }

        // Lặp lại quy trình kiểm tra cho mỗi View khác bạn muốn thiết lập listener

    }

    private void loadPosts() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("userId", ""); // Lấy userId từ SharedPreferences

        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<List<PostModel>> call = service.getPostUser("Bearer " + token, userId); // Truyền userId vào phương thức getPostUser
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    List<PostModel> posts = response.body();
                    postAdapter.setData(posts); // Cập nhật dữ liệu vào adapter

                    // Log dữ liệu từ API
                    Log.d("API_Response", "Total posts received: " + posts.size());
                    for (PostModel post : posts) {
                        Log.d("API_Response", "Post ID: " + post.getPostId());
                        // Log thêm các thông tin khác của bài đăng nếu cần
                    }
                } else {
                    Toast.makeText(activity_home.this, "Failed to fetch posts: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Toast.makeText(activity_home.this, "Error fetching posts: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching posts: ", t);
            }

        });
    }





    private void showMoreMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.more_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_marketplace) {
                startActivity(new Intent(activity_home.this, MarketplaceActivity.class));
                return true;
            } else if (itemId == R.id.nav_change_password) {
                startActivity(new Intent(activity_home.this, ForgotPasswordActivity.class));
                return true;
            } else {
                return false;
            }
        });
        popup.show();
    }
}
