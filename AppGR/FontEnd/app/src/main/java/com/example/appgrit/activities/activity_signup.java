package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.R;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.ResponseModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.network.UserApiService;
import com.example.appgrit.network.ApiServiceProvider;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_signup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextConfirmPassword, editTextBirthday;
    private Button buttonSignUp;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        bindViews();
        editTextBirthday.setOnClickListener(v -> showDatePickerDialog());
        buttonSignUp.setOnClickListener(v -> attemptSignUp());
    }

    private void bindViews() {
        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        editTextBirthday = findViewById(R.id.edit_text_birthday);
        buttonSignUp = findViewById(R.id.button_sign_up);
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editTextBirthday.setText(sdf.format(calendar.getTime()));
    }
    private void attemptSignUp() {
        SignUpModel signUpModel = new SignUpModel();
        signUpModel.setFirstName(editTextFirstName.getText().toString().trim());
        signUpModel.setLastName(editTextLastName.getText().toString().trim());
        signUpModel.setEmail(editTextEmail.getText().toString().trim());
        signUpModel.setPassword(editTextPassword.getText().toString());
        signUpModel.setPasswordConfirmation(editTextConfirmPassword.getText().toString());
        signUpModel.setBirthday(editTextBirthday.getText().toString());

        UserApiService apiService = ApiServiceProvider.getApiService();
        Call<Response<TokenModel>> call = apiService.signUp(signUpModel);

        call.enqueue(new Callback<Response<TokenModel>>() {
            @Override
            public void onResponse(Call<Response<TokenModel>> call, Response<Response<TokenModel>> response) {
                if (response.isSuccessful()) {
                    Response<TokenModel> tokenResponse = response.body();
                    if (tokenResponse != null && tokenResponse.body() != null) {
                        TokenModel tokenModel = tokenResponse.body();
                        // Xử lý token ở đây
                        handleToken(tokenModel);
                    }
                } else {
                    // Có lỗi xảy ra
                    handleSignUpFailure(response);
                }
            }

            @Override
            public void onFailure(Call<Response<TokenModel>> call, Throwable t) {
                // Xử lý lỗi kết nối
                handleNetworkFailure(t);
            }
        });
    }

    private void handleToken(TokenModel tokenModel) {
        // Đăng ký thành công, bạn có thể lưu token vào SharedPreferences hoặc sử dụng theo nhu cầu của bạn
        // Ví dụ:
//         SharedPreferences.Editor editor = getSharedPreferences("com.example.appgrit.TOKEN_PREFS", MODE_PRIVATE).edit();
//         editor.putString("access_token", tokenModel.getAccessToken());
//         editor.putString("refresh_token", tokenModel.getRefreshToken());
//         editor.apply();
        Toast.makeText(activity_signup.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();

        // Chuyển đến activity tiếp theo sau khi đăng ký thành công
        Intent intent = new Intent(activity_signup.this, activity_signin.class);
        startActivity(intent);
        finish(); // Đóng activity hiện tại để ngăn người dùng quay lại màn hình đăng ký
    }

    private void handleSignUpFailure(Response<Response<TokenModel>> response) {
        try {
            // Lấy thông báo lỗi từ body của response
            if (response.errorBody() != null) {
                ResponseModel errorModel = new Gson().fromJson(response.errorBody().string(), ResponseModel.class);
                if (errorModel != null) {
                    Toast.makeText(activity_signup.this, errorModel.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity_signup.this, "Có lỗi xảy ra khi đăng ký", Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            // Không thể đọc thông tin lỗi
            Toast.makeText(activity_signup.this, "Không thể đọc thông tin lỗi", Toast.LENGTH_LONG).show();
        }
    }

    private void handleNetworkFailure(Throwable t) {
        // Xử lý lỗi kết nối
        Toast.makeText(activity_signup.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }



}
