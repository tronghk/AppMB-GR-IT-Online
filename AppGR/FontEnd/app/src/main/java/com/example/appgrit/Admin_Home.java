package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgrit.activities.CreatePostHome;
import com.example.appgrit.activities.activity_home;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_Home extends AppCompatActivity {

    private Button btn_sumOfDay;
    private Button btn_sumOfWeek;
    private Button sum_revenue;
    private Button sum_user;
   // private Button sum_top;
    private LinearLayout sub_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btn_sumOfDay = findViewById(R.id.sum_post_day);
        btn_sumOfWeek = findViewById(R.id.sum_post_week);
        sum_revenue = findViewById(R.id.sum_revenue);
        sum_user = findViewById(R.id.sum_user);
     //   sum_top = findViewById(R.id.sum_top);
        sub_menu = findViewById(R.id.line1);

        setupBottomNavigationView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        EventSumOfDay();
        EventSumOfWeek();
        EventSumUser();
        EventSumPayMent();
    }
    public void EventSumOfDay(){
        btn_sumOfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = prefs.getString("accessToken", "");
                PostApiService service = ApiServiceProvider.getPostApiService();
                Call<ResponseBody> call = service.GetSumPost("Bearer " + token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            sub_menu.removeAllViews();
                            try {
                                String s = response.body().string();




                                TextView text = new TextView(getApplicationContext());
                                text.setText(s+" bài viết được thêm trong hôm nay");

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                                params.setMargins(10,10,10,10);
                                text.setLayoutParams(params);
                                sub_menu.addView(text);
                            }catch (Exception e){
                                Log.e("error",token+"fail");
                            }
                            // Log dữ liệu từ API

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }
    public void EventSumUser(){
        sum_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = prefs.getString("accessToken", "");
                PostApiService service = ApiServiceProvider.getPostApiService();
                Call<ResponseBody> call = service.GetSumUser("Bearer " + token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            sub_menu.removeAllViews();
                            try {
                                String s = response.body().string();




                                TextView text = new TextView(getApplicationContext());
                                text.setText("Tổng số người dùng ứng dụng là:" + s);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                                params.setMargins(10,10,10,10);
                                text.setLayoutParams(params);
                                sub_menu.addView(text);
                            }catch (Exception e){
                                Log.e("error",token+"fail");
                            }
                            // Log dữ liệu từ API

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }
    public void EventSumPayMent(){
        sum_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = prefs.getString("accessToken", "");
                PostApiService service = ApiServiceProvider.getPostApiService();
                Call<ResponseBody> call = service.GetSumPayMent("Bearer " + token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            sub_menu.removeAllViews();
                            try {
                                String s = response.body().string();




                                TextView text = new TextView(getApplicationContext());
                                text.setText("Tổng doanh thu trong tháng là: " + s);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                                params.setMargins(10,10,10,10);
                                text.setLayoutParams(params);
                                sub_menu.addView(text);
                            }catch (Exception e){
                                Log.e("error",token+"fail");
                            }
                            // Log dữ liệu từ API

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }
    public void EventSumOfWeek(){
        btn_sumOfWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = prefs.getString("accessToken", "");
                PostApiService service = ApiServiceProvider.getPostApiService();
                Call<ResponseBody> call = service.GetSumPostWeek("Bearer " + token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            sub_menu.removeAllViews();
                            try {
                                String s = response.body().string();




                                TextView text = new TextView(getApplicationContext());
                                text.setText(s+" bài viết được thêm trong tuần nay");

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                                params.setMargins(10,10,10,10);
                                text.setLayoutParams(params);
                                sub_menu.addView(text);
                            }catch (Exception e){
                                Log.e("error",token+"fail");
                            }
                            // Log dữ liệu từ API

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }

                });
                Call<ResponseBody> call2 = service.GetSumPostComPa("Bearer " + token);
                call2.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {


                            try {
                                String s = response.body().string();




                                TextView text = new TextView(getApplicationContext());
                                int in = Integer.parseInt(s);
                                if( in > 0)
                                text.setText("Tăng hơn so với tuần trước là " + s);
                                else {
                                    text.setText("Giảm đi so với tuần trước là " + Math.abs(in));
                                }

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                                params.setMargins(10,10,10,10);
                                text.setLayoutParams(params);
                                sub_menu.addView(text);
                            }catch (Exception e){
                                Log.e("error",token+"fail");
                            }
                            // Log dữ liệu từ API

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }


        });

    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_statistical) {
                // Giữ nguyên màn hình hiện tại
                return true;
            } else if (itemId == R.id.nav_unlock) {
                Intent intent = new Intent(getApplicationContext(), Admin_Unlock.class);
                startActivity(intent);
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