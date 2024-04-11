package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.R;
import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserInforModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_signin extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSignIn = findViewById(R.id.button_sign_in);

        buttonSignIn.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                signIn(email, password);
            } else {
                Toast.makeText(activity_signin.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn(String email, String password) {
        SignInModel signInModel = new SignInModel(email, password);
        UserApiService service = ApiServiceProvider.getUserApiService();

        Call<TokenModel> call = service.signIn(signInModel);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    // Save the access token in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    prefs.edit().putString("token", accessToken).apply();

                    // Now get user info
                    getUserInfo(accessToken);
                    Toast.makeText(activity_signin.this, "Login successful!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity_signin.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Toast.makeText(activity_signin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo(String accessToken) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<UserInforModel> call = service.getUserInfo("Bearer " + accessToken, editTextEmail.getText().toString().trim());

        call.enqueue(new Callback<UserInforModel>() {
            @Override
            public void onResponse(Call<UserInforModel> call, Response<UserInforModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Here you get user information after successful login
                    String userId = response.body().getUserId();

                    // Save the user ID in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    prefs.edit().putString("userId", userId).apply();

                    Log.d("LoginSuccess", "UserID " + userId + " saved in SharedPreferences.");
                    navigateToHome();
                } else {
                    Toast.makeText(activity_signin.this, "Failed to retrieve user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInforModel> call, Throwable t) {
                Toast.makeText(activity_signin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        // Navigate to the next screen after login
        Intent intent = new Intent(activity_signin.this, activity_home.class);
        startActivity(intent);
        finish();
    }

    // Phương thức chuyển sang màn hình đăng ký
    public void goToSignUp(View view) {
        Intent intent = new Intent(this, activity_signup.class);
        startActivity(intent);
    }
}
