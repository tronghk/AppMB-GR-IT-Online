package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.appgrit.activities.CreateMarketplace;
import com.example.appgrit.activities.CreatePostHome;
import com.example.appgrit.activities.DetailMarketplace;
import com.example.appgrit.activities.ListUserLike;
import com.example.appgrit.activities.activity_home;
import com.example.appgrit.activities.activity_signin;
import com.example.appgrit.activities.activity_signup;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MainActivity extends AppCompatActivity {

    private Button buttonSignUp;
    private Button buttonSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_main);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignin = findViewById(R.id.buttonSignin);

        buttonSignUp.setOnClickListener(v -> {
            // Chuyển đến màn hình Signup
            Intent signupIntent = new Intent(MainActivity.this, activity_signup.class);
            startActivity(signupIntent);
        });
        buttonSignin.setOnClickListener(v -> {
            // Chuyển đến màn hình Signin
            Intent signinIntent = new Intent(MainActivity.this, activity_signin.class);
            startActivity(signinIntent);
        });
    }
}
