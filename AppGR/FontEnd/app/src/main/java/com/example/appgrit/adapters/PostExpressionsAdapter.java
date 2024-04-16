package com.example.appgrit.adapters;

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

import java.util.List;

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
