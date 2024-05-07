package com.example.appgrit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.adapters.ImageGalleryAdapter;
import com.example.appgrit.comment;
import com.example.appgrit.models.ImagePostModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMarketplaceActivity extends AppCompatActivity {
    private ImageView sellerAvatarImageView;
    private TextView sellerUsernameTextView;
    private RecyclerView imageGalleryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_marketplace);
        sellerAvatarImageView = findViewById(R.id.image_seller_avatar);
        sellerUsernameTextView = findViewById(R.id.text_seller_username);
        imageGalleryRecyclerView = findViewById(R.id.imageGallery);

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

            // Kiểm tra số lượng ảnh
            if (imagePosts.size() > 1) {
                // Ẩn ImageView nếu có nhiều hơn một ảnh
                imageView.setVisibility(View.GONE);

                // Hiển thị RecyclerView nếu có nhiều hơn một ảnh
                imageGalleryRecyclerView.setVisibility(View.VISIBLE);

                // Tạo và thiết lập adapter cho RecyclerView
                List<String> imageUrls = new ArrayList<>();
                for (ImagePostModel post : imagePosts) {
                    imageUrls.add(post.getImagePath());
                }
                ImageGalleryAdapter adapter = new ImageGalleryAdapter(DetailMarketplaceActivity.this, imageUrls);
                imageGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(DetailMarketplaceActivity.this, LinearLayoutManager.HORIZONTAL, false));
                imageGalleryRecyclerView.setAdapter(adapter);

                // Thêm lắng nghe sự kiện cho từng ảnh trong RecyclerView
                adapter.setOnImageClickListener(new ImageGalleryAdapter.OnImageClickListener() {
                    @Override
                    public void onImageClick(int position) {
                        // Xem chi tiết ảnh khi bấm vào
                        Intent intent = new Intent(DetailMarketplaceActivity.this, ImageDetailActivity.class);
                        intent.putExtra("imagePath", imageUrls.get(position));
                        startActivity(intent);
                    }
                });
            } else {
                // Hiển thị ImageView nếu chỉ có một ảnh
                imageView.setVisibility(View.VISIBLE);
                imageGalleryRecyclerView.setVisibility(View.GONE);
            }
        }

//        TextView textSellerComments = findViewById(R.id.text_seller_comments);
//        textSellerComments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Lấy postId từ bundle
//                String postSellProductId = bundle.getString("postSellProductId", "");
//                Intent intent = new Intent(DetailMarketplaceActivity.this, comment.class);
//                // Gửi postId qua intent
//                intent.putExtra("postId", postSellProductId);
//                startActivity(intent);
//            }
//        });
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
