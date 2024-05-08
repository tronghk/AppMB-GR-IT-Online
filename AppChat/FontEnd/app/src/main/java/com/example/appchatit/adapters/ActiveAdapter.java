package com.example.appchatit.adapters;

import android.content.Context;
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
import com.example.appchatit.models.UserModel;

import java.util.List;

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userList;

    public ActiveAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_active_item, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgUser;
        public TextView nameUser;
        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.profile_image_active);
            nameUser = itemView.findViewById(R.id.txt_active_fr);
            mContext = itemView.getContext();
        }
    }
}