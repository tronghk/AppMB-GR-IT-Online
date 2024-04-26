package com.example.appchatit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.activities.DetailsChatActivity;
import com.example.appchatit.models.ChatModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChatAdapter extends RecyclerView.Adapter<CreateChatAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userList;

    public CreateChatAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel userModel = userList.get(position);

        String otherUser = userModel.getUserName();
        if (otherUser != null && !otherUser.isEmpty()) {
            holder.nameUser.setText(otherUser);
        } else {
            holder.nameUser.setText("Unknown");
        }

        String imagePath = userModel.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CircleCrop());
            Glide.with(holder.imgUser.getContext())
                    .load(imagePath)
                    .apply(requestOptions)
                    .into(holder.imgUser);
        } else {
            holder.imgUser.setImageResource(R.drawable.baseline_api_24);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgUser;
        public TextView nameUser;
        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.avt_friend_item);
            nameUser = itemView.findViewById(R.id.name_friend_item);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                UserModel user = userList.get(position);
                createNewChat(user.getUserId(), user.getUserName(), user.getImagePath());
            }
        }

        private void createNewChat(String userOrtherId, String userName, String imagePath) {
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            String userId = prefs.getString("userId", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            ChatModel chatModel = new ChatModel();
            chatModel.setUserId(userId);
            chatModel.setUserOrtherId(userOrtherId);

            Call<ChatModel> call = service.createChat("Bearer " + token, chatModel);
            call.enqueue(new Callback<ChatModel>() {
                @Override
                public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                    if (response.isSuccessful()) {
                        ChatModel chatModel = response.body();
                        String newChatId = chatModel.getMessId();
                        startDetailsChatActivity(newChatId, userName, imagePath);
                    } else {
                        Toast.makeText(mContext, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ChatModel> call, Throwable t) {
                    Toast.makeText(mContext, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_Error", "Error fetching users: ", t);
                }
            });
        }

        private void startDetailsChatActivity(String newChatId, String userName, String imagePath) {
            Intent intent = new Intent(mContext, DetailsChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("chatId", newChatId);
            bundle.putString("userName", userName);
            bundle.putString("imagePath", imagePath);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}