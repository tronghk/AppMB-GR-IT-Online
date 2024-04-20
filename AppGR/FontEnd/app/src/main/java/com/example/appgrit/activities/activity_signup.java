package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.appgrit.R;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserInforModel;
import com.example.appgrit.models.UserModel;
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
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextConfirmPassword, editTextBirthday;
    private Button buttonSignUp;
    private RadioButton radioButtonMale, radioButtonFemale;
    private RadioGroup radioGroupGender;
    private final Calendar calendar = Calendar.getInstance();
    // Khởi tạo giá trị mặc định cho giới tính
    private String gender = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        bindViews();
        setupGenderRadioGroup();
        editTextBirthday.setOnClickListener(v -> showDatePickerDialog());
        buttonSignUp.setOnClickListener(v -> attemptSignUp());
    }

    private void setupGenderRadioGroup() {
        // Xử lý sự kiện khi lựa chọn giới tính
        radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_button_male) {
                gender = "Male";
            } else if (checkedId == R.id.radio_button_female) {
                gender = "Female";
            }
        });
    }
    private void bindViews() {
        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        editTextBirthday = findViewById(R.id.edit_text_birthday);
//        editTextGender = findViewById(R.id.edit_text_gender);
        radioButtonMale = findViewById(R.id.radio_button_male);
        radioButtonFemale = findViewById(R.id.radio_button_female);
        radioGroupGender = findViewById(R.id.radio_group_gender);
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
//        signUpModel.setGender(editTextGender.getText().toString()); // Lấy giá trị giới tính từ EditText
        signUpModel.setGender(gender); // Sử dụng giá trị giới tính từ RadioGroup


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

        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<TokenModel> call = apiService.signUp(signUpModel);

        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    TokenModel tokenModel = response.body();
                    if (tokenModel != null && tokenModel.getAccessToken() != null) {
                        handleSignUpSuccess(tokenModel);
                    } else {
                        handleSignUpFailure("Dữ liệu token không hợp lệ");
                    }
                } else {
                    // Xử lý trường hợp phản hồi không thành công
                    handleSignUpFailure("Đăng ký không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }

    private void handleSignUpSuccess(TokenModel tokenModel) {
        // Kiểm tra xem tokenModel có null hay không trước khi lưu vào SharedPreferences
        if (tokenModel != null && tokenModel.getAccessToken() != null) {
            saveTokenToSharedPreferences(tokenModel.getAccessToken());
            Toast.makeText(activity_signup.this, "Đăng ký thành công và đã nhận được token!", Toast.LENGTH_LONG).show();

            // Lấy thông tin người dùng sau khi đăng ký thành công để lấy userId
            getUserInfo(tokenModel.getAccessToken());

            Intent intent = new Intent(activity_signup.this, activity_signin.class);
            startActivity(intent);
            finish(); // Đóng activity hiện tại để ngăn người dùng quay lại màn hình đăng ký
        } else {
            handleSignUpFailure("Không nhận được token từ máy chủ"); // Xử lý nếu không nhận được token
        }
    }


    private void getUserInfo(String accessToken) {
        // Lấy email từ EditText
        String email = editTextEmail.getText().toString().trim();

        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<UserInforModel> call = apiService.getUserInfo("Bearer " + accessToken, email);

        call.enqueue(new Callback<UserInforModel>() {
            @Override
            public void onResponse(Call<UserInforModel> call, Response<UserInforModel> response) {
                if (response.isSuccessful()) {
                    UserInforModel userModel = response.body();
                    if (userModel != null) {
                        // Xử lý thông tin người dùng ở đây
                    } else {
                        handleSignUpFailure("Không nhận được thông tin người dùng từ máy chủ");
                    }
                } else {
                    handleSignUpFailure("Lấy thông tin người dùng không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserInforModel> call, Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }



    // Phương thức lưu token vào SharedPreferences
    private void saveTokenToSharedPreferences(String token) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
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
