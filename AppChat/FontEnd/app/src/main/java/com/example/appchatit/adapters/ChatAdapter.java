package com.example.appchatit.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.activities.DetailsChatActivity;
import com.example.appchatit.models.UserModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    public static List<UserModel> userModelList;

    public ChatAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listchat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        UserModel userModel = userModelList.get(position);

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
        return userModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgUser;
        public TextView nameUser;
        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.avt_item);
            nameUser = itemView.findViewById(R.id.name_item);

            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                UserModel post = userModelList.get(position);
                Intent intent = new Intent(mContext, DetailsChatActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("chatId", post.getUserId());
                bundle.putString("userName", post.getUserName());
                bundle.putString("imagePath", post.getImagePath());

                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        }
    }

    public void setData(List<UserModel> newData) {
        userModelList.clear();
        userModelList.addAll(newData);
        notifyDataSetChanged();
    }
}