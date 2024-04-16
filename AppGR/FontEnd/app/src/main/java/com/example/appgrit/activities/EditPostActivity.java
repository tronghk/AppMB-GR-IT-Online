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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appgrit.R;
import com.example.appgrit.models.ImagePostModel;
import com.example.appgrit.models.PostModel;
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

public class EditPostActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    EditText editTextContent;
    ImageView imageViewSelectImage;
    Button buttonUpdate;

    private String userId;
    private String postId; // Thêm biến để lưu trữ postId
    private List<Uri> selectedImageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        editTextContent = findViewById(R.id.edittext_description);
        imageViewSelectImage = findViewById(R.id.image_placeholder);
        buttonUpdate = findViewById(R.id.button_update);

        imageViewSelectImage.setOnClickListener(v -> openFileChooser());

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "");

        // Lấy postId từ Intent
        postId = getIntent().getStringExtra("postId");

        buttonUpdate.setOnClickListener(v -> {
            String content = editTextContent.getText().toString().trim();
            if (!content.isEmpty()) {
                updatePost(content, selectedImageUris);
            } else {
                Toast.makeText(EditPostActivity.this, "Please enter some content", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
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
            updateSelectedImagesUI();
        }
    }

    private void updateSelectedImagesUI() {
        imageViewSelectImage.setImageResource(0);
        if (!selectedImageUris.isEmpty()) {
            imageViewSelectImage.setImageURI(selectedImageUris.get(0));
        }
    }

    private void updatePost(String content, List<Uri> imageUris) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");

        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri imageUri : imageUris) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), buffer);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

                imageParts.add(body);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to read image data", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<List<String>> call = service.uploadImages("Bearer " + token, imageParts);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    List<String> imageUrls = response.body();
                    editPost(token, imageUrls, content);
                } else {
                    Toast.makeText(EditPostActivity.this, "Image upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Image upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPost(String token, List<String> imageUrls, String content) {
        PostModel postModel = new PostModel();
        postModel.setUserId(userId);
        postModel.setPostId(postId); // Đặt postId vào postModel

        postModel.setContent(content);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String postTimeString = sdf.format(new Date());
        postModel.setPostTimeString(postTimeString);

        int postTypeValue = 1;
        postModel.setPostType(String.valueOf(postTypeValue));

        List<ImagePostModel> imagePosts = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            ImagePostModel imagePostModel = new ImagePostModel();
            imagePostModel.setImagePath(imageUrl);
            imagePosts.add(imagePostModel);
        }
        postModel.setImagePost(imagePosts);

        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<PostModel> postCall = service.editPost("Bearer " + token, postModel);
        postCall.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPostActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditPostActivity.this, activity_home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditPostActivity.this, "Failed to update post: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Error updating post: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

