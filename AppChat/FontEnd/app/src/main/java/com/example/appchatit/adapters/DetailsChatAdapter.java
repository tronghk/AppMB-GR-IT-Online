package com.example.appchatit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.activities.ChatActivity;
import com.example.appchatit.models.DetailsChatModel;
import com.example.appchatit.models.ResponseModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String userId;
    private String imagePath;
    private Context context;
    public static List<DetailsChatModel> detailsChatModelList;

    public DetailsChatAdapter(Context context, List<DetailsChatModel> list, String userId, String imagePath) {
        this.context = context;
        this.detailsChatModelList = list;
        this.userId = userId;
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.message_left_item, parent, false);
            return new ViewHolder1(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_right_item, parent, false);
            return new ViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DetailsChatModel list = detailsChatModelList.get(position);
        String message = list.getContent();
        String status = list.getStatus();

        if (holder instanceof ViewHolder1) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            if (message != null && !message.isEmpty()) {
                if (status != null && status.equals("recall")) {
                    viewHolder1.messageLeft.setText("Message recalled");
                } else {
                    viewHolder1.messageLeft.setText(message);
                }
                if (imagePath != null && !imagePath.isEmpty() && imagePath.startsWith("https://firebasestorage.googleapis.com/")) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transforms(new CircleCrop());
                    Glide.with(((ViewHolder1) holder).imgUser.getContext())
                            .load(imagePath)
                            .apply(requestOptions)
                            .into(((ViewHolder1) holder).imgUser);
                } else {
                    ((ViewHolder1) holder).imgUser.setImageResource(R.drawable.baseline_api_24);
                }
            } else {
                viewHolder1.messageLeft.setText("Error");
            }
        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            if (message != null && !message.isEmpty()) {
                if (status != null && status.equals("recall")) {
                    viewHolder2.messageRight.setText("Message recalled");
                } else {
                    viewHolder2.messageRight.setText(message);
                }
            } else {
                viewHolder2.messageRight.setText("Error");
            }
        }
    }

    @Override
    public int getItemCount() {
        return detailsChatModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (detailsChatModelList.get(position).getUserId().equals(userId)) {
            return 1;
        } else {
            return 0;
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public ImageView imgUser;
        public TextView messageLeft;
        private Context mContext;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.avt_item_boxchat);
            messageLeft = itemView.findViewById(R.id.msg_left_boxchat);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            messageLeft.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            //
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int DELETE_MESSAGE_ID = 1;
            menu.add(Menu.NONE, DELETE_MESSAGE_ID, Menu.NONE, "Delete message").setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case 1:
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DetailsChatModel messageModel = detailsChatModelList.get(position);
                        deleteMessage(messageModel);
                    }
                    return true;
                default:
                    return false;
            }
        }

        private void deleteMessage(DetailsChatModel messageModel) {
            SharedPreferences prefs = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();

            Call<ResponseModel> call = service.deleteMessage("Bearer " + token, messageModel);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        messageLeft.setText("Message recalled");
                        Toast.makeText(mContext, "Recall message successfully", Toast.LENGTH_SHORT).show();
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
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView messageRight;
        private Context mContext;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            messageRight = itemView.findViewById(R.id.msg_right_boxchat);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            messageRight.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            //
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int DELETE_MESSAGE_ID = 1;
            menu.add(Menu.NONE, DELETE_MESSAGE_ID, Menu.NONE, "Delete message").setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case 1:
                    deleteMessage();
                    return true;
                default:
                    return false;
            }
        }

        private void deleteMessage() {
        }
    }

    public void setData(List<DetailsChatModel> newData) {
        detailsChatModelList.clear();
        detailsChatModelList.addAll(newData);
        notifyDataSetChanged();
    }
}