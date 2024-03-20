package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZSI6InVzZXJAZXhhbXBsZS5jb20iLCJqdGkiOiI5ZDdlOGYyNS00Mzk5LTQ4NWYtYjBlNS1iY2Q4MGZkYTM1MGYiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJDVVNUT01FUiIsImV4cCI6MTcxMDgzNTE5NywiaXNzIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6NzAwMyIsImF1ZCI6IkFwcEdySVQifQ.vtGyKYdO_PQxcKCGmHlQm9hfKxL1eH3P3lfEHCHM7w4";
        JWT jwt = new JWT(token);

        String issuer = jwt.getIssuer(); //get registered claims
        String claim = jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name").asString(); //get custom claims
        boolean isExpired = jwt.isExpired(10);

        Toast.makeText(getApplicationContext(),claim,Toast.LENGTH_SHORT).show();
    }
}