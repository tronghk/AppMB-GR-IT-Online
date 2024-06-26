package com.example.appchatit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.adapters.DetailsChatAdapter;
import com.example.appchatit.models.DetailsChatModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.ViewTreeObserver;

public class DetailsChatActivity extends AppCompatActivity {
    private String userId;
    private boolean isUser;
    private String chatId;
    private String userName;
    private String imagePath;
    private DetailsChatAdapter detailsChatAdapter;
    private RecyclerView recyclerView;
    private List<DetailsChatModel> detailsChatModelList = new ArrayList<>();
    private ImageView btnSend;
    private EditText edtContent;
    private RecyclerView boxChat;
    private ImageView btnInfo;
    private ImageView btnBack;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxchat);
        initializeViews();
        setupEventListeners();
        setupRecyclerView();
    }

    private void initializeViews(){
        btnInfo = findViewById(R.id.btn_info_chat);
        btnSend = findViewById(R.id.btn_send);
        btnBack = findViewById(R.id.btn_back_boxchat);
        edtContent = findViewById(R.id.edt_message);
        boxChat = findViewById(R.id.box_chat);
    }

    private void setupEventListeners(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            private boolean isClicked = false;

            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    isClicked = true;
                    createMessage();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isClicked = false;
                        }
                    }, 3000);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtContent.setText("");
                }
            }
        });

        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                boxChat.getWindowVisibleDisplayFrame(r);
                int screenHeight = boxChat.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    recyclerView.smoothScrollToPosition(detailsChatModelList.size());
                }
            }
        };
        boxChat.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsChatActivity.this, InfoGroupActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("chatId", chatId);
                bundle.putString("userName", userName);
                bundle.putString("imagePath", imagePath);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        startLoadingMessagesPeriodically();
    }

    private void setupRecyclerView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            imagePath = bundle.getString("imagePath", "");
        }

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "");

        recyclerView = findViewById(R.id.box_chat);
        detailsChatAdapter = new DetailsChatAdapter(this, detailsChatModelList, userId, imagePath);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(detailsChatAdapter);
        loadListMessage();
    }

    private void createMessage() {
        String message = edtContent.getText().toString().trim();
        if (!message.isEmpty()) {
            DetailsChatModel detailsChatModel = new DetailsChatModel();
            detailsChatModel.setChatId(chatId);
            detailsChatModel.setUserId(userId);
            detailsChatModel.setContent(message);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            String timeString = sdf.format(new Date());
            detailsChatModel.setTime(timeString);

            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");
            ChatApiService service = ApiServiceProvider.getChatApiService();
            Call<DetailsChatModel> call = service.createMessage("Bearer " + token, detailsChatModel);
            call.enqueue(new Callback<DetailsChatModel>() {
                @Override
                public void onResponse(Call<DetailsChatModel> call, Response<DetailsChatModel> response) {
                    if (response.isSuccessful()) {
                        loadListMessage();
                        edtContent.setText("");
                    } else {
//                        Toast.makeText(DetailsChatActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DetailsChatModel> call, Throwable t) {
//                    Toast.makeText(DetailsChatActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_Error", "Error fetching users: ", t);
                }
            });
        } else {
            Toast.makeText(DetailsChatActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadListMessage() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            isUser = bundle.getBoolean("isUser");
            chatId = bundle.getString("chatId", "");
            userName = bundle.getString("userName", "");
            imagePath = bundle.getString("imagePath", "");
        }

        if (isUser) {
            btnInfo.setVisibility(View.GONE);
        }

        TextView txtUserName = findViewById(R.id.txt_name);
        txtUserName.setText(userName);

        ImageView imgUser = findViewById(R.id.avatar_item);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CircleCrop());
        Glide.with(this).load(imagePath).apply(requestOptions).into(imgUser);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();
        Call<List<DetailsChatModel>> call = service.getListDetailsChat("Bearer " + token, chatId);

        call.enqueue(new Callback<List<DetailsChatModel>>() {
            @Override
            public void onResponse(Call<List<DetailsChatModel>> call, Response<List<DetailsChatModel>> response) {
                if (response.isSuccessful()) {
                    detailsChatModelList.clear();
                    detailsChatModelList = response.body();
                    Collections.sort(detailsChatModelList, new Comparator<DetailsChatModel>() {
                        @Override
                        public int compare(DetailsChatModel detailChat1, DetailsChatModel detailChat2) {
                            return detailChat1.getTime().compareTo(detailChat2.getTime());
                        }
                    });
                    detailsChatAdapter.setData(detailsChatModelList);
                    recyclerView.smoothScrollToPosition(detailsChatModelList.size());
                    startLoadingMessagesPeriodically();
                } else {
//                    Toast.makeText(DetailsChatActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetailsChatModel>> call, Throwable t) {
//                Toast.makeText(DetailsChatActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching users: ", t);
            }
        });
    }

    private void startLoadingMessagesPeriodically() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchDataFromApi();
            }
        }, 0, 2000);
    }

    private void fetchDataFromApi() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        ChatApiService service = ApiServiceProvider.getChatApiService();
        Call<List<DetailsChatModel>> call = service.getListDetailsChat("Bearer " + token, chatId);

        call.enqueue(new Callback<List<DetailsChatModel>>() {
            @Override
            public void onResponse(Call<List<DetailsChatModel>> call, Response<List<DetailsChatModel>> response) {
                if (response.isSuccessful()) {
                    detailsChatModelList.clear();
                    detailsChatModelList = response.body();
                    Collections.sort(detailsChatModelList, new Comparator<DetailsChatModel>() {
                        @Override
                        public int compare(DetailsChatModel detailChat1, DetailsChatModel detailChat2) {
                            return detailChat1.getTime().compareTo(detailChat2.getTime());
                        }
                    });
                    detailsChatAdapter.setData(detailsChatModelList);
                    recyclerView.smoothScrollToPosition(detailsChatModelList.size());
                } else {
//                    Toast.makeText(DetailsChatActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetailsChatModel>> call, Throwable t) {
//                Toast.makeText(DetailsChatActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching users: ", t);
            }
        });
    }
}