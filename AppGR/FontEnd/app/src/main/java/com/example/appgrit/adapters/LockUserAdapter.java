package com.example.appgrit.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appgrit.R;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockUserAdapter extends RecyclerView.Adapter<LockUserAdapter.ViewHolder> {

    private Context context;
    private String token;
    private List<UserModel> userList;

    public LockUserAdapter(Context context, List<UserModel> userList, String token) {
        this.context = context;
        this.userList = userList;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lock_user, parent, false);
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

        holder.unLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventUnlock(user.getUserId());
            }
        });
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
    public void EventUnlock(String userId){



                PostApiService service = ApiServiceProvider.getPostApiService();
                Call<ResponseBody> call = service.UnlockUser("Bearer " + token,userId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context.getApplicationContext(), "User Is Unlock",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname;
        public Button unLock;
        public CircleImageView imageProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            unLock= itemView.findViewById(R.id.unlock);
            imageProfile = itemView.findViewById(R.id.image_profile);
        }
    }
    public void setData(List<UserModel> newData) {
        userList.clear();
        userList.addAll(newData);
        notifyDataSetChanged();
    }
}