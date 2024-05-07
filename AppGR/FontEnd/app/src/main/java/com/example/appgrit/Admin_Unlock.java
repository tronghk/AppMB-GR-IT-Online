package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgrit.activities.activity_home;
import com.example.appgrit.adapters.LockUserAdapter;
import com.example.appgrit.adapters.PostAdapter;
import com.example.appgrit.adapters.UserAdapter;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_Unlock extends AppCompatActivity {

    private RecyclerView recyclerViewUser;
    private LockUserAdapter userAdapter;
    private List<UserModel> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_unlock);
        setupBottomNavigationView();

        recyclerViewUser = findViewById(R.id.recycler_view);

    }

    private void setupRecyclerView() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        userAdapter = new LockUserAdapter(getApplicationContext(),userList,token);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUser.setAdapter(userAdapter);
        loadUser();
    }


    private void loadUser() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<List<UserModel>> call = service.GetUserLocked("Bearer " + token);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {

                    List<UserModel> users = response.body();
                    userAdapter.setData(users);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch posts: " + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
    }


    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_statistical) {
                Intent intent = new Intent(getApplicationContext(), Admin_Home.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_unlock) {

                return true;
            } else if (itemId == R.id.nav_post) {
                Intent intent = new Intent(getApplicationContext(), Admin_Post.class);
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });
    }
}