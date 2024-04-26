package com.example.appchatit.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.ResponseModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;
import com.example.appchatit.services.OnMemberListChangeListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userList;
    private List<GroupMemberModel> memberList;
    public String currentGroupId;
    private boolean isEdit;

    public CreateGroupAdapter(Context context, List<UserModel> userList, String chatId, List<GroupMemberModel> memberList) {
        this.context = context;
        this.userList = userList;
        this.currentGroupId = chatId;
        this.isEdit = chatId != null;
        this.memberList = memberList;
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

        boolean isChecked = false;
        UserModel user = userList.get(position);
        for (GroupMemberModel member : memberList) {
            if (user.getUserId().equals(member.getUserId())) {
                isChecked = true;
                break;
            }
        }
        holder.checkBox.setChecked(isChecked);
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
        private ConstraintLayout member_item_layout;

        public ViewHolder(@NonNull View itemView, OnMemberListChangeListener listener) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.avt_member_item);
            nameUser = itemView.findViewById(R.id.name_member_item);
            checkBox = itemView.findViewById(R.id.checkbox_member_item);
            if (!isEdit) {
                memberList = new ArrayList<>();
            }
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            this.listener = listener;
            member_item_layout = itemView.findViewById(R.id.member_layout);
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
                    if (isEdit) {
                        addMemberGroup(member);
                    }
                } else {
                    checkBox.setChecked(false);
                    Iterator<GroupMemberModel> iterator = memberList.iterator();
                    while (iterator.hasNext()) {
                        GroupMemberModel existingMember = iterator.next();
                        if (existingMember.getUserId().equals(member.getUserId())) {
                            iterator.remove();
                            if (isEdit) {
                                deleteMemberGroup(member);
                            }
                            break;
                        }
                    }
                }
                Toast.makeText(mContext, "Member list " + memberList.size(), Toast.LENGTH_SHORT).show();
            }
            if (listener != null) {
                listener.onMemberListChange(memberList);
            }

            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float progress = animation.getAnimatedFraction();
                    int alpha = (int) (255 * (1 - progress));
                    int color = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
                    member_item_layout.setBackgroundColor(color);
                }
            });
            animator.start();
        }

        private void addMemberGroup(GroupMemberModel member) {
            member.setGroupId(currentGroupId);
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            String userId = prefs.getString("userId", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            Call<GroupMemberModel> call = service.addMemberGroupChat("Bearer " + token, member);
            call.enqueue(new Callback<GroupMemberModel>() {
                @Override
                public void onResponse(Call<GroupMemberModel> call, Response<GroupMemberModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(mContext, "Add member", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GroupMemberModel> call, Throwable t) {
                    Toast.makeText(mContext, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void deleteMemberGroup(GroupMemberModel member) {
            member.setGroupId(currentGroupId);
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            String userId = prefs.getString("userId", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            Call<ResponseModel> call = service.deleteMemberGroupChat("Bearer " + token, member);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(mContext, "Delete member", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(mContext, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}