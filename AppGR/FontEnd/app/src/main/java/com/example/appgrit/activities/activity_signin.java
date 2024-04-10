package com.example.appgrit.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.HomeActivity;
import com.example.appgrit.HomeFragment;
import com.example.appgrit.R;
import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_signin extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private  Button btn_gg;
    GoogleSignInClient googleSignInClient;
    int Rc_SignIn = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Ánh xạ views
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        btn_gg = findViewById(R.id.btn_gg);

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

    @Override
    protected void onResume() {
        super.onResume();
        EventClickBtnGG();
    }
    public  void  EventClickBtnGG(){
        GoogleSignInOptions opt = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,opt);
        btn_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    GoogleSignIn();
            }
        });
    }
    public void GoogleSignIn(){
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,Rc_SignIn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Rc_SignIn){
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                SignInGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private  void SignInGoogle(String idToken){


        // Gọi API đăng nhập bằng Retrofit
        UserApiService apiService = ApiServiceProvider.getApiService();
        Call<TokenModel> call = apiService.SignInGoogle(idToken);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    // Đăng nhập thành công
                    TokenModel tokenModel = response.body();
                    if (tokenModel != null) {
                        // Nhận được token từ API
                        handleSignInSuccess();
                    } else {
                        // Đăng nhập không thành công vì không nhận được token
                        handleSignInFailure("Không nhận được token từ server");
                    }
                } else {
                    // Đăng nhập thất bại
                    handleSignInFailure("Đăng nhập google thất bại");
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                handleNetworkFailure(t);
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
                    if (tokenModel != null) {
                        // Nhận được token từ API
                        handleSignInSuccess();
                    } else {
                        // Đăng nhập không thành công vì không nhận được token
                        handleSignInFailure("Không nhận được token từ server");
                    }
                } else {
                    // Đăng nhập thất bại
                    handleSignInFailure("Email hoặc mật khẩu không chính xác");
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                handleNetworkFailure(t);
            }
        });
    }

    // Xử lý khi đăng nhập thành công
    private void handleSignInSuccess() {
        Toast.makeText(activity_signin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        // Chuyển hướng sang màn hình chính
        Intent intent = new Intent(activity_signin.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Đóng activity hiện tại để ngăn người dùng quay lại màn hình đăng nhập
    }

    // Xử lý khi đăng nhập không thành công
    private void handleSignInFailure(String errorMessage) {
        Toast.makeText(activity_signin.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // Xử lý lỗi kết nối
    private void handleNetworkFailure(Throwable t) {
        Toast.makeText(activity_signin.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    // Phương thức chuyển sang màn hình đăng ký
    public void goToSignUp(View view) {
        Intent intent = new Intent(this, activity_signup.class);
        startActivity(intent);
    }
}
