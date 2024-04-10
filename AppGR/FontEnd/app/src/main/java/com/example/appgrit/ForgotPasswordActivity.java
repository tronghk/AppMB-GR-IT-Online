package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button submitButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.edt_email);
        submitButton = findViewById(R.id.btn_submit);
        cancelButton = findViewById(R.id.btn_cancel);

        // Xử lý sự kiện click cho nút Submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForgotPasswordRequest();
            }
        });

        // Xử lý sự kiện click cho nút Cancel
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendForgotPasswordRequest() {
        String email = emailEditText.getText().toString().trim();

        if (!email.isEmpty()) {
            // Gửi yêu cầu quên mật khẩu đến API
            sendForgotPasswordRequestToAPI(email);
        } else {
            // Hiển thị thông báo lỗi nếu email trống
            showErrorMessage("Vui lòng nhập email");
        }
    }

    private void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendForgotPasswordRequestToAPI(final String email) {
        UserApiService apiService = ApiServiceProvider.getApiService();
        Call<ResponseBody> call = apiService.resetPassword(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        handleForgotPasswordResponse(responseBody);
                    } catch (IOException e) {
                        showErrorMessage("Đã xảy ra lỗi: " + e.getMessage());
                    }
                } else {
                    showErrorMessage("Đã xảy ra lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMessage("Đã xảy ra lỗi: " + t.getMessage());
            }
        });
    }

    private void handleForgotPasswordResponse(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");

            if (status.equals("SUCCESS")) {
                // Xử lý thành công
                showSuccessMessage("Yêu cầu quên mật khẩu đã được gửi thành công");
            } else {
                // Xử lý thất bại
                showErrorMessage(message);
            }
        } catch (JSONException e) {
            // Xử lý lỗi khi phân tích JSON
            showErrorMessage("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
}