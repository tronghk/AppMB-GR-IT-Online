package com.example.appchatit.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.adapters.CreateChatAdapter;
import com.example.appchatit.adapters.CreateGroupAdapter;
import com.example.appchatit.models.ChatModel;
import com.example.appchatit.models.DetailsChatModel;
import com.example.appchatit.models.GroupChatModel;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.UserFriendModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.network.ApiServiceProvider;
import com.example.appchatit.services.ChatApiService;
import com.example.appchatit.services.OnMemberListChangeListener;
import com.example.appchatit.services.UserApiService;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupActivity extends AppCompatActivity implements OnMemberListChangeListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private CreateGroupAdapter createGroupAdapter;
    private RecyclerView recyclerView;
    private List<UserModel> userList = new ArrayList<>();
    private ImageView btnCreateGroup;
    private ImageView imgGroup;
    private EditText nameGroup;
    private String imgGroupUrl;
    private List<GroupMemberModel> listMember = new ArrayList<>();
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initializeViews();

        if (checkEditGroup()) {
            setupEditGroup();
        } else {
            setupEventListeners();
        }
        setupRecyclerView();
    }

    private void initializeViews() {
        btnCreateGroup = findViewById(R.id.btn_create_group);
        imgGroup = findViewById(R.id.img_group);
        nameGroup = findViewById(R.id.edt_name_group);
        parentLayout = findViewById(R.id.layoutMain);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupEventListeners() {
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });

        imgGroup.setOnClickListener(v -> openFileChooser());

        nameGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameGroup.setText("");
                }
            }
        });

        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (nameGroup.hasFocus()) {
                        nameGroup.clearFocus();
                        nameGroup.setText("Name group");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(nameGroup.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycle_add_member);
        createGroupAdapter = new CreateGroupAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(createGroupAdapter);
        loadListFriend();
    }

    private boolean checkEditGroup() {
        boolean isEdit = false;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            isEdit = bundle.getBoolean("isEdit");
        }
        return isEdit;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupEditGroup() {
        String userName = null;
        String imagePath = null;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            userName = bundle.getString("userName", "");
            imagePath = bundle.getString("imagePath", "");
        }
        nameGroup.setText(userName);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CircleCrop());
        Glide.with(this).load(imagePath).apply(requestOptions).into(imgGroup);

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGroup();
                Toast.makeText(CreateGroupActivity.this, "Press button edit", Toast.LENGTH_SHORT).show();
            }
        });

        imgGroup.setOnClickListener(v -> openFileChooser());

        nameGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameGroup.setText(nameGroup.getText().toString().trim());
                }
            }
        });

        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (nameGroup.hasFocus()) {
                        nameGroup.clearFocus();
                        nameGroup.setText(nameGroup.getText().toString().trim());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(nameGroup.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imgGroup.setImageBitmap(bitmap);

                MultipartBody.Part part;
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    byte[] buffer = new byte[inputStream.available()];
                    inputStream.read(buffer);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transforms(new CircleCrop());
                    Glide.with(CreateGroupActivity.this).load(selectedImageUri).apply(requestOptions).into(imgGroup);
                    Toast.makeText(CreateGroupActivity.this, "Read image successful", Toast.LENGTH_SHORT).show();

                    RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImageUri)), buffer);
                    part = MultipartBody.Part.createFormData("image", "file.jpg", requestBody);
                } catch (Exception e) {
                    Toast.makeText(CreateGroupActivity.this, "Failed to read image data", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String token = prefs.getString("accessToken", "");
                UserApiService service = ApiServiceProvider.getUserApiService();
                Call<List<String>> call = service.uploadImage(part);
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                            imgGroupUrl = response.body().get(0);
                            Toast.makeText(CreateGroupActivity.this, "OK " + response.message(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateGroupActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Toast.makeText(CreateGroupActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("API_Error", "Error fetching users: ", t);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadListFriend() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<List<UserFriendModel>> call = userApiService.getListUserFriend(userId);
        call.enqueue(new Callback<List<UserFriendModel>>() {
            @Override
            public void onResponse(Call<List<UserFriendModel>> call, Response<List<UserFriendModel>> response) {
                if (response.isSuccessful()) {
                    List<UserFriendModel> userFriendsList = response.body();
                    if (userFriendsList != null && !userFriendsList.isEmpty()) {
                        for (UserFriendModel userFriend : userFriendsList) {
                            String userFriendId = userFriend.getUserFriendId();
                            getUserInfo(userFriendId);
                        }
                    } else {
                        Toast.makeText(CreateGroupActivity.this, "Không có bạn bè", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateGroupActivity.this, "Lỗi khi lấy danh sách bạn bè", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserFriendModel>> call, Throwable t) {
                Toast.makeText(CreateGroupActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo(String userFriendId) {
        UserApiService userApiService = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = userApiService.getUserBasic(userFriendId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    if (user != null) {
                        userList.add(user);
                        createGroupAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CreateGroupActivity.this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateGroupActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(CreateGroupActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createGroup() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", "");
        String userId = sharedPreferences.getString("userId", "");

        String nameGr = nameGroup.getText().toString().trim();

        Toast.makeText(CreateGroupActivity.this, "list " + listMember.size(), Toast.LENGTH_SHORT).show();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String timeString = sdf.format(new Date());

        if (listMember.size() >= 2) {
            if (imgGroupUrl != null) {
                if (!nameGr.isEmpty()) {
                    GroupMemberModel admin = new GroupMemberModel(null, userId, "GR_MANAGER");
                    listMember.add(admin);

                    GroupChatModel newGroup = new GroupChatModel();
                    newGroup.setImagePath(imgGroupUrl);
                    newGroup.setGroupName(nameGr);
                    newGroup.setTimeCreate(timeString);
                    newGroup.setGroupMembers(listMember);
                    Toast.makeText(CreateGroupActivity.this, "Created", Toast.LENGTH_SHORT).show();

                    ChatApiService service = ApiServiceProvider.getChatApiService();
                    Call<GroupChatModel> call = service.createGroupChat("Bearer " + token, newGroup);
                    call.enqueue(new Callback<GroupChatModel>() {
                        @Override
                        public void onResponse(Call<GroupChatModel> call, Response<GroupChatModel> response) {
                            if (response.isSuccessful()) {
                                GroupChatModel newGroup = response.body();
                                String newGroupId = newGroup.getGroupId();
                                String newGroupName = newGroup.getGroupName();
                                String newGroupImagePath = newGroup.getImagePath();
                                startDetailsChatActivity(newGroupId, newGroupName, newGroupImagePath);
                            } else {
                                Toast.makeText(CreateGroupActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GroupChatModel> call, Throwable t) {
                            Toast.makeText(CreateGroupActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateGroupActivity.this, "Please enter group name", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CreateGroupActivity.this, "Please choose image group", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CreateGroupActivity.this, "Please choose 2 member to create new group", Toast.LENGTH_SHORT).show();
        }
    }

    public void editGroup() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", "");
        String userId = sharedPreferences.getString("userId", "");
        String groupId = null;

        String nameGr = nameGroup.getText().toString().trim();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String chatId = bundle.getString("chatId", "");
            String imagePath = bundle.getString("imagePath", "");
            imgGroupUrl = imagePath;
            groupId = chatId;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String timeString = sdf.format(new Date());


        if (listMember.size() >= 2) {
            if (imgGroupUrl != null) {
                if (!nameGr.isEmpty()) {
                    boolean isAdminExist = false;

                    for (GroupMemberModel member : listMember) {
                        if (member.getUserId().equals(userId) && "GR_MANAGER".equals(member.getRole())) {
                            isAdminExist = true;
                            break;
                        }
                    }
                    if (!isAdminExist) {
                        GroupMemberModel admin = new GroupMemberModel(null, userId, "GR_MANAGER");
                        listMember.add(admin);
                    }
                    Toast.makeText(CreateGroupActivity.this, "Memberlist " + listMember.size(), Toast.LENGTH_SHORT).show();

                    GroupChatModel newGroup = new GroupChatModel();
                    newGroup.setGroupId(groupId);
                    newGroup.setImagePath(imgGroupUrl);
                    newGroup.setGroupName(nameGr);
                    newGroup.setTimeCreate(timeString);
                    newGroup.setGroupMembers(listMember);
                    Toast.makeText(CreateGroupActivity.this, "Edited", Toast.LENGTH_SHORT).show();

                    ChatApiService service = ApiServiceProvider.getChatApiService();
                    Call<GroupChatModel> call = service.updateGroupChat("Bearer " + token, newGroup);
                    call.enqueue(new Callback<GroupChatModel>() {
                        @Override
                        public void onResponse(Call<GroupChatModel> call, Response<GroupChatModel> response) {
                            if (response.isSuccessful()) {
                                GroupChatModel newGroup = response.body();
                                String newGroupId = newGroup.getGroupId();
                                String newGroupName = newGroup.getGroupName();
                                String newGroupImagePath = newGroup.getImagePath();
                                startInfoGroupActivity(newGroupId, newGroupName, newGroupImagePath);
                            } else {
                                Toast.makeText(CreateGroupActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GroupChatModel> call, Throwable t) {
                            Toast.makeText(CreateGroupActivity.this, "Error fetching users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateGroupActivity.this, "Please enter group name", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CreateGroupActivity.this, "Please choose image group", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CreateGroupActivity.this, "Please choose least 2 members to create new group", Toast.LENGTH_SHORT).show();
        }
    }

    private void startDetailsChatActivity(String newGroupId, String userNnewGroupNameame, String newGroupImagePath) {
        Intent intent = new Intent(this, DetailsChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatId", newGroupId);
        bundle.putString("userName", userNnewGroupNameame);
        bundle.putString("imagePath", newGroupImagePath);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private void startInfoGroupActivity(String newGroupId, String userNnewGroupNameame, String newGroupImagePath) {
        Intent intent = new Intent(this, InfoGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatId", newGroupId);
        bundle.putString("userName", userNnewGroupNameame);
        bundle.putString("imagePath", newGroupImagePath);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override
    public void onMemberListChange(List<GroupMemberModel> memberList) {
        this.listMember = memberList;
        Toast.makeText(CreateGroupActivity.this, memberList.size() + " member(s)", Toast.LENGTH_SHORT).show();
    }
}