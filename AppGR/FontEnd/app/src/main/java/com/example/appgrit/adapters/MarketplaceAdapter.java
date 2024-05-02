package com.example.appgrit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.activities.DetailMarketplaceActivity;
import com.example.appgrit.activities.activity_edit_marketplace;
import com.example.appgrit.models.ImagePostModel;
import com.example.appgrit.models.PostSellProductModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.services.MarketplaceApiService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketplaceAdapter extends RecyclerView.Adapter<MarketplaceAdapter.ViewHolder> {
    private Context context;
    public static List<PostSellProductModel> postList;
    private MarketplaceApiService service;

    public MarketplaceAdapter(Context context, List<PostSellProductModel> postList) {
        this.context = context;
        this.postList = postList;
        this.service = ApiServiceProvider.getMarketplaceApiService();
        ;
    }

    @NonNull
    @Override
    public MarketplaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.marketplace_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketplaceAdapter.ViewHolder holder, int position) {
        PostSellProductModel postSellProductModel = postList.get(position);
        // Lấy userId hiện tại từ SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        // Kiểm tra nếu userId của bài đăng trùng khớp với userId hiện tại, thì hiển thị nút "more", ngược lại ẩn nút "more"
        if (postSellProductModel.getUserId().equals(userId)) {
            holder.moreButton.setVisibility(View.VISIBLE);
        } else {
            holder.moreButton.setVisibility(View.GONE);
        }
        String product = postSellProductModel.getProductName();
        if (product != null && !product.isEmpty()) {
            holder.txt3.setText(product);
        } else {
            holder.txt3.setText("Unknown");
        }
    // Lấy giá sản phẩm từ PostSellProductModel và hiển thị lên TextView
        float price = postSellProductModel.getPrice();
        holder.txt2.setText(String.valueOf(price)); // Thiết lập giá trị cho TextView txt2
        List<ImagePostModel> imageList = postSellProductModel.getImagePosts();

        // Kiểm tra nếu danh sách hình ảnh không rỗng
        if (!imageList.isEmpty()) {
            ImagePostModel imagePost = imageList.get(0);
            String imagePath = imagePost.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                Glide.with(holder.txt1.getContext())
                        .load(imagePath)
                        .placeholder(R.drawable.profile)
                        .into(holder.txt1);
            } else {
                holder.txt1.setImageResource(R.drawable.profile);
            }
        } else {
            // Nếu danh sách hình ảnh rỗng, hiển thị hình ảnh mặc định
            holder.txt1.setImageResource(R.drawable.profile);
        }

        // Handle click event for "more" ImageView to show PopupMenu
        // Handle click event for "more" ImageView to show PopupMenu
        holder.moreButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.menu_sell);

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.item_edit) {
                    // Start activity to edit post sell
                    Intent editIntent = new Intent(context, activity_edit_marketplace.class);
                    editIntent.putExtra("postId", postSellProductModel.getPostSellProductId());
                    editIntent.putExtra("productName", postSellProductModel.getProductName());
                    editIntent.putExtra("price", postSellProductModel.getPrice());
                    editIntent.putExtra("description", postSellProductModel.getContent());

                    // Attach imageUrls to Intent
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (ImagePostModel imagePost : postSellProductModel.getImagePosts()) {
                        imageUrls.add(imagePost.getImagePath());
                    }
                    editIntent.putStringArrayListExtra("imageUrls", imageUrls);

                    context.startActivity(editIntent);
                    return true;
                } else if (itemId == R.id.item_delete) {
                    // Handle delete post sell
                    deletePostSell(postSellProductModel);
                    return true;
                } else {
                    return false;
                }
            });

            popupMenu.show();
        });



    }

    private void deletePostSell(PostSellProductModel postSellProductModel) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        String userId = prefs.getString("userId", "");

        service.deleteSellPost("Bearer " + token, postSellProductModel.getPostSellProductId(), userId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Xóa bài đăng khỏi danh sách và cập nhật giao diện
                            postList.remove(postSellProductModel);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Post deleted successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete post: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error deleting post: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView txt1;
        public TextView txt2, txt3, txt4;
        public ImageView moreButton; // Add reference to the "more" button

        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.ivImage);
            txt2 = itemView.findViewById(R.id.tvPrice);
            txt3 = itemView.findViewById(R.id.tvDes);
            txt4 = itemView.findViewById(R.id.tvLocate);
            moreButton = itemView.findViewById(R.id.ivMore); // Initialize the "more" button

            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                PostSellProductModel post = postList.get(position);
                Intent intent = new Intent(mContext, DetailMarketplaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("postSellProductId", post.getPostSellProductId());
                bundle.putString("userId", post.getUserId());
                bundle.putString("content", post.getContent());
                bundle.putString("productName", post.getProductName());
                bundle.putSerializable("imagePosts", (Serializable) post.getImagePosts());

                Date postTime = post.getPostTime();
                String postTimeString = postTime != null ? postTime.toString() : null;
                bundle.putString("postTime", postTimeString);


                bundle.putFloat("price", post.getPrice());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        }
    }

    public void setData(List<PostSellProductModel> newData) {
        postList.clear();
        postList.addAll(newData);
        notifyDataSetChanged();
    }
}