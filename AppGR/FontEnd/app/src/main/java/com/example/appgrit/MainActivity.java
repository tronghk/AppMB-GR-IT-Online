package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.appgrit.activities.activity_home;
import com.example.appgrit.activities.activity_signin;
import com.example.appgrit.activities.activity_signup;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

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
        if(prefs != null){
            String expiration = prefs.getString("expiration", "");


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                // giờ token
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(expiration));


                // giờ hiện tại


                Date date = new Date();
                date.setHours(date.getHours()-7);

                Calendar myCal = new GregorianCalendar();
                myCal.setTime(date);
                int compa = myCal.compareTo((calendar));


                if(compa < 0){
                   /* Intent signinIntent = new Intent(getApplicationContext(), activity_home.class);
                    startActivity(signinIntent);*/
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }



            // Parse chuỗi thành LocalDateTime

        }

    }

//    new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {
//            Intent intent = new Intent(Activity_Test.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }, 2000);
}
