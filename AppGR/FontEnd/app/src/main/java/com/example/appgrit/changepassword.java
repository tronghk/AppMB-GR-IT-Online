package com.example.appgrit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appgrit.models.ChangePasswordModel;
import com.example.appgrit.models.ResponseModel;
import com.example.appgrit.network.ApiServiceProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class changepassword extends AppCompatActivity {

    EditText etEmail, etOldPassword, etNewPassword, etConfirmNewPassword;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        etEmail = findViewById(R.id.etEmail);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String email = etEmail.getText().toString().trim();
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString();
        String confirmNewPassword = etConfirmNewPassword.getText().toString();

        if (newPassword.equals(confirmNewPassword)) {
            ChangePasswordModel changePasswordModel = new ChangePasswordModel(email, oldPassword, newPassword);
            String accessToken = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("accessToken", "");

            if (!accessToken.isEmpty()) {
                String authHeader = "Bearer " + accessToken;

                Call<ResponseModel> call = ApiServiceProvider.getChangePasswordApiService().changePassword(authHeader, changePasswordModel);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            // Xử lý khi thay đổi mật khẩu thành công
                            Toast.makeText(changepassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Đóng màn hình thay đổi mật khẩu sau khi thành công
                        } else {
                            // Xử lý khi thay đổi mật khẩu thất bại
                            Toast.makeText(changepassword.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                        Toast.makeText(changepassword.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Xử lý trường hợp accessToken không tồn tại
                Toast.makeText(changepassword.this, "Access token not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Hiển thị thông báo nếu mật khẩu mới và xác nhận mật khẩu mới không khớp
            Toast.makeText(changepassword.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
        }
    }
}
