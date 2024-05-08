package com.example.appchatit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.activities.AdminRoleActivity;
import com.example.appchatit.activities.InfoGroupActivity;
import com.example.appchatit.models.ChatModel;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.ResponseModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRoleAdapter extends RecyclerView.Adapter<AdminRoleAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userList;
    private List<GroupMemberModel> memberList;
    public String groupId;
    private String nameGroup;
    private String imgGroup;

    public AdminRoleAdapter(Context context, List<UserModel> userList, List<GroupMemberModel> memberList, String chatId, String nameGroup, String imgGroup) {
        this.context = context;
        this.userList = userList;
        this.memberList = memberList;
        this.groupId = chatId;
        this.nameGroup = nameGroup;
        this.imgGroup = imgGroup;
    }

    @NonNull
    @Override
    public AdminRoleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRoleAdapter.ViewHolder holder, int position) {
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
                updateAdminGroup(user.getUserId());
            }
        }

        private void updateAdminGroup(String memberId) {
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            Call<ResponseModel> call = service.updateRoleAdminGroupMember("Bearer " + token, groupId, memberId);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        startInfoGroupActivity();
                        Toast.makeText(mContext, "Change admin group successfully", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(mContext, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
//                    Toast.makeText(mContext, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void startInfoGroupActivity() {
            Intent intent = new Intent(mContext, InfoGroupActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("chatId", groupId);
            bundle.putString("userName", nameGroup);
            bundle.putString("imagePath", imgGroup);

            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}