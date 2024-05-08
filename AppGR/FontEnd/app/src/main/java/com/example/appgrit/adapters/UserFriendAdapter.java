package com.example.appgrit.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appgrit.ProfileOther;
import com.example.appgrit.R;
import com.example.appgrit.UserActivity;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.UserFriendsModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.example.appgrit.network.UserApiService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFriendAdapter extends RecyclerView.Adapter<UserFriendAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> userList;
    private String userId;
    private String token;

    public UserFriendAdapter(Context context, List<UserModel> userList, String userId, String token) {
        this.context = context;
        this.userList = userList;
        this.token = token;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userfriends, parent, false);
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

        // Thêm sự kiện lắng nghe cho tên người dùng
        holder.fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy userId của người dùng từ danh sách
                String userId = userList.get(holder.getAdapterPosition()).getUserId();

                // Tạo Intent để chuyển sang ProfileOther và truyền userId
                Intent intent = new Intent(context, ProfileOther.class);
                intent.putExtra("selectedUserId", userId);
                context.startActivity(intent);
            }
        });

        // Thêm sự kiện lắng nghe cho hình ảnh người dùng
        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy userId của người dùng từ danh sách
                String userIdOrther = userList.get(holder.getAdapterPosition()).getUserId();

                // Tạo Intent để chuyển sang ProfileOther và truyền userId
                Intent intent = new Intent(context, ProfileOther.class);
                intent.putExtra("selectedUserId", userIdOrther);
                context.startActivity(intent);
            }
        });
        holder.btn_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIdOrther = userList.get(holder.getAdapterPosition()).getUserId();

                UserFriendsModel us = new UserFriendsModel(userId,userIdOrther,"True");


                PostApiService service = ApiServiceProvider.getPostApiService();
                Call<UserFriendsModel> call = service.UpdateFriend("Bearer " + token,us);
                call.enqueue(new Callback<UserFriendsModel>() {
                    @Override
                    public void onResponse(Call<UserFriendsModel> call, Response<UserFriendsModel> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context.getApplicationContext(),"Thêm bạn thành công",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, UserActivity.class);
                            intent.putExtra("selectedUserId", userId);
                            context.startActivity(intent);
                        }else {
                            Log.e("tokenTest",response.message());
                            Log.e("userId",userId);
                            Log.e("userIdOrther",userIdOrther);
                            Toast.makeText(context.getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserFriendsModel> call, Throwable t) {

                    }
                });
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userIdOrther = userList.get(holder.getAdapterPosition()).getUserId();

                UserFriendsModel us = new UserFriendsModel(userId,userIdOrther,"True");


                PostApiService service = ApiServiceProvider.getPostApiService();
                Call<ResponseBody> call = service.DeleteFriend("Bearer " + token,us);
               call.enqueue(new Callback<ResponseBody>() {
                   @Override
                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       if(response.isSuccessful()){
                           Toast.makeText(context.getApplicationContext(),"Thêm bạn thành công",Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(context, UserActivity.class);
                           intent.putExtra("selectedUserId", userId);
                           context.startActivity(intent);
                       }else {

                           Toast.makeText(context.getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<ResponseBody> call, Throwable t) {

                   }
               });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname;
        public CircleImageView imageProfile;

        public Button btn_allow;
        public Button btn_delete;
        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            imageProfile = itemView.findViewById(R.id.image_profile);
            btn_allow = itemView.findViewById(R.id.allow);
            btn_delete = itemView.findViewById(R.id.delete);
        }
    }
    public void setData(List<UserModel> newData) {
        userList.clear();
        userList.addAll(newData);
        notifyDataSetChanged();
    }
}