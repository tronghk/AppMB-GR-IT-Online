package com.example.appgrit.adapters;

import android.content.Context;
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
import com.example.appgrit.models.PostCommentModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<PostCommentModel> commentList;
    private Context context;

    public CommentAdapter(Context context, List<PostCommentModel> commentList) {
        this.context = context;
        this.commentList = commentList;
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
    }
    private String getAccessTokenFromSharedPreferences() {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return "Bearer " + prefs.getString("accessToken", "");
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
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar, imageHeart;
        TextView textName, textComment, textLikes;
        Button buttonReply;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
            imageHeart = itemView.findViewById(R.id.imageHeart);
            textName = itemView.findViewById(R.id.textName);
            textComment = itemView.findViewById(R.id.textComment);
            textLikes = itemView.findViewById(R.id.textLikes);
            buttonReply = itemView.findViewById(R.id.buttonReply);
        }
    }
}

