package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.MainActivity;
import com.example.appgrit.R;
import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.TokenModel;
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

        // Ánh xạ views
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSignIn = findViewById(R.id.button_sign_in);

        // Xử lý sự kiện khi nhấn nút đăng nhập
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Kiểm tra tính hợp lệ của email và password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(activity_signin.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Gửi yêu cầu đăng nhập
                    signIn(email, password);
                }
            }
        });
    }

    // Phương thức gọi API đăng nhập
    private void signIn(String email, String password) {
        // Tạo một instance của SignInModel với email và password
        SignInModel signInModel = new SignInModel(email, password);

        // Gọi API đăng nhập bằng Retrofit
        UserApiService apiService = ApiServiceProvider.getApiService();
        Call<TokenModel> call = apiService.signIn(signInModel);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    // Đăng nhập thành công
                    TokenModel tokenModel = response.body();
                    // Xử lý dữ liệu tokenModel
                    Toast.makeText(activity_signin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng sang màn hình chính
                    Intent intent = new Intent(activity_signin.this, activity_home.class);
                    startActivity(intent);
                    finish(); // Đóng activity hiện tại để ngăn người dùng quay lại màn hình đăng nhập
                } else {
                    // Đăng nhập thất bại
                    Toast.makeText(activity_signin.this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                Toast.makeText(activity_signin.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức chuyển sang màn hình đăng ký
    public void goToSignUp(View view) {
        Intent intent = new Intent(this, activity_signup.class);
        startActivity(intent);
    }
}
