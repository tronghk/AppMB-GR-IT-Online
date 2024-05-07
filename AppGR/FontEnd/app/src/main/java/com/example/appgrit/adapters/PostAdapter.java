package com.example.appgrit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgrit.ProfileOther;
import com.example.appgrit.activities.EditPostActivity;
import com.example.appgrit.activities.ImageDetailActivity;
import com.example.appgrit.models.ImagePostModel;
import com.example.appgrit.models.PostIconsModel;
import com.example.appgrit.models.SharePostModel;
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
import com.example.appgrit.network.PostApiService;
import com.example.appgrit.network.UserApiService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static Context context;
    private List<PostModel> postList;
    private PostApiService service;



    public PostAdapter(Context context, List<PostModel> postList) {
        this.context = context;
        this.postList = postList;
        this.service = ApiServiceProvider.getPostApiService();
    }

    public int GetSize(){
        return postList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view,postList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel post = postList.get(position);
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        boolean isLiked = prefs.getBoolean(userId + "_" + post.getPostId() + "_liked", false);
        int likesCount = prefs.getInt(post.getPostId() + "_likes", 0);

        holder.isLiked = isLiked; // Cập nhật trạng thái like vào ViewHolder
        holder.likeCount = likesCount; // Cập nhật số lượng like vào ViewHolder

        holder.updateLikeButton(); // Cập nhật giao diện nút like
        holder.likesTextView.setText(likesCount + " likes");

        holder.txtContent.setText(post.getContent());
        holder.txtTime.setText(post.getPostTimeString());
        //countLikes(post.getPostId(), holder.likesTextView, holder);
        loadUserData(post.getUserId(), holder);
        // Cập nhật trạng thái của nút "more"
        // Kiểm tra xem userId của bài đăng có trùng khớp với userId hiện tại hay không
        if (post.getUserId().equals(userId)) {
            holder.moreImageView.setVisibility(View.VISIBLE);
        } else {
            holder.moreImageView.setVisibility(View.GONE);
        }
        holder.moreImageView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.moreImageView);
            popup.getMenuInflater().inflate(R.menu.post_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_edit_post) {
                    // Lấy postId từ PostModel tại vị trí position
                    String postId = post.getPostId();
                    String content = post.getContent();
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (ImagePostModel image : post.getImagePost()) {
                        imageUrls.add(image.getImagePath());
                    }

                    // Tạo Intent và truyền postId, content, và imageUrls sang EditPostActivity
                    Intent intent = new Intent(context, EditPostActivity.class);
                    intent.putExtra("postId", postId);
                    intent.putExtra("content", content);
                    intent.putStringArrayListExtra("imageUrls", imageUrls);
                    context.startActivity(intent);
                    return true;
                } else if (itemId == R.id.menu_delete_post) {
                    // Lấy postId từ PostModel tại vị trí position
                    String postId = post.getPostId();
                    // Gọi phương thức xóa bài đăng, truyền position
                    deletePost(postId, position);
                    return true;
                } else {
                    return false;
                }
            });
            popup.show();
        });



        // Kiểm tra xem danh sách ảnh có nhiều hơn một ảnh hay không
        if (post.getImagePost() != null && post.getImagePost().size() == 1) {
            // Đặt ảnh vào ImageView post_image
            ImagePostModel imagePost = post.getImagePost().get(0);
            Glide.with(holder.imageView.getContext())
                    .load(imagePost.getImagePath())
                    .centerCrop() // Hiển thị hình ảnh đúng kích thước của ảnh gốc
                    .into(holder.imageView);
            // Ẩn RecyclerView imageGallery
            holder.imageGallery.setVisibility(View.GONE);

            // Thêm sự kiện lắng nghe cho hình ảnh
            holder.imageView.setOnClickListener(v -> {
                // Xem chi tiết hình ảnh khi bấm vào
                Intent intent = new Intent(context, ImageDetailActivity.class);
                intent.putExtra("imagePath", imagePost.getImagePath());
                context.startActivity(intent);
            });
        } else {
            // Nếu có nhiều hơn một ảnh, hiển thị chúng trong RecyclerView imageGallery
            holder.imageGallery.setVisibility(View.VISIBLE);
            List<String> imageUrls = new ArrayList<>();
            for (ImagePostModel imagePost : post.getImagePost()) {
                imageUrls.add(imagePost.getImagePath());
            }
            ImageGalleryAdapter galleryAdapter = new ImageGalleryAdapter(context, imageUrls);
            holder.imageGallery.setAdapter(galleryAdapter);

            // Bắt sự kiện khi người dùng bấm vào một hình ảnh trong ImageGalleryAdapter
            galleryAdapter.setOnImageClickListener(clickedPosition -> {
                // Xem chi tiết hình ảnh khi bấm vào
                Intent intent = new Intent(context, ImageDetailActivity.class);
                intent.putExtra("imagePath", imageUrls.get(clickedPosition));
                context.startActivity(intent);
            });


        }
        // Thiết lập sự kiện lắng nghe cho các phần tử
        holder.likesTextView.setOnClickListener(v -> {
            // Chuyển sang trang ListUserLike.java
//            context.startActivity(new Intent(context, ListUserLike.class));
            String postId = post.getPostId();
            Intent intent = new Intent(context, ListUserLike.class);
            intent.putExtra("postId", postId);
            context.startActivity(intent);
        });

        holder.commentsTextView.setOnClickListener(v -> {
            // Lấy postId từ PostModel tại vị trí position
            String postId = post.getPostId();

            // Tạo Intent và truyền postId sang comment.java
            Intent intent = new Intent(context, comment.class);
            intent.putExtra("postId", postId);
            context.startActivity(intent);
        });
        holder.sendImageView.setOnClickListener(v -> {
            if (!userId.isEmpty()) {
                SharePostModel sharePostModel = new SharePostModel();
                sharePostModel.setUserId(userId);
                sharePostModel.setPostId(post.getPostId());
                sharePostModel.setTimeShare(new Date()); // Set thời gian chia sẻ

                // Gọi phương thức để chia sẻ bài đăng
                sharePost(sharePostModel, post);
            } else {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });
        holder.addCommentTextView.setOnClickListener(v -> {
            // Lấy postId từ PostModel tại vị trí position
            String postId = post.getPostId();

            // Tạo Intent và truyền postId sang comment.java
            Intent intent = new Intent(context, comment.class);
            intent.putExtra("postId", postId);
            context.startActivity(intent);
        });

        holder.commentImageView.setOnClickListener(v -> {
            // Lấy postId từ PostModel tại vị trí position
            String postId = post.getPostId();

            // Tạo Intent và truyền postId sang comment.java
            Intent intent = new Intent(context, comment.class);
            intent.putExtra("postId", postId);
            context.startActivity(intent);
        });

        holder.likeImageView.setOnClickListener(v -> {
            if (!userId.isEmpty()) {
                ExpressionModel expressionModel = new ExpressionModel();
                expressionModel.setUserId(userId);
                expressionModel.setPostId(post.getPostId());
                expressionModel.setType("1");

                holder.isLiked = !holder.isLiked; // Toggle the state
                if (holder.isLiked) {
                    addExpression(expressionModel, holder.likesTextView, holder);
                } else {
                    deleteExpression(expressionModel, holder.likesTextView, holder);
                }
                holder.updateLikeButton(); // Reflect the new state in the UI
            } else {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Thêm sự kiện lắng nghe cho hình ảnh người dùng
        holder.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy userId của người dùng từ danh sách bài đăng
                String userId = postList.get(holder.getAdapterPosition()).getUserId();

                // Tạo Intent để chuyển sang ProfileOther và truyền userId
                Intent intent = new Intent(context, ProfileOther.class);
                intent.putExtra("selectedUserId", userId);
                context.startActivity(intent);
            }
        });

        // Thêm sự kiện lắng nghe cho tên người dùng
        holder.usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy userId của người dùng từ danh sách bài đăng
                String userId = postList.get(holder.getAdapterPosition()).getUserId();

                // Tạo Intent để chuyển sang ProfileOther và truyền userId
                Intent intent = new Intent(context, ProfileOther.class);
                intent.putExtra("selectedUserId", userId);
                context.startActivity(intent);
            }
        });
    }

    private void deletePost(String postId, int position) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");
        String token = "Bearer " + getAccessTokenFromSharedPreferences();
        Call<ResponseModel> call = service.deletePost(token, postId, userId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    // Xóa bài đăng thành công, cập nhật giao diện hoặc thông báo cho người dùng
                    // Ví dụ: gọi phương thức removeItemAt(position) để xóa bài đăng khỏi danh sách
                    postList.remove(position);
                    Toast.makeText(context, "Delete Post successfully", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged(); // Cập nhật RecyclerView
                } else {
                    // Xóa bài đăng không thành công, hiển thị thông báo lỗi cho người dùng
                    Toast.makeText(context, "Failed to delete post: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Xử lý khi gửi yêu cầu không thành công
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void sharePost(SharePostModel sharePostModel, PostModel post) {
        String token = "Bearer " + getAccessTokenFromSharedPreferences(); // Lấy access token từ SharedPreferences
        Call<ResponseModel> call = service.sharePost(token, sharePostModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi chia sẻ bài đăng thành công
                    Toast.makeText(context, "Post shared successfully", Toast.LENGTH_SHORT).show();

                } else {
                    // Xử lý khi chia sẻ bài đăng không thành công
                    Toast.makeText(context, "Failed to share post: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Xử lý khi gửi yêu cầu không thành công
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void addExpression(ExpressionModel expressionModel, TextView likeCountView, ViewHolder holder) {
        // Update local data immediately
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(expressionModel.getUserId() + "_" + expressionModel.getPostId() + "_liked", true);
        int currentLikes = prefs.getInt(expressionModel.getPostId() + "_likes", 0);
        editor.putInt(expressionModel.getPostId() + "_likes", currentLikes + 1);
        editor.apply();

        // Update UI immediately
        holder.likeCount = currentLikes + 1;
        holder.isLiked = true;
        holder.updateLikeButton();
        likeCountView.setText((currentLikes + 1) + " likes");

        // Send API request
        ExpressionApiService service = ApiServiceProvider.getExpressionApiService();
        String token = "Bearer " + getAccessTokenFromSharedPreferences();
        Call<ResponseModel> call = service.addExpression(token, expressionModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (!response.isSuccessful()) {
                    // Handle API request failure
                    // You may need to revert local data changes here
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Handle network failure
                // You may need to revert local data changes here
            }
        });
    }



    private void deleteExpression(ExpressionModel expressionModel, TextView likeCountView, ViewHolder holder) {
        // Update local data immediately
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int currentLikes = prefs.getInt(expressionModel.getPostId() + "_likes", 0);
        int updatedLikesCount = Math.max(0, currentLikes - 1); // Prevent negative values
        editor.putBoolean(expressionModel.getUserId() + "_" + expressionModel.getPostId() + "_liked", false);
        editor.putInt(expressionModel.getPostId() + "_likes", updatedLikesCount);
        editor.apply();

        // Update UI immediately
        holder.likeCount = updatedLikesCount;
        holder.isLiked = false;
        holder.updateLikeButton();
        likeCountView.setText(updatedLikesCount + " likes");

        // Send API request
        ExpressionApiService service = ApiServiceProvider.getExpressionApiService();
        String token = "Bearer " + getAccessTokenFromSharedPreferences();
        Call<ResponseModel> call = service.deleteExpression(token, expressionModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (!response.isSuccessful()) {
                    // Handle API request failure
                    // You may need to revert local data changes here
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Handle network failure
                // You may need to revert local data changes here
            }
        });
    }




    private void countLikes(String postId, TextView likeCountView, ViewHolder holder) {
        // Retrieve the access token from SharedPreferences
        String token = getAccessTokenFromSharedPreferences();

        // Call the API to get the expressions for the post
        Call<List<PostIconsModel>> call = ApiServiceProvider.getPostApiService().getPostExpressions(token, postId);
        call.enqueue(new Callback<List<PostIconsModel>>() {
            @Override
            public void onResponse(Call<List<PostIconsModel>> call, Response<List<PostIconsModel>> response) {
                if (response.isSuccessful()) {
                    // Filter the expressions to count only 'likes'
                    List<PostIconsModel> expressions = response.body();
                    int likeCount = 0;
                    for (PostIconsModel expression : expressions) {
                        if (expression.getExpression() == 0) { // Assuming '1' stands for 'like'
                            likeCount++;
                        }
                    }

                    // Update SharedPreferences with the latest count
                    SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(postId + "_likes", likeCount);
                    editor.apply();

                    // Update the UI with the new like count
                    likeCountView.setText(likeCount + " likes");
                    holder.likeCount = likeCount; // Update the like count in the holder if needed
                } else {
                    Toast.makeText(context, "Failed to fetch like data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostIconsModel>> call, Throwable t) {
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
        RecyclerView imageGallery;
        int likeCount = 0; // Thêm biến likeCount ở mức lớp
        private List<PostModel> postList; // Thêm trường postList vào ViewHolder
        public ViewHolder(@NonNull View itemView,List<PostModel> postList) {
            super(itemView);
            PostModel post = new PostModel();
            this.postList = postList;
            imageView = itemView.findViewById(R.id.post_image);
            likeImageView = itemView.findViewById(R.id.like);
            commentImageView = itemView.findViewById(R.id.comment);
            sendImageView = itemView.findViewById(R.id.send);
//            saveImageView = itemView.findViewById(R.id.save);
            moreImageView = itemView.findViewById(R.id.more);
            txtContent = itemView.findViewById(R.id.description);
            txtTime = itemView.findViewById(R.id.date_post);
            likesTextView = itemView.findViewById(R.id.likes);
            commentsTextView = itemView.findViewById(R.id.comments);
            addCommentTextView = itemView.findViewById(R.id.add_comment);
            profileImageView = itemView.findViewById(R.id.image_profile);
            usernameTextView = itemView.findViewById(R.id.username);
            imageGallery = itemView.findViewById(R.id.imageGallery);

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

//        private void toggleLikeState() {
//            PostModel post = new PostModel();
//            isLiked = !isLiked;
//            updateLikeButton();
//
//            // Lưu trạng thái mới và số lượng like vào SharedPreferences
//            saveLikeStatusAndCount(post.getPostId(), isLiked, likeCount);
//        }
        // Phương thức lưu trạng thái và số lượng like vào SharedPreferences
//        private void saveLikeStatusAndCount(String postId, boolean isLiked, int likeCount) {
//            SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("isLiked_" + postId, isLiked);
//            editor.putInt("likeCount_" + postId, likeCount);
//            editor.apply();
//        }

        private void updateLikeButton() {
            if (isLiked) {
                likeImageView.setImageResource(R.drawable.love);
            } else {
                likeImageView.setImageResource(R.drawable.heart_home);
            }
        }
    }


    public void setData(List<PostModel> newData) {
        postList.clear();
        postList.addAll(newData);
        notifyDataSetChanged();
    }
}

