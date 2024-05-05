package com.example.appgrit.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.models.ImagePostModel;
import com.example.appgrit.models.PostSellProductModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_edit_marketplace extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextPrice;
    private EditText editTextDescription;
    private Button buttonUpdate;
    private ImageView imageViewProduct;

    private PostApiService postApiService;
    private String postId;
    private List<Uri> selectedImageUris = new ArrayList<>();

    static final int PICK_IMAGE_REQUEST = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_marketplace);

        // Get the Post API Service
        postApiService = ApiServiceProvider.getPostApiService();

        // Get the postId from Intent
        postId = getIntent().getStringExtra("postId");

        // Get other data passed from MarketplaceAdapter
        String productName = getIntent().getStringExtra("productName");
        float price = getIntent().getFloatExtra("price", 0f);
        String description = getIntent().getStringExtra("description");
        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");

        // Initialize UI elements
        editTextTitle = findViewById(R.id.edittext_title);
        editTextPrice = findViewById(R.id.edittext_price);
        editTextDescription = findViewById(R.id.edittext_description);
        buttonUpdate = findViewById(R.id.button_update);
        imageViewProduct = findViewById(R.id.image_placeholder);

        // Set data to UI elements
        editTextTitle.setText(productName);
        editTextPrice.setText(String.valueOf(price));
        editTextDescription.setText(description);

        // Load the first image to display if available
        if (imageUrls != null && !imageUrls.isEmpty()) {
            Glide.with(this).load(imageUrls.get(0)).into(imageViewProduct);
        }

        // Handle update event
        buttonUpdate.setOnClickListener(view -> updatePostSell());

        // Handle image selection
        Button buttonAddImage = findViewById(R.id.button_add_image);
        buttonAddImage.setOnClickListener(v -> openFileChooser());
    }


    private void loadPostDetails() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        // Assuming you have a method to fetch post details based on postId
        // Replace this with your actual method
        // Here, I'm just setting dummy data for demonstration
        editTextTitle.setText("Product Title");
        editTextPrice.setText("100");
        editTextDescription.setText("Product Description");

        // You can load images similarly if needed
        // For now, I'm just clearing any previous selection
        selectedImageUris.clear();
    }

    private void updatePostSell() {
        // Lấy các giá trị cập nhật từ giao diện người dùng
        String title = editTextTitle.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || priceString.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        float price = Float.parseFloat(priceString);

        // Tạo một PostSellProductModel với các giá trị cập nhật
        PostSellProductModel updatedPost = new PostSellProductModel();
        updatedPost.setPostSellProductId(postId); // Set postId
        updatedPost.setProductName(title);
        updatedPost.setPrice(price);
        updatedPost.setContent(description);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String postTimeString = sdf.format(new Date());
        updatedPost.setPostTime(postTimeString);

        // Đặt UserId dựa trên người dùng hiện tại
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", "");
        updatedPost.setUserId(userId);

        // Đặt imagePosts (tuỳ chọn, tùy thuộc vào yêu cầu của máy chủ của bạn)
        List<ImagePostModel> imagePosts = new ArrayList<>();
        for (Uri imageUri : selectedImageUris) {
            ImagePostModel imagePost = new ImagePostModel();
            imagePost.setImagePath(imageUri.toString());
            imagePosts.add(imagePost);
        }
        updatedPost.setImagePosts(imagePosts);

        // Gọi backend để cập nhật bài đăng
        String token = "Bearer " + prefs.getString("accessToken", "");

        // Kiểm tra nếu không có hình ảnh được chọn
        if (selectedImageUris.isEmpty()) {
            // Không có hình ảnh, gọi API để cập nhật bài đăng trực tiếp
            updatePostDirectly(updatedPost, token);
            return;
        }

        // Tạo danh sách MultipartBody.Part từ URI của hình ảnh đã chọn
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri imageUri : selectedImageUris) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                // Tạo RequestBody từ byte[] của hình ảnh
                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), buffer);

                // Tạo MultipartBody.Part từ RequestBody
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

                // Thêm MultipartBody.Part vào danh sách imageParts
                imageParts.add(body);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to read image data", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Gọi API để tải lên các hình ảnh đã chọn
        postApiService.uploadImages(token, imageParts).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    // Nếu tải lên hình ảnh thành công, tiếp tục cập nhật bài đăng
                    List<String> imageUrls = response.body();
                    // Cập nhật URL hình ảnh vào bài đăng
                    for (int i = 0; i < imageUrls.size(); i++) {
                        if (i < imagePosts.size()) {
                            imagePosts.get(i).setImagePath(imageUrls.get(i));
                        }
                    }
                    // Gọi API để cập nhật bài đăng
                    updatePostDirectly(updatedPost, token);
                } else {
                    Toast.makeText(activity_edit_marketplace.this, "Image upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(activity_edit_marketplace.this, "Image upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePostDirectly(PostSellProductModel updatedPost, String token) {
        // Gọi API để cập nhật bài đăng
        postApiService.updateSellPost(token, updatedPost).enqueue(new Callback<PostSellProductModel>() {
            @Override
            public void onResponse(Call<PostSellProductModel> call, Response<PostSellProductModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity_edit_marketplace.this, "Post updated successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_edit_marketplace.this, MarketplaceActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(activity_edit_marketplace.this, "Failed to update post: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostSellProductModel> call, Throwable t) {
                Toast.makeText(activity_edit_marketplace.this, "Error updating post: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUris.clear();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
            }
            // Update the UI to display the selected images
            updateSelectedImagesUI();
        }
    }

    private void updateSelectedImagesUI() {
        imageViewProduct.setImageResource(0);
        if (!selectedImageUris.isEmpty()) {
            // Display the first selected image in the ImageView
            imageViewProduct.setImageURI(selectedImageUris.get(0));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFileChooser();
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

}
