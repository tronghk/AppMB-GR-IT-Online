package com.example.appgrit;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.appgrit.HomeFragment;
import com.example.appgrit.R;
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // Load the HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

    }
}
