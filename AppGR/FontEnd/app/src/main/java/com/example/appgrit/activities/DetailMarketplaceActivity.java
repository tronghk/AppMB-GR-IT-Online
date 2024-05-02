package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.comment;
import com.example.appgrit.models.ImagePostModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMarketplaceActivity extends AppCompatActivity {
    private ImageView sellerAvatarImageView;
    private TextView sellerUsernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_marketplace);
        sellerAvatarImageView = findViewById(R.id.image_seller_avatar);
        sellerUsernameTextView = findViewById(R.id.text_seller_username);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String productName = bundle.getString("productName", "");
            String content = bundle.getString("content", "");
            List<ImagePostModel> imagePosts = (List<ImagePostModel>) bundle.getSerializable("imagePosts");
            float price = bundle.getFloat("price", 0.0f); // Giá trị mặc định là 0.0 nếu không tìm thấy giá trị
            String userId = bundle.getString("userId", "");
            // Gọi phương thức để lấy thông tin của người bán
            loadSellerData(userId);

            TextView textView = findViewById(R.id.text_product_title);
            textView.setText(productName);

            TextView contentTextView = findViewById(R.id.text_product_description);
            contentTextView.setText(content);

            TextView priceTextView = findViewById(R.id.text_product_price);
            priceTextView.setText(String.valueOf(price));

            ImageView imageView = findViewById(R.id.image_product);
            ImagePostModel imagePost = imagePosts.get(0);
            Glide.with(this).load(imagePost.getImagePath()).into(imageView);
        }

        TextView textSellerComments = findViewById(R.id.text_seller_comments);
        textSellerComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy postId từ bundle
                String postSellProductId = bundle.getString("postSellProductId", "");
                Intent intent = new Intent(DetailMarketplaceActivity.this, comment.class);
                // Gửi postId qua intent
                intent.putExtra("postId", postSellProductId);
                startActivity(intent);
            }
        });

    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getString("accessToken", "");
    }

    private void loadSellerData(String userId) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        String token = getTokenFromSharedPreferences();

        Call<UserModel> call = service.getUserBasic("Bearer " + token, userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel user = response.body();
                    // Hiển thị thông tin người bán trên giao diện
                    sellerUsernameTextView.setText(user.getUserName());
                    Glide.with(DetailMarketplaceActivity.this)
                            .load(user.getImagePath())
                            .placeholder(R.drawable.avartar_user)
                            .error(R.drawable.avartar_user)
                            .into(sellerAvatarImageView);
                } else {
                    Log.e("API Error", "Failed to load user data: " + response.message());
                    // Xử lý trường hợp API không thành công
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Network Error", "Error loading user data: " + t.getMessage());
                // Xử lý trường hợp lỗi mạng
            }
        });
    }
}
