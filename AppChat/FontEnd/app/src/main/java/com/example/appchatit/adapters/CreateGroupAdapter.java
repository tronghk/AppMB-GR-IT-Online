package com.example.appchatit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.services.OnMemberListChangeListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userList;
    private List<GroupMemberModel> memberList;

    public CreateGroupAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        return new ViewHolder(view, (OnMemberListChangeListener) context);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateGroupAdapter.ViewHolder holder, int position) {
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
        public CheckBox checkBox;
        private Context mContext;
        private OnMemberListChangeListener listener;

        public ViewHolder(@NonNull View itemView, OnMemberListChangeListener listener) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.avt_member_item);
            nameUser = itemView.findViewById(R.id.name_member_item);
            checkBox = itemView.findViewById(R.id.checkbox_member_item);
            memberList = new ArrayList<>();

            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            this.listener = listener;
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                UserModel user = userList.get(position);
                GroupMemberModel member = new GroupMemberModel(null, user.getUserId(), "GR_MEMBER");
                if (!checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    memberList.add(member);
                } else {
                    checkBox.setChecked(false);
                    Iterator<GroupMemberModel> iterator = memberList.iterator();
                    while (iterator.hasNext()) {
                        GroupMemberModel existingMember = iterator.next();
                        if (existingMember.getUserId().equals(member.getUserId())) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            if (listener != null) {
                listener.onMemberListChange(memberList);
            }
        }
    }
}