package com.example.appchatit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.appchatit.activities.ChatActivity;
import com.example.appchatit.models.SignInModel;
import com.example.appchatit.models.TokenModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Intent intent = getIntent();
        String token = intent.getStringExtra("accessToken");
        String refreshToken = intent.getStringExtra("refreshToken");
        String expiration = intent.getStringExtra("expiration");
        if (token != null) {
            prefs.edit().putString("accessToken", token).apply();
            prefs.edit().putString("refreshToken", refreshToken).apply();
            prefs.edit().putString("expiration", expiration).apply();
            getUserInfo(token);
        } else {
//            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        }, 2000);
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

                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Log.d("Error: ", t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}