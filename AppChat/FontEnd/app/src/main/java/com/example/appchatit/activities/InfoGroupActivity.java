package com.example.appchatit.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.models.GroupChatModel;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.ResponseModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoGroupActivity extends AppCompatActivity {
    private String chatId;
    private String userName;
    private String imagePath;
    private ImageView imgGroup;
    private TextView nameGroup;
    private ConstraintLayout edit_group_item;
    private ConstraintLayout manage_role_item;
    private ConstraintLayout admin_role_item;
    private ConstraintLayout out_group_item;
    private List<GroupMemberModel> memberList;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        initializeViews();
        setupEventListeners();
        loadInfoGroup();
        getMemberList();
    }

    private void initializeViews() {
        imgGroup = findViewById(R.id.img_info_gr);
        nameGroup = findViewById(R.id.txt_name_gr);
        edit_group_item = findViewById(R.id.btn_edit_group);
        manage_role_item = findViewById(R.id.btn_edit_role);
        admin_role_item = findViewById(R.id.btn_change_admin);
        out_group_item = findViewById(R.id.btn_leave_group);
        btnBack = findViewById(R.id.btn_back_info_gr);
    }

    private void setupEventListeners() {
        edit_group_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoGroupActivity.this, CreateGroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEdit", true);
                bundle.putString("chatId", chatId);
                bundle.putString("userName", userName);
                bundle.putString("imagePath", imagePath);
                bundle.putSerializable("memberList", (Serializable) memberList);
                intent.putExtras(bundle);
                startActivity(intent);

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = animation.getAnimatedFraction();
                        int alpha = (int) (255 * (1 - progress));
                        int color = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
                        edit_group_item.setBackgroundColor(color);
                    }
                });
                animator.start();
            }
        });

        manage_role_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<GroupMemberModel> roleComparator = new Comparator<GroupMemberModel>() {
                    @Override
                    public int compare(GroupMemberModel member1, GroupMemberModel member2) {
                        if ("GR_MANAGER".equals(member1.getRole())) {
                            return -1;
                        } else if ("GR_MANAGER".equals(member2.getRole())) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                };
                Collections.sort(memberList, roleComparator);

                Intent intent = new Intent(InfoGroupActivity.this, ManageRoleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatId", chatId);
                bundle.putString("userName", userName);
                bundle.putString("imagePath", imagePath);
                bundle.putSerializable("memberList", (Serializable) memberList);
                intent.putExtras(bundle);
                startActivity(intent);

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = animation.getAnimatedFraction();
                        int alpha = (int) (255 * (1 - progress));
                        int color = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
                        manage_role_item.setBackgroundColor(color);
                    }
                });
                animator.start();
            }
        });

        admin_role_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoGroupActivity.this, AdminRoleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatId", chatId);
                bundle.putString("userName", userName);
                bundle.putString("imagePath", imagePath);
                bundle.putSerializable("memberList", (Serializable) memberList);
                intent.putExtras(bundle);
                startActivity(intent);

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = animation.getAnimatedFraction();
                        int alpha = (int) (255 * (1 - progress));
                        int color = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
                        admin_role_item.setBackgroundColor(color);
                    }
                });
                animator.start();
            }
        });

        out_group_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup();

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = animation.getAnimatedFraction();
                        int alpha = (int) (255 * (1 - progress));
                        int color = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
                        out_group_item.setBackgroundColor(color);
                    }
                });
                animator.start();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void leaveGroup() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();

        Call<ResponseModel> call = service.outGroup("Bearer " + token, chatId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(InfoGroupActivity.this, ChatActivity.class);
                    startActivity(intent);
                    Toast.makeText(InfoGroupActivity.this, "Leave group successfully", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            JSONObject errorObj = new JSONObject(response.errorBody().string());
                            String errorMessage = errorObj.optString("message", "Unknown error occurred");
                            if ("Please change role before out group".equals(errorMessage)) {
                                Toast.makeText(InfoGroupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(InfoGroupActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
//                            Toast.makeText(InfoGroupActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                        Toast.makeText(InfoGroupActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Toast.makeText(InfoGroupActivity.this, "Error fetching group: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadInfoGroup() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            chatId = bundle.getString("chatId", "");
            userName = bundle.getString("userName", "");
            imagePath = bundle.getString("imagePath", "");
        }

        nameGroup.setText(userName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CircleCrop());
        Glide.with(this).load(imagePath).apply(requestOptions).into(imgGroup);
    }

    private void getMemberList() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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
//                        Toast.makeText(InfoGroupActivity.this, "Get member list successfully", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(InfoGroupActivity.this, "Failed to fetch member list: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<GroupMemberModel>> call, Throwable t) {
//                    Toast.makeText(InfoGroupActivity.this, "Error fetching member list: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}