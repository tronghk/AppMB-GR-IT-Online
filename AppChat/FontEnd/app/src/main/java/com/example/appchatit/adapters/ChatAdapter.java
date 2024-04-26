package com.example.appchatit.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.appchatit.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userModelList;
    private List<UserModel> listMessOrtherUser = new ArrayList<>();

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
        if (imagePath != null && !imagePath.isEmpty() && imagePath.startsWith("https://firebasestorage.googleapis.com/")) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgUser;
        public TextView nameUser;
        private Context mContext;
        private ConstraintLayout chat_item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.avt_item);
            nameUser = itemView.findViewById(R.id.name_item);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            chat_item_layout = itemView.findViewById(R.id.listchat_layout);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                boolean isUser = false;
                UserModel user = userModelList.get(position);
                for (UserModel model : listMessOrtherUser){
                    if (model.getUserName().equals(user.getUserName())){
                        isUser = true;
                        break;
                    }
                }

                Intent intent = new Intent(mContext, DetailsChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isUser", isUser);
                bundle.putString("chatId", user.getUserId());
                bundle.putString("userName", user.getUserName());
                bundle.putString("imagePath", user.getImagePath());
                intent.putExtras(bundle);
                mContext.startActivity(intent);

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = animation.getAnimatedFraction();
                        int alpha = (int) (255 * (1 - progress));
                        int color = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
                        chat_item_layout.setBackgroundColor(color);
                    }
                });
                animator.start();
            }
        }
    }

    public void setData(List<UserModel> newData) {
        userModelList.clear();
        userModelList.addAll(newData);
        notifyDataSetChanged();
    }

    public void updateMessOrtherUser(List<UserModel> newData) {
        listMessOrtherUser.clear();
        listMessOrtherUser.addAll(newData);
        notifyDataSetChanged();
    }
}