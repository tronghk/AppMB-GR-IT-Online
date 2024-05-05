package com.example.appgrit.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.comment;
import com.example.appgrit.models.ExpressionModel;
import com.example.appgrit.models.PostCommentModel;
import com.example.appgrit.models.ResponseModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.ExpressionApiService;
import com.example.appgrit.network.PostCommentApiService;
import com.example.appgrit.network.UserApiService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<PostCommentModel> commentList;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String postId; // Biến lưu trữ postId

    public CommentAdapter(Context context, List<PostCommentModel> commentList, String postId) {
        this.context = context;
        this.commentList = commentList;
        this.sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        this.postId = postId; // Lưu trữ postId được truyền vào constructor
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        PostCommentModel comment = commentList.get(position);
        holder.textComment.setText(comment.getContent());

        if (comment.getUser() != null) {
            holder.textName.setText(comment.getUser().getUserName());
            Glide.with(context)
                    .load(comment.getUser().getImagePath())
                    .placeholder(R.drawable.avatar)
                    .into(holder.imageAvatar);
        } else {
            loadUserData(comment.getUserId(), holder);
        }

        String currentUserId = getUserIdFromSharedPreferences();
        boolean isLiked = isCommentLiked(comment.getCommentId(), currentUserId);
        updateLikeButton(holder.imageHeart, isLiked);

        // Kiểm tra xem userId của bài đăng có trùng khớp với userId hiện tại hay không
        // Nếu có thì hiển thị nút more, ngược lại ẩn đi

        if (comment != null && comment.getUserId() != null && currentUserId != null && comment.getUserId().equals(currentUserId)) {
            holder.buttonMore.setVisibility(View.VISIBLE);
        } else {
            holder.buttonMore.setVisibility(View.GONE);
            // Hiển thị thông báo khi không có quyền truy cập vào chức năng "more"
        }



        holder.imageHeart.setOnClickListener(v -> {
            if (isLiked) {
                // Unlike the comment and update SharedPreferences
                unlikeComment(comment.getCommentId(), currentUserId, holder.textLikes, holder);
            } else {
                // Like the comment and update SharedPreferences
                likeComment(comment.getCommentId(), currentUserId, holder.textLikes, holder);
            }
        });

        // Thêm xử lý sự kiện khi người dùng chọn nút "more"
        holder.buttonMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.buttonMore);
            popupMenu.getMenuInflater().inflate(R.menu.comment_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete_comment) {
                    // Gọi phương thức để xử lý xóa bình luận
                    deleteComment(comment.getCommentId(), position); // Gọi phương thức deleteComment khi nhấn Delete
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void deleteComment(String commentId, int position) {
        String userId = getUserIdFromSharedPreferences(); // Lấy userId từ SharedPreferences
        String accessToken = getAccessTokenFromSharedPreferences(); // Lấy accessToken từ SharedPreferences

        // Lấy postId từ activity hoặc fragment chứa RecyclerView
        String postId = getPostIdFromActivity();

        // Gọi service từ Retrofit để thực hiện yêu cầu xóa comment
        PostCommentApiService service = ApiServiceProvider.getPostCommentApiService();
        Call<ResponseModel> call = service.deleteComment(accessToken, commentId, userId, postId);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Xóa comment khỏi danh sách
                    commentList.remove(position);
                    // Thông báo cho RecyclerView biết là có dữ liệu thay đổi
                    notifyItemRemoved(position);
                    // Hiển thị thông báo thành công
                    Toast.makeText(context, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi xóa không thành công
                    // Hiển thị thông báo lỗi
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Xử lý khi gặp lỗi
                // Hiển thị thông báo lỗi
            }
        });
    }



    public void updateCommentList(List<PostCommentModel> newCommentList) {
        this.commentList = newCommentList;
        notifyDataSetChanged();
    }

    // Phương thức này cần được thay thế bằng cách lấy postId từ activity hoặc fragment chứa RecyclerView
    private String getPostIdFromActivity() {
        // Lấy postId từ activity hoặc fragment chứa RecyclerView
        // Ví dụ: return ((commentActivity) context).getPostId();
        return postId;
    }


    private String getUserIdFromSharedPreferences() {
        return sharedPreferences.getString("userId", "");
    }

    private boolean isCommentLiked(String commentId, String userId) {
        Set<String> likedComments = sharedPreferences.getStringSet(commentId, new HashSet<>());
        return likedComments.contains(userId);
    }

    private void likeComment(String commentId, String userId, TextView likesTextView, CommentViewHolder holder) {
        Set<String> likedComments = sharedPreferences.getStringSet(commentId, new HashSet<>());
        likedComments.add(userId);

        // Save the updated set back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(commentId, likedComments);
        editor.apply();

        // Update the UI to reflect the new like count
        holder.likeCount = likedComments.size();
        likesTextView.setText(String.valueOf(holder.likeCount));
        updateLikeButton(holder.imageHeart, true);
    }

    private void unlikeComment(String commentId, String userId, TextView likesTextView, CommentViewHolder holder) {
        Set<String> likedComments = sharedPreferences.getStringSet(commentId, new HashSet<>());
        likedComments.remove(userId);

        // Save the updated set back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(commentId, likedComments);
        editor.apply();

        // Update the UI to reflect the new like count
        holder.likeCount = likedComments.size();
        likesTextView.setText(String.valueOf(holder.likeCount));
        updateLikeButton(holder.imageHeart, false);
    }

    private void loadUserData(String userId, CommentViewHolder holder) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        String token = getAccessTokenFromSharedPreferences();
        Call<UserModel> call = service.getUserBasic(token, userId);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel user = response.body();
                    commentList.get(holder.getAdapterPosition()).setUser(user);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    Log.e("API Error", "Failed to load user data");
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("API Failure", "Error loading user data", t);
            }
        });
    }

    private String getAccessTokenFromSharedPreferences() {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return "Bearer " + prefs.getString("accessToken", "");
    }

    private void updateLikeButton(ImageView likeImageView, boolean isLiked) {
        if (isLiked) {
            likeImageView.setImageResource(R.drawable.love);
        } else {
            likeImageView.setImageResource(R.drawable.heart);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar, imageHeart, buttonMore; // Sửa khai báo của buttonMore từ Button sang ImageView
        TextView textName, textComment, textLikes;
        int likeCount = 0;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
            imageHeart = itemView.findViewById(R.id.imageHeart);
            textName = itemView.findViewById(R.id.textName);
            textComment = itemView.findViewById(R.id.textComment);
            textLikes = itemView.findViewById(R.id.textLikes);
            buttonMore = itemView.findViewById(R.id.buttonMore); // Ánh xạ buttonMore
        }
    }
}