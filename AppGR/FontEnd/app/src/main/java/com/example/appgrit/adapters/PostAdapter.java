package com.example.appgrit.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.example.appgrit.models.PostModel;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static Context context;
    private List<PostModel> postList;
    boolean isLiked = false; // Thêm biến isLiked


    public PostAdapter(Context context, List<PostModel> postList) {
        this.context = context;
        this.postList = postList;
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

        // Thiết lập sự kiện lắng nghe cho các phần tử
        holder.likesTextView.setOnClickListener(v -> {
            // Chuyển sang trang ListUserLike.java
            context.startActivity(new Intent(context, ListUserLike.class));
        });

        holder.commentsTextView.setOnClickListener(v -> {
            // Hiển thị trang comment.java
            context.startActivity(new Intent(context, comment.class));
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
            // Thực hiện chuyển đổi trạng thái like
            holder.toggleLikeState();
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, likeImageView, commentImageView, sendImageView, saveImageView, moreImageView;
        TextView txtContent, txtTime, likesTextView, commentsTextView, addCommentTextView;
        boolean isLiked = false; // Khai báo biến isLiked ở mức lớp

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
            likeImageView.setImageResource(isLiked ? R.drawable.heart_home : R.drawable.love);
        }
    }


    public void setData(List<PostModel> newData) {
        postList.clear();
        postList.addAll(newData);
        notifyDataSetChanged();
    }
}
