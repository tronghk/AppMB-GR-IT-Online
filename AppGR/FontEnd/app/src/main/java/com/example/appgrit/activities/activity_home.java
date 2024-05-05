// activity_home.java
package com.example.appgrit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgrit.ForgotPasswordActivity;
import com.example.appgrit.FragmentProfile;
import com.example.appgrit.R;
import com.example.appgrit.UserActivity;
import com.example.appgrit.adapters.PostAdapter;
import com.example.appgrit.changepassword;
import com.example.appgrit.edit_profile;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
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
                Intent intent = new Intent(activity_home.this, FragmentProfile.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_users) {
                Intent intent = new Intent(activity_home.this, UserActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreMenu(findViewById(R.id.nav_more));
                return true;
            }
            else if (itemId == R.id.nav_chat) {
                changeAppChat("com.example.appchatit");
                return true;
            }
            else {
                return false;
            }
        });
    }
    public void changeAppChat(String appId){
// Tên gói ứng dụng của ứng dụng bạn muốn mở
        String packageName = appId;

// Tạo một Intent để mở ứng dụng với tên gói ứng dụng đã biết
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String refreshToken = prefs.getString("refreshToken", "");
        String expiration =  prefs.getString("expiration", "");
        intent.putExtra("accessToken",token);
        intent.putExtra("refreshToken",refreshToken);
        intent.putExtra("expiration",expiration);
        if (intent != null) {
            // Kiểm tra xem Intent có hợp lệ không trước khi mở ứng dụng
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),intent.toString(),Toast.LENGTH_SHORT).show();
        }
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
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", ""); // Lấy userId từ SharedPreferences
        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<List<PostModel>> call = service.getPostUser("Bearer " + token, userId);
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    List<PostModel> posts = response.body();

                    // Sắp xếp `posts` để đảm bảo các bài đăng mới nhất ở đầu
                    Collections.sort(posts, (post1, post2) -> post2.getPostTime().compareTo(post1.getPostTime()));

                    postAdapter.setData(posts); // Cập nhật dữ liệu vào adapter
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
    private void logout() {
        // Xóa dữ liệu đăng nhập từ SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
        editor.remove("accessToken");
        editor.remove("userId");
        editor.apply();

        // Chuyển hướng đến màn hình đăng nhập
        Intent intent = new Intent(activity_home.this, activity_signin.class);
        startActivity(intent);
        finish(); // Kết thúc activity hiện tại để ngăn người dùng quay lại khi nhấn nút Back
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
                startActivity(new Intent(activity_home.this, changepassword.class));
                return true;
            } else if (itemId == R.id.nav_edit_info) {
                startActivity(new Intent(activity_home.this, edit_profile.class));
                return true;
            } else if (itemId == R.id.nav_upgrade_account) {
                startActivity(new Intent(activity_home.this, UpgradeAccountActivity.class));
                return true;
            } else if (itemId == R.id.nav_logout) {
                // Xử lý đăng xuất ở đây
                logout();
                return true;
            }
            else {
                return false;
            }
        });
        popup.show();
    }
}
