package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgrit.activities.activity_home;
import com.example.appgrit.adapters.PostAdapter;
import com.example.appgrit.helper.JWTServices;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.example.appgrit.network.UserApiService;
import com.example.appgrit.services.ProfileServices;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends AppCompatActivity {

    private ImageView back;

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<PostModel> postList = new ArrayList<>();
    private LinearLayout top_bar;
    private CircleImageView image_profile;
    private TextView posts;
    private TextView follower;
    private TextView following;
    private LinearLayout mid_bar;
    private TextView fullname;
    private LinearLayout setting;
    private Button btn_share;
    private Button btn_edit;
    private LinearLayout last_bar;
    private ImageView my_photo;
    private ImageView share_photo;
    private RecyclerView recycler_view;
    private RecyclerView recycler_view_share;

    private ProfileServices _profileService;
    private TokenModel tokenModel;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentt_profile);

        // create obj
        back = findViewById(R.id.back);
        top_bar = findViewById(R.id.top_bar);
        image_profile = findViewById(R.id.image_profile);
        posts = findViewById(R.id.posts);
        follower = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        mid_bar = findViewById(R.id.mid_bar);
        fullname = findViewById(R.id.fullname);
        setting = findViewById(R.id.setting);
        btn_edit = findViewById(R.id.btn_edit);
        btn_share = findViewById(R.id.btn_share);
        last_bar = findViewById(R.id.last_bar);
        my_photo = findViewById(R.id.my_photos);
        share_photo = findViewById(R.id.save_photos);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view_share = findViewById(R.id.recycler_view_save);
        _profileService = new ProfileServices();
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accessToken = prefs.getString("accessToken", "No name defined");//"No name defined" is the default value.
        String refreshToken = prefs.getString("refreshToken", "No name defined");
        String timeToken = prefs.getString("tokenTime", "No name defined");

        tokenModel = new TokenModel();
        tokenModel.setAccessToken(accessToken);
        tokenModel.setRefreshToken(refreshToken);
       // tokenModel.setExpiration(new Dat);
        setupRecyclerView();

    }
    private void setupRecyclerView() {
        recyclerViewPosts = findViewById(R.id.recycler_view);
        postAdapter = new PostAdapter(this, postList);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPosts.setAdapter(postAdapter);
        loadPosts();
        CountFl();
        GetImage();
    }
    private void CountFl(){
        String userId = JWTServices.GetUserId(tokenModel.getAccessToken());
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<ResponseBody> call = service.CountFollowers(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String s = response.body().string();
                        JSONObject json = new JSONObject(s);
                       String count =  json.get("count").toString();



                        follower.setText(count+"");
                    }catch (Exception e){

                    }
                    // Log dữ liệu từ API

                }            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching count fl: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching posts: ", t);
            }

        });
    }
    private void GetImage(){
        String userId = JWTServices.GetUserId(tokenModel.getAccessToken());
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
                Toast.makeText(getApplicationContext(), "Error fetching count fl: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching posts: ", t);
            }

        });
    }
    private int abc = 1;
    private void loadPosts() {

        String userId = JWTServices.GetUserId(tokenModel.getAccessToken());
        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<List<PostModel>> call = service.getPostSelfUser(userId); // Truyền userId vào phương thức getPostUser
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    List<PostModel> list = response.body();
                    abc = list.size();
                    posts.setText(abc+"");
                    postAdapter.setData(list); // Cập nhật dữ liệu vào adapter

                    // Log dữ liệu từ API

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch posts: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching posts: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching posts: ", t);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventEdit();

    }

    private void EventEdit() {
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), edit_profile.class);
                startActivity(intent);
            }
        });
    }
}