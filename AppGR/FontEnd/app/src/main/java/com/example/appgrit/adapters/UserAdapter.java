package com.example.appgrit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> userList;

    public UserAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel user = userList.get(position);

        String userName = user.getUserName();
        if (userName != null && !userName.isEmpty()) {
            holder.fullname.setText(userName);

        } else {
            holder.fullname.setText("Unknown");
        }

        String imagePath = user.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imagePath)
                    .placeholder(R.drawable.profile)
                    .into(holder.imageProfile);
        } else {
            holder.imageProfile.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname;
        public CircleImageView imageProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            imageProfile = itemView.findViewById(R.id.image_profile);
        }
    }
    public void setData(List<UserModel> newData) {
        userList.clear();
        userList.addAll(newData);
        notifyDataSetChanged();
    }
}