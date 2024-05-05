package com.example.appgrit.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.appgrit.Admin_Home;
import com.example.appgrit.ForgotPasswordActivity;
import com.example.appgrit.R;
import com.example.appgrit.helper.SynthesizeRoles;
import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserInforModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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

    private CheckBox checkBoxRememberPassword; // Thêm CheckBox
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        btn_gg = findViewById(R.id.btn_gg);
        checkBoxRememberPassword = findViewById(R.id.check_box_remember_password); // Ánh xạ CheckBox
        ImageButton togglePasswordVisibilityButton = findViewById(R.id.image_button_toggle_password_visibility);
        // Thiết lập bộ lắng nghe sự kiện cho ImageButton
        togglePasswordVisibilityButton.setOnClickListener(v -> {
            // Đảo ngược trạng thái của biến boolean
            isPasswordVisible = !isPasswordVisible;

            // Lấy tham chiếu đến EditText chứa mật khẩu
            EditText passwordEditText = findViewById(R.id.edit_text_password);

            // Thay đổi loại của EditText dựa trên trạng thái của biến boolean
            if (isPasswordVisible) {
                // Hiện mật khẩu
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                togglePasswordVisibilityButton.setImageResource(R.drawable.view);
            } else {
                // Ẩn mật khẩu
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibilityButton.setImageResource(R.drawable.hide);
            }


            // Di chuyển con trỏ về cuối của EditText sau khi thay đổi loại
            passwordEditText.setSelection(passwordEditText.getText().length());
        });

        // Kiểm tra và hiển thị mật khẩu đã ghi nhớ nếu có
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedEmail = prefs.getString("email", "");
        String savedPassword = prefs.getString("password", "");
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            editTextEmail.setText(savedEmail);
            editTextPassword.setText(savedPassword);
            checkBoxRememberPassword.setChecked(true);
        }

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
    public void changeAppChat(String appId){
// Tên gói ứng dụng của ứng dụng bạn muốn mở
        String packageName = appId;

// Tạo một Intent để mở ứng dụng với tên gói ứng dụng đã biết
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if (intent != null) {
            // Kiểm tra xem Intent có hợp lệ không trước khi mở ứng dụng
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),intent.toString(),Toast.LENGTH_SHORT).show();
        }
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

        Log.e("idtoken",idToken);
        // Gọi API đăng nhập bằng Retrofit
        UserApiService service = ApiServiceProvider.getUserApiService();

        Call<TokenModel> call = service.SignInGoogle(idToken);

        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    // Đăng nhập thành công
                    TokenModel tokenModel = response.body();

                    if (response.isSuccessful() && response.body() != null) {
                        String accessToken = response.body().getAccessToken();
                        String refreshToken = response.body().getRefreshToken();
                        String date = response.body().getExpiration();
                        // Save the access token in SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        prefs.edit().putString("accessToken", accessToken).apply();
                        prefs.edit().putString("refreshToken", refreshToken).apply();
                        prefs.edit().putString("expiration", date).apply();
                        boolean isAdmin = CheckAdmin(accessToken);
                        if(isAdmin){
                            ChangeAcAdmin();
                        }else {
                            getUserInfo(accessToken);
                            Toast.makeText(activity_signin.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        }
                        // Now get user info

                    }
                }
                else {
                     Toast.makeText(activity_signin.this, "Login failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                Toast.makeText(activity_signin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean CheckAdmin(String token){

        JWSObject jwsObject;
        JWTClaimsSet claims = null;

        try {
            jwsObject = JWSObject.parse(token);
            claims =  JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
            // now access any claims you want using the relevant key. It will be returned as an object
            List<String> role = claims.getStringListClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
            Log.e("size_role",role.size()+"");
            for(String value : role){
                if(value.contains(SynthesizeRoles.ADMIN))
                    return  true;
            }
        } catch (java.text.ParseException e) {

            Log.e("error",e.toString());
        }
        return false;
    }
    public void ChangeAcAdmin(){
        Intent intent = new Intent(getApplicationContext(), Admin_Home.class);
        startActivity(intent);
    }
    // Phương thức gọi API đăng nhập
    private void signIn(String email, String password) {
        SignInModel signInModel = new SignInModel(email, password);
        UserApiService service = ApiServiceProvider.getUserApiService();

        Call<TokenModel> call = service.signIn(signInModel);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    // Đăng nhập thành công
                    TokenModel tokenModel = response.body();

                    if (response.body() != null) {
                        String accessToken = response.body().getAccessToken();
                        String refreshToken = response.body().getRefreshToken();
                        String date = response.body().getExpiration();
                        // Save the access token in SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        prefs.edit().putString("accessToken", accessToken).apply();
                        prefs.edit().putString("refreshToken", refreshToken).apply();
                        prefs.edit().putString("expiration", date).apply();

                        // Lưu thông tin mật khẩu vào SharedPreferences nếu người dùng chọn tùy chọn ghi nhớ mật khẩu
                        SharedPreferences.Editor editor = prefs.edit();
                        if (checkBoxRememberPassword.isChecked()) {
                            editor.putString("email", email);
                            editor.putString("password", password);
                        } else {
                            // Nếu người dùng không chọn tùy chọn ghi nhớ mật khẩu, xóa thông tin mật khẩu trong SharedPreferences
                            editor.remove("email");
                            editor.remove("password");
                        }
                        editor.apply();

                        // Now get user info
                        getUserInfo(accessToken);
                        boolean isAdmin = CheckAdmin(accessToken);
                        Log.e("admin",isAdmin+"");
                        if(isAdmin){
                            ChangeAcAdmin();
                        }else {
                            getUserInfo(accessToken);
                            Toast.makeText(activity_signin.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(activity_signin.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    }

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
        JWT parsedJWT = new JWT(accessToken);
        Claim subscriptionMetaData = parsedJWT.getClaim("userId");
        String userId = subscriptionMetaData.asString();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefs.edit().putString("userId", userId).apply();
        navigateToHome();

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

    public void goToForgot(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
