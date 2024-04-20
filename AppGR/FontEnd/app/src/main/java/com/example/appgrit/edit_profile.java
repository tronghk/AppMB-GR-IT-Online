package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.models.UserInforModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class edit_profile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextFirstName, editTextLastName, editTextGender, editTextBirthday, editTextAddress, editTextPhone;
    private Button btnSave;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Bind views
        editTextFirstName = findViewById(R.id.firstnameEditText);
        editTextLastName = findViewById(R.id.lastnameEditText);
        editTextGender = findViewById(R.id.genderEditText);
        editTextBirthday = findViewById(R.id.birthdayEditText);
        editTextAddress = findViewById(R.id.addressEditText);
        editTextPhone = findViewById(R.id.phoneEditText);
        btnSave = findViewById(R.id.savebtn);

        // Set onClickListener for the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        // Set onClickListener for the birthday field to show date picker
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
        updateLabel(selectedDate);
    }

    private void updateLabel(Calendar selectedDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = formatter.format(selectedDate.getTime());
        editTextBirthday.setText(formattedDate);
    }

    private void saveUserProfile() {
        // Get user input from EditText fields
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String birthday = editTextBirthday.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        // Get AccessToken and UserId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accessToken = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", ""); // Assuming you have stored the userId in SharedPreferences

        // Create a UserInforModel object with updated user information
        UserInforModel updatedUserInfo = new UserInforModel(firstName, lastName, gender, birthday, address, userId, phone);

        // Call the API to update user information
        UserApiService apiService = ApiServiceProvider.getUserApiService();
        Call<UserInforModel> call = apiService.editUserInfo("Bearer " + accessToken, updatedUserInfo); // Pass AccessToken in Authorization header

        call.enqueue(new Callback<UserInforModel>() {
            @Override
            public void onResponse(Call<UserInforModel> call, Response<UserInforModel> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Toast.makeText(edit_profile.this, "Thông tin người dùng đã được cập nhật", Toast.LENGTH_SHORT).show();
                    // You can optionally navigate back to the profile screen or perform any other action here
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(edit_profile.this, "Cập nhật thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInforModel> call, Throwable t) {
                // Handle failure
                Toast.makeText(edit_profile.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
