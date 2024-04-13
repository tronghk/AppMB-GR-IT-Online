package com.example.appgrit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.appgrit.R;
import com.example.appgrit.activities.ListUserLike;
import com.example.appgrit.comment;
import com.example.appgrit.models.ExpressionModel;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.ResponseModel;
import com.example.appgrit.network.ExpressionApiService;
import com.example.appgrit.network.UserApiService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static Context context;
    private List<PostModel> postList;

    boolean isLiked = false; // Thêm biến isLiked


    public PostAdapter(Context context, List<PostModel> postList) {
        this.context = context;
        this.postList = postList;
    }
    public int GetSize(){
        return postList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel post = postList.get(position);
        holder.txtContent.setText(post.getContent());
        holder.txtTime.setText(post.getPostTimeString());
        // Load số lượng like
        loadLikesCount(post.getPostId(), holder);
        // Load ảnh bài viết nếu có
        if (post.getImagePost() != null && !post.getImagePost().isEmpty()) {
            String imageUrl = post.getImagePost().get(0).getImagePath();
            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.avartar_user))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Xử lý khi load ảnh thất bại
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Xử lý khi load ảnh thành công
                            return false;
                        }
                    })
                    .into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        // Load thông tin người dùng
        loadUserData(post.getUserId(), holder);

        // Thiết lập sự kiện lắng nghe cho các phần tử
        holder.likesTextView.setOnClickListener(v -> {
            // Chuyển sang trang ListUserLike.java
            context.startActivity(new Intent(context, ListUserLike.class));
        });

        holder.commentsTextView.setOnClickListener(v -> {
            // Lấy postId từ PostModel tại vị trí position
            String postId = post.getPostId();

            // Tạo Intent và truyền postId sang comment.java
            Intent intent = new Intent(context, comment.class);
            intent.putExtra("postId", postId);
            context.startActivity(intent);
        });


        holder.addCommentTextView.setOnClickListener(v -> {
            // Hiển thị trang comment.java
            context.startActivity(new Intent(context, comment.class));
        });

        holder.commentImageView.setOnClickListener(v -> {
            // Hiển thị trang comment.java
            context.startActivity(new Intent(context, comment.class));
        });

        holder.likeImageView.setOnClickListener(v -> {
            SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userId = prefs.getString("userId", "");
            if (!userId.isEmpty()) {
                ExpressionModel expressionModel = new ExpressionModel();
                expressionModel.setUserId(userId);
                expressionModel.setPostId(post.getPostId());
                expressionModel.setType("1"); // Assuming '1' is the type for like

                if (holder.isLiked) {
                    // Call API to delete the like
                    deleteExpression(expressionModel);
                    holder.toggleLikeState();  // Update UI to reflect the unlike
                } else {
                    // Call API to add the like
                    addExpression(expressionModel);
                    holder.toggleLikeState();  // Update UI to reflect the like
                }
            } else {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLikesCount(String postId, ViewHolder holder) {
        ExpressionApiService service = ApiServiceProvider.getExpressionApiService();
        Call<String> call = service.countExpressionInPost(postId, "like");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String likeCount = response.body();
                    holder.likesTextView.setText(likeCount);
                } else {
                    Log.e("API Error", "Failed to load likes count: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Network Error", "Error loading likes count: " + t.getMessage());
            }
        });
    }

    private void loadUserData(String userId, ViewHolder holder) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        String token = getAccessTokenFromSharedPreferences();
        Call<UserModel> call = service.getUserBasic(token, userId);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel user = response.body();
                    Log.d("User Data", "Username: " + user.getUserName() + ", Image URL: " + user.getImagePath());
                    holder.usernameTextView.setText(user.getUserName());
                    Glide.with(context)
                            .load(user.getImagePath())
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.add)
                            .into(holder.profileImageView);
                } else {
                    Log.e("API Error", "Failed to load user data: " + response.message());
                    Toast.makeText(context, "Failed to load user data: " + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Network Error", "Error loading user data: " + t.getMessage());
            }
        });
    }


    // Phương thức gọi API thêm biểu cảm (like)
    private void addExpression(ExpressionModel expressionModel) {
        // Gán giá trị cho trường "Type"
        expressionModel.setType("1"); // hoặc expressionModel.setType(1);

        // Gọi API để thực hiện hành động like
        ExpressionApiService service = ApiServiceProvider.getExpressionApiService();
        String token = "Bearer " + getAccessTokenFromSharedPreferences(); // Lấy access token từ SharedPreferences
        Call<ResponseModel> call = service.addExpression(token, expressionModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi hành động like thành công
                    // Cập nhật giao diện người dùng hoặc thực hiện các tác vụ khác
                } else {
                    // Xử lý khi hành động like không thành công
                    Toast.makeText(context, "Failed to like post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteExpression(ExpressionModel expressionModel) {
        ExpressionApiService service = ApiServiceProvider.getExpressionApiService();
        String token = "Bearer " + getAccessTokenFromSharedPreferences();
        Call<ResponseModel> call = service.deleteExpression(token, expressionModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    Toast.makeText(context, "Like removed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to remove like", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức lấy access token từ SharedPreferences
    private String getAccessTokenFromSharedPreferences() {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getString("accessToken", "");
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, likeImageView, commentImageView, sendImageView, saveImageView, moreImageView;
        TextView txtContent, txtTime, likesTextView, commentsTextView, addCommentTextView;
        boolean isLiked = false; // Khai báo biến isLiked ở mức lớp
        CircleImageView profileImageView;
        TextView usernameTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.post_image);
            likeImageView = itemView.findViewById(R.id.like);
            commentImageView = itemView.findViewById(R.id.comment);
            sendImageView = itemView.findViewById(R.id.send);
            saveImageView = itemView.findViewById(R.id.save);
            moreImageView = itemView.findViewById(R.id.more); // Thêm nút more
            txtContent = itemView.findViewById(R.id.description);
            txtTime = itemView.findViewById(R.id.date_post);
            likesTextView = itemView.findViewById(R.id.likes);
            commentsTextView = itemView.findViewById(R.id.comments);
            addCommentTextView = itemView.findViewById(R.id.add_comment);
            profileImageView = itemView.findViewById(R.id.image_profile);
            usernameTextView = itemView.findViewById(R.id.username);

            moreImageView.setOnClickListener(v -> {
                // Hiển thị menu edit post và delete post
                PopupMenu popup = new PopupMenu(context, moreImageView);
                popup.getMenuInflater().inflate(R.menu.post_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.menu_edit_post) {
                            // Xử lý sự kiện edit post
                            return true;
                        } else if (itemId == R.id.menu_delete_post) {
                            // Xử lý sự kiện delete post
                            return true;
                        } else {
                            return false;
                        }
                    }

                });
                popup.show();
            });
        }

        public void toggleLikeState() {
            // Chuyển đổi trạng thái like
            isLiked = !isLiked;
            likeImageView.setImageResource(isLiked ? R.drawable.love : R.drawable.heart_home);
        }

    }


    public void setData(List<PostModel> newData) {
        postList.clear();
        postList.addAll(newData);
        notifyDataSetChanged();
    }
}
