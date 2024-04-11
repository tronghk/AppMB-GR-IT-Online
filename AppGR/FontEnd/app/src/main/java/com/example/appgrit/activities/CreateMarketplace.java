package com.example.appgrit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appgrit.R;
import com.example.appgrit.models.ImagePostModel;
import com.example.appgrit.models.PostSellProductModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMarketplace extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;

    EditText editTextTitle, editTextPrice, editTextDescription;
    ImageView imageViewProduct;
    Button buttonAddImage, buttonSubmit;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_marketplace);

        editTextTitle = findViewById(R.id.edittext_title);
        editTextPrice = findViewById(R.id.edittext_price);
        editTextDescription = findViewById(R.id.edittext_description);
        imageViewProduct = findViewById(R.id.image_placeholder);
        buttonAddImage = findViewById(R.id.button_add_image);
        buttonSubmit = findViewById(R.id.button_submit);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "");

        buttonAddImage.setOnClickListener(v -> openFileChooser());
        buttonSubmit.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            float price = Float.parseFloat(editTextPrice.getText().toString().trim());

            if (!title.isEmpty() && !description.isEmpty() && price > 0 && !selectedImageUris.isEmpty()) {
                uploadImagesAndCreatePostSell(title, description, price, selectedImageUris);
            } else {
                Toast.makeText(CreateMarketplace.this, "Please fill in all the details and select at least one image.", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFileChooser();
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
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
            imageViewProduct.setImageURI(selectedImageUris.get(0));
            // You might want to update the UI here to reflect multiple images selected
        }
    }

    private void uploadImagesAndCreatePostSell(String title, String description, float price, List<Uri> imageUris) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        PostApiService service = ApiServiceProvider.getPostApiService();
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), buffer);
                MultipartBody.Part part = MultipartBody.Part.createFormData("image", "file.jpg", requestBody);
                parts.add(part);
            } catch (Exception e) {
                Toast.makeText(CreateMarketplace.this, "Failed to read image data", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // First, upload images
        service.uploadImages("Bearer " + token, parts).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> imageUrls = response.body();
                    // Once images are uploaded, create the post sell
                    createPostSell(token, title, description, price, imageUrls);
                } else {
                    Toast.makeText(CreateMarketplace.this, "Failed to upload images", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(CreateMarketplace.this, "Image upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPostSell(String token, String title, String description, float price, List<String> imageUrls) {
        PostSellProductModel postSell = new PostSellProductModel();
        // Assume you have setters in your PostSellProductModel class for these fields
        postSell.setUserId(userId);
        postSell.setProductName(title);
        postSell.setContent(description);
        postSell.setPrice(price);
        postSell.setPostTime(new Date()); // Or any specific time you want

        List<ImagePostModel> imagePosts = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            ImagePostModel image = new ImagePostModel();
            image.setImagePath(imageUrl);
            imagePosts.add(image);
        }
        postSell.setImagePosts(imagePosts);

        PostApiService service = ApiServiceProvider.getPostApiService();
        service.addSellPost("Bearer " + token, postSell).enqueue(new Callback<PostSellProductModel>() {
            @Override
            public void onResponse(Call<PostSellProductModel> call, Response<PostSellProductModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateMarketplace.this, "Marketplace post created successfully!", Toast.LENGTH_SHORT).show();
                    // Optionally navigate back to the marketplace listing screen or clear the form
                } else {
                    Toast.makeText(CreateMarketplace.this, "Failed to create marketplace post: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostSellProductModel> call, Throwable t) {
                Toast.makeText(CreateMarketplace.this, "Error creating marketplace post: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
