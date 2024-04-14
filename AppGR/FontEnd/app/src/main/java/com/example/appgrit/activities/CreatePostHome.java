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
import com.example.appgrit.models.PostModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;

import java.io.InputStream;
import java.text.ParseException;
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

public class CreatePostHome extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    EditText editTextContent;
    ImageView imageViewSelectImage;
    Button buttonPost;

    private String userId;
    private List<Uri> selectedImageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post_home);

        editTextContent = findViewById(R.id.edittext_description);
        imageViewSelectImage = findViewById(R.id.image_placeholder);
        buttonPost = findViewById(R.id.button_upload);

        imageViewSelectImage.setOnClickListener(v -> openFileChooser());

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "");

        buttonPost.setOnClickListener(v -> {
            String content = editTextContent.getText().toString().trim();
            if (!content.isEmpty() && !selectedImageUris.isEmpty()) {
                uploadImagesAndCreatePost(content, selectedImageUris);
            } else {
                Toast.makeText(CreatePostHome.this, "Please enter some content and select an image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openFileChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều hình ảnh
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền, mở hộp thoại chọn ảnh
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            } else {
                // Người dùng từ chối cấp quyền, hiển thị thông báo
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
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
            // Cập nhật giao diện người dùng tại đây
            updateSelectedImagesUI();
        }
    }

    // Phương thức để cập nhật giao diện người dùng sau khi người dùng chọn hình ảnh
    private void updateSelectedImagesUI() {
        // Xóa hình ảnh hiện có trong ImageView
        imageViewSelectImage.setImageResource(0);
        // Hiển thị hình ảnh đầu tiên trong danh sách đã chọn
        if (!selectedImageUris.isEmpty()) {
            imageViewSelectImage.setImageURI(selectedImageUris.get(0));
        }
    }


    private void uploadImagesAndCreatePost(String content, List<Uri> imageUris) {
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
                    createPost(token, imageUrls, content);
                } else {
                    Toast.makeText(CreatePostHome.this, "Image upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(CreatePostHome.this, "Image upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onSelectImage(View view) {
        openFileChooser();
    }

    private void createPost(String token, List<String> imageUrls, String content) {
        PostModel postModel = new PostModel();
        postModel.setUserId(userId);
        postModel.setContent(content);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String postTimeString = sdf.format(new Date()); // Định dạng thời gian hiện tại thành String
        postModel.setPostTimeString(postTimeString); // Sử dụng phương thức setPostTimeString thay vì setPostTime

        int postTypeValue = 1; // Giả sử postTypeValue là 1
        postModel.setPostType(String.valueOf(postTypeValue));

        List<ImagePostModel> imagePosts = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            ImagePostModel imagePostModel = new ImagePostModel();
            imagePostModel.setImagePath(imageUrl);
            imagePosts.add(imagePostModel);
        }
        postModel.setImagePost(imagePosts);

        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<PostModel> postCall = service.addPost("Bearer " + token, postModel);
        postCall.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreatePostHome.this, "Post created successfully", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình activity_home
                    Intent intent = new Intent(CreatePostHome.this, activity_home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreatePostHome.this, "Failed to create post: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Toast.makeText(CreatePostHome.this, "Error creating post: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
