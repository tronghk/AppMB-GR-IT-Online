package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appgrit.adapters.CommentAdapter;
import com.example.appgrit.models.PostCommentModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostCommentApiService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class comment extends AppCompatActivity {

    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private List<PostCommentModel> commentList;
    private String postId; // Biến lưu trữ postId
    private EditText editComment;
    private Button btnPostComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postId = getIntent().getStringExtra("postId");

        recyclerViewComments = findViewById(R.id.recycler_view);
        editComment = findViewById(R.id.editComment);
        btnPostComment = findViewById(R.id.buttonSend);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(commentAdapter);

        getComments();

        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
    }

    private String getUserToken() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getString("accessToken", "");
    }

    private String getUserId() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getString("userId", "");
    }

    private void getComments() {
        // Gọi API để lấy danh sách comment và cập nhật RecyclerView khi có kết quả
        String token = getUserToken();

        PostCommentApiService service = ApiServiceProvider.getPostCommentApiService();
        service.getComments(token, postId).enqueue(new Callback<List<PostCommentModel>>() {
            @Override
            public void onResponse(Call<List<PostCommentModel>> call, Response<List<PostCommentModel>> response) {
                if (response.isSuccessful()) {
                    commentList.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(comment.this, "Lỗi khi lấy danh sách comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostCommentModel>> call, Throwable t) {
                Toast.makeText(comment.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void postComment() {
        String content = editComment.getText().toString().trim();
        if (!content.isEmpty()) {
            String userId = getUserId(); // Lấy userId từ SharedPreferences
            PostCommentModel newComment = new PostCommentModel();
            newComment.setPostId(postId);
            newComment.setUserId(userId);
            newComment.setContent(content);
            newComment.setCommentTime(new Date()); // Set the current date for comment

            String token = getUserToken();
            if (!token.isEmpty()) {
                token = "Bearer " + token; // Bao gồm mã thông báo trong tiêu đề Authorization
                PostCommentApiService service = ApiServiceProvider.getPostCommentApiService();
                service.addComment(token, newComment).enqueue(new Callback<PostCommentModel>() {
                    @Override
                    public void onResponse(Call<PostCommentModel> call, Response<PostCommentModel> response) {
                        if (response.isSuccessful()) {
                            // Kiểm tra nếu commentId không phải là null trước khi thêm vào danh sách
                            if (response.body().getCommentId() != null) {
                                // Nhận commentId từ phản hồi và thiết lập cho newComment
                                newComment.setCommentId(response.body().getCommentId());
                                // Add the new comment to the list and update the RecyclerView
                                commentList.add(newComment);
                                commentAdapter.notifyDataSetChanged();
                                // Clear the edit text after successfully posting the comment
                                editComment.setText("");
                                // Show a toast message indicating success
                                Toast.makeText(comment.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Show a toast message indicating failure with the error message from the response
                                Toast.makeText(comment.this, "Failed to add comment: Comment ID is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Show a toast message indicating failure with the error message from the response
                            Toast.makeText(comment.this, "Failed to add comment: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostCommentModel> call, Throwable t) {
                        // Show a toast message indicating failure with the error message
                        Toast.makeText(comment.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                        // Log the error message
                        Log.e("PostCommentError", "Error posting comment: " + t.getMessage(), t);
                    }
                });
            } else {
                // Show a toast message indicating that the token is empty
                Toast.makeText(this, "Token is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a toast message indicating that the comment cannot be empty
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }



}
