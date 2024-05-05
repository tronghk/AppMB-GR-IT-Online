package com.example.appgrit.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.models.PostIconsModel;
import com.example.appgrit.models.UserFollowsModel;
import com.example.appgrit.network.ApiServiceProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostExpressionsAdapter extends RecyclerView.Adapter<PostExpressionsAdapter.ViewHolder> {
    private List<PostIconsModel> postExpressions;

    public PostExpressionsAdapter(List<PostIconsModel> postExpressions) {
        this.postExpressions = postExpressions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_like_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostIconsModel userLike = postExpressions.get(position);
        holder.userName.setText(userLike.getUserName());
        Glide.with(holder.itemView.getContext())
                .load(userLike.getUserImage())
                .placeholder(R.drawable.profile)
                .error(R.drawable.avartar_user)
                .into(holder.profileImage);

        checkFollowStatus(holder, userLike.getUserId());

        holder.followButton.setOnClickListener(v -> {
            if (holder.followButton.getText().toString().equals("Theo dõi")) {
                followUser(holder, userLike.getUserId());
            } else {
                unfollowUser(holder, userLike.getUserId());
            }
        });
    }

    private void checkFollowStatus(ViewHolder holder, String userId) {
        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = "Bearer " + prefs.getString("accessToken", "");
        String currentUserId = prefs.getString("userId", "");
        Log.d(currentUserId, "userFI: " + currentUserId);

        Call<List<UserFollowsModel>> call = ApiServiceProvider.getUserApiService().getUserFollow(token, currentUserId);
        call.enqueue(new Callback<List<UserFollowsModel>>() {
            @Override
            public void onResponse(Call<List<UserFollowsModel>> call, Response<List<UserFollowsModel>> response) {
                if (response.isSuccessful()) {
                    List<UserFollowsModel> followList = response.body();
                    for (UserFollowsModel followModel : followList) {
                        if (followModel.getUserId().equals(userId)) {
                            holder.followButton.setText("Đang theo dõi");
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserFollowsModel>> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void followUser(ViewHolder holder, String userId) {
        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = "Bearer " + prefs.getString("accessToken", "");
        String currentUserId = prefs.getString("userId", "");

        UserFollowsModel followModel = new UserFollowsModel(userId, currentUserId);
        Call<Void> call = ApiServiceProvider.getUserApiService().addFollow(token, followModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    holder.followButton.setText("Đang theo dõi");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void unfollowUser(ViewHolder holder, String userId) {
        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = "Bearer " + prefs.getString("accessToken", "");
        String currentUserId = prefs.getString("userId", "");

        UserFollowsModel followModel = new UserFollowsModel(userId, currentUserId);
        Call<Void> call = ApiServiceProvider.getUserApiService().deleteFollow(token, followModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    holder.followButton.setText("Theo dõi");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    @Override
    public int getItemCount() {
        return postExpressions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView userName;
        private Button followButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.user_name);
            followButton = itemView.findViewById(R.id.button_follow);
        }

        public void bind(PostIconsModel postExpression) {
            // Load profile image using Glide or Picasso
             Glide.with(itemView.getContext()).load(postExpression.getUserImage()).into(profileImage);

            userName.setText(postExpression.getUserName());
            // Set click listener for follow button if needed
        }
    }
}
