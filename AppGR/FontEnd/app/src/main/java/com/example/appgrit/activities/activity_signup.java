package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.R;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.network.UserApiService;
import com.example.appgrit.network.ApiServiceProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_signup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextConfirmPassword, editTextBirthday, editTextGender;
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
        editTextGender = findViewById(R.id.edit_text_gender);
        buttonSignUp = findViewById(R.id.button_sign_up);
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            updateLabel(selectedDate);
        }
    }

    private void updateLabel(Calendar selectedDate) {
        if (editTextBirthday != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = formatter.format(selectedDate.getTime());
            editTextBirthday.setText(formattedDate);
        }
    }

    private void attemptSignUp() {
        SignUpModel signUpModel = new SignUpModel();
        signUpModel.setFirstName(editTextFirstName.getText().toString().trim());
        signUpModel.setLastName(editTextLastName.getText().toString().trim());
        signUpModel.setEmail(editTextEmail.getText().toString().trim());
        signUpModel.setPassword(editTextPassword.getText().toString());
        signUpModel.setPasswordConfirmation(editTextConfirmPassword.getText().toString());
        signUpModel.setGender(editTextGender.getText().toString()); // Lấy giá trị giới tính từ EditText

        // Chuyển đổi ngày sinh từ String thành Date
        String birthdayText = editTextBirthday.getText().toString();
        if (!birthdayText.isEmpty()) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date birthday = formatter.parse(birthdayText);
                signUpModel.setBirthday(birthday); // Đặt ngày sinh
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            // Xử lý trường hợp người dùng chưa chọn ngày sinh
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return;
        }

        UserApiService apiService = ApiServiceProvider.getApiService();
        Call<TokenModel> call = apiService.signUp(signUpModel);

        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    TokenModel tokenModel = response.body();
                    if (tokenModel != null) {
                        handleSignUpSuccess(); // Xử lý nếu đăng ký thành công
                        Toast.makeText(activity_signup.this, "Đăng ký thành công và đã nhận được token!", Toast.LENGTH_LONG).show();
                    } else {
                        handleSignUpFailure("Unknown error"); // Xử lý nếu đăng ký không thành công
                    }
                } else {
                    handleSignUpFailure("Có lỗi xảy ra khi đăng ký"); // Xử lý lỗi không thành công
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                handleNetworkFailure(t); // Xử lý lỗi kết nối
            }
        });
    }

    private void handleSignUpSuccess() {
        // Xử lý khi đăng ký thành công
        Toast.makeText(activity_signup.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity_signup.this, activity_signin.class);
        startActivity(intent);
        finish(); // Đóng activity hiện tại để ngăn người dùng quay lại màn hình đăng ký
    }

    private void handleSignUpFailure(String errorMessage) {
        // Xử lý khi đăng ký không thành công
        Toast.makeText(activity_signup.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void handleNetworkFailure(Throwable t) {
        // Xử lý lỗi kết nối
        Toast.makeText(activity_signup.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
