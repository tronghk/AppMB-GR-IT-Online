package com.example.appchatit.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.appchatit.activities.InfoGroupActivity;
import com.example.appchatit.models.ChatModel;
import com.example.appchatit.models.DetailsChatModel;
import com.example.appchatit.models.GroupChatModel;
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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userModelList;
    private List<UserModel> listMessOrtherUser = new ArrayList<>();
    private List<GroupMemberModel> memberList = new ArrayList<>();

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
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
            chat_item_layout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                UserModel user = userModelList.get(position);
                boolean isUser = checkIsUser(user);
                startDetailsChatActivity(isUser, user);
                animateBackgroundColorChange();
            }
        }

        private boolean checkIsUser(UserModel user) {
            for (UserModel model : listMessOrtherUser) {
                if (model.getUserName().equals(user.getUserName())) {
                    return true;
                }
            }
            return false;
        }

        private void startDetailsChatActivity(boolean isUser, UserModel user) {
            Intent intent = new Intent(mContext, DetailsChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isUser", isUser);
            bundle.putString("chatId", user.getUserId());
            bundle.putString("userName", user.getUserName());
            bundle.putString("imagePath", user.getImagePath());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }

        private void animateBackgroundColorChange() {
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


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int DELETE_CHAT_ID = 1;
            final int DELETE_GROUP_ID = 2;
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                UserModel user = userModelList.get(position);
                boolean isUser = checkIsUser(user);
                if (isUser) {
                    menu.add(Menu.NONE, DELETE_CHAT_ID, Menu.NONE, "Delete chat").setOnMenuItemClickListener(this);
                } else {
                    menu.add(Menu.NONE, DELETE_GROUP_ID, Menu.NONE, "Delete group").setOnMenuItemClickListener(this);
                }
                getMemberList(user.getUserId());
            }
        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            String otherUserId = null;
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                UserModel user = userModelList.get(position);
                for (UserModel model : listMessOrtherUser) {
                    if (model.getUserName().equals(user.getUserName())) {
                        otherUserId = model.getUserId();
                        break;
                    }
                }
                switch (item.getItemId()) {
                    case 1:
                        deleteChat(user.getUserId(), otherUserId, position);
                        return true;
                    case 2:
                        deleteGroup(user.getUserId(), user.getUserName(), position);
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        }

        private void deleteChat(String chatId, String otherUserId, int position) {
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            String userId = prefs.getString("userId", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            ChatModel chatModel = new ChatModel();
            chatModel.setMessId(chatId);
            chatModel.setUserId(userId);
            chatModel.setUserOrtherId(otherUserId);

            Call<ResponseModel> call = service.deleteChat("Bearer " + token, chatModel);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        userModelList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(mContext, "Delete chat successfully", Toast.LENGTH_SHORT).show();
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

        private void deleteGroup(String chatId, String userName, int position) {
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            String userId = prefs.getString("userId", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            GroupChatModel groupChatModel = new GroupChatModel();
            groupChatModel.setGroupId(chatId);
            groupChatModel.setGroupName(userName);

            for (GroupMemberModel model : memberList) {
                if (model.getUserId().equals(userId) && model.getRole().equals("GR_MANAGER")) {
                    Call<ResponseModel> call = service.deleteGroupChat("Bearer " + token, groupChatModel);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful()) {
                                userModelList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(mContext, "Delete group successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(mContext, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(mContext, "You aren't admin group", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

        private void getMemberList(String chatId) {
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            String userId = prefs.getString("userId", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            if (chatId != null) {
                Call<List<GroupMemberModel>> call = service.getListMemberGroup("Bearer " + token, chatId);
                call.enqueue(new Callback<List<GroupMemberModel>>() {
                    @Override
                    public void onResponse(Call<List<GroupMemberModel>> call, Response<List<GroupMemberModel>> response) {
                        if (response.isSuccessful()) {
                            memberList = response.body();
                        } else {
                            Toast.makeText(mContext, "Failed to fetch member list: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GroupMemberModel>> call, Throwable t) {
                        Toast.makeText(mContext, "Error fetching member list: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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