package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appgrit.models.TokenModel;
import com.example.appgrit.services.ProfileServices;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends AppCompatActivity {

    private ImageView back;
    private LinearLayout top_bar;
    private CircleImageView image_profile;
    private TextView posts;
    private TextView follower;
    private TextView following;
    private LinearLayout mid_bar;
    private TextView fullname;
    private LinearLayout setting;
    private Button btn_share;
    private Button btn_edit;
    private LinearLayout last_bar;
    private ImageView my_photo;
    private ImageView share_photo;
    private RecyclerView recycler_view;
    private RecyclerView recycler_view_share;

    private ProfileServices _profileService;
    private TokenModel tokenModel;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentt_profile);

        // create obj
        back = findViewById(R.id.back);
        top_bar = findViewById(R.id.top_bar);
        image_profile = findViewById(R.id.image_profile);
        posts = findViewById(R.id.posts);
        follower = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        mid_bar = findViewById(R.id.mid_bar);
        fullname = findViewById(R.id.fullname);
        setting = findViewById(R.id.setting);
        btn_edit = findViewById(R.id.btn_edit);
        btn_share = findViewById(R.id.btn_share);
        last_bar = findViewById(R.id.last_bar);
        my_photo = findViewById(R.id.my_photos);
        share_photo = findViewById(R.id.save_photos);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view_share = findViewById(R.id.recycler_view_save);
        _profileService = new ProfileServices();
        prefs = getSharedPreferences("Token", MODE_PRIVATE);
        String accessToken = prefs.getString("accessToken", "No name defined");//"No name defined" is the default value.
        String refreshToken = prefs.getString("refreshToken", "No name defined");
        String timeToken = prefs.getString("tokenTime", "No name defined");

        tokenModel = new TokenModel();
        tokenModel.setAccessToken(accessToken);
        tokenModel.setRefreshToken(refreshToken);
       // tokenModel.setExpiration(new Dat);
        PostSum();

    }

    private void PostSum() {






        String userId = "abc";
        int sum = _profileService.SumPost(userId);
        posts.setText(tokenModel.getAccessToken()+"");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}