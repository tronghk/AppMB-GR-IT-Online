package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgrit.adapters.PostAdapter;
import com.example.appgrit.helper.JWTServices;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.example.appgrit.network.UserApiService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileOther extends AppCompatActivity {

    private ImageView back;
    private CircleImageView image_profile;
    private TextView posts;
    private TextView follower;
    private TextView following;
    private TextView fullname;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<PostModel> postList = new ArrayList<>();
    private TokenModel tokenModel;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other);

        // Khởi tạo các thành phần giao diện
        back = findViewById(R.id.back);
        image_profile = findViewById(R.id.image_profile);
        posts = findViewById(R.id.posts);
        follower = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        fullname = findViewById(R.id.fullname);
        recyclerViewPosts = findViewById(R.id.recycler_view);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Lấy dữ liệu người dùng đã chọn
        Intent intent = getIntent();
        String selectedUserId = intent.getStringExtra("selectedUserId");

        // Lấy thông tin và bài đăng của người dùng đã chọn
        getUserInfo(selectedUserId);
        getUserPosts(selectedUserId);
        getCountFollowers(selectedUserId);
        getCountFollowing(selectedUserId);

        // Xử lý sự kiện nút quay lại
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Phương thức để lấy thông tin người dùng từ API
    private void getUserInfo(String userId) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = service.getUserInfo(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    fullname.setText(user.getUserName());
                    Picasso.get().load(user.getImagePath()).into(image_profile);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching user info: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching user info: ", t);
            }
        });
    }

    // Phương thức để lấy bài đăng của người dùng từ API
    private void getUserPosts(String userId) {
        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<List<PostModel>> call = service.getPostSelfUser(userId);
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    List<PostModel> list = response.body();
                    postAdapter = new PostAdapter(ProfileOther.this, list);
                    recyclerViewPosts.setLayoutManager(new LinearLayoutManager(ProfileOther.this));
                    recyclerViewPosts.setAdapter(postAdapter);
                    posts.setText(String.valueOf(list.size()));
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching user posts: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching user posts: ", t);
            }
        });
    }

    // Phương thức để lấy số người theo dõi của người dùng từ API
    private void getCountFollowers(String userId) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<ResponseBody> call = service.CountFollowers(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        JSONObject json = new JSONObject(s);
                        String count = json.get("count").toString();
                        follower.setText(count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching follower count: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching follower count: ", t);
            }
        });
    }

    // Phương thức để lấy số người đang theo dõi của người dùng từ API
    private void getCountFollowing(String userId) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<ResponseBody> call = service.CountUserFollowers(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        JSONObject json = new JSONObject(s);
                        String count = json.get("count").toString();
                        following.setText(count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching following count: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching following count: ", t);
            }
        });
    }
}
