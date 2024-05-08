package com.example.appgrit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appgrit.R;
import com.example.appgrit.helper.JWTServices;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;
import com.example.appgrit.services.PaymentApiService;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GetImage();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upgrade_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnPayment = findViewById(R.id.btn_payment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String selectedRadioButtonText = selectedRadioButton.getText().toString();
                    int month = Integer.parseInt(selectedRadioButtonText.substring(0, selectedRadioButtonText.indexOf(" ")));
                    getLinkPayment(String.valueOf(month));
                } else {
                    Toast.makeText(UpgradeAccountActivity.this, "Please select a radio button", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void GetImage(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", "");
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = service.getUserInfo(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    if (user != null) {
                        String userName = user.getUserName();
                        String imagePath = user.getImagePath();

                        // Display user name in textView4
                        TextView textView4 = findViewById(R.id.textView4);
                        textView4.setText(userName);

                        // Load and display user image in imageView2 using Picasso
                        ImageView imageView2 = findViewById(R.id.imageView2);
                        Picasso.get().load(imagePath).into(imageView2);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching user info: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching user info: ", t);
            }
        });
    }


    private void getLinkPayment(String month){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", "");
        PaymentApiService service = ApiServiceProvider.getPaymentApiService();
        Call<ResponseBody> call = service.payment("Bearer " + token, userId, month);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String payUrl = response.body().string();
                        openLinkPayment(payUrl);
                    } catch (IOException e) {
                        e.printStackTrace();}
                } else {
                    Toast.makeText(UpgradeAccountActivity.this, "Failed to fetch payment link: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UpgradeAccountActivity.this, "Error fetching payment link: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching payment link: ", t);
            }
        });
    }

    private void openLinkPayment(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}