package com.example.appchatit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatit.R;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    public static List<UserModel> userModelList;
    private ChatApiService service;

    public ChatAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
        this.service = ApiServiceProvider.getChatApiService();
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
            holder.txt1.setText(otherUser);
        } else {
            holder.txt1.setText("Unknown");
        }

//        List<ImagePostModel> imageList = postSellProductModel.getImagePosts();
//        String imagePath = imageList.get(0).toString();
//        if (imagePath != null && !imagePath.isEmpty()) {
////            Glide.with(holder.itemView.getContext())
////                    .load(imagePath)
////                    .placeholder(R.drawable.profile)
////                    .into(holder.txt1);
//            ImagePostModel imagePost = postSellProductModel.getImagePosts().get(0);
//            Glide.with(holder.txt1.getContext())
//                    .load(imagePost.getImagePath())
//                    .into(holder.txt1);
//        } else {
//            holder.txt1.setImageResource(R.drawable.profile);
//        }
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt1;
        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.name_item);

            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void setData(List<UserModel> newData) {
        userModelList.clear();
        userModelList.addAll(newData);
        notifyDataSetChanged();
    }
}