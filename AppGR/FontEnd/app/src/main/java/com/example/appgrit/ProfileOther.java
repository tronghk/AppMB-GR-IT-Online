package com.example.appgrit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgrit.adapters.PostAdapter;
import com.example.appgrit.helper.JWTServices;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserFriendsModel;
import com.example.appgrit.models.UserModel;
import com.example.appgrit.network.ApiServiceProvider;
import com.example.appgrit.network.PostApiService;
import com.example.appgrit.network.UserApiService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileOther extends AppCompatActivity {

    private ImageView back;
    private CircleImageView image_profile;
    private TextView posts;
    private TextView follower;
    private TextView following;
    private TextView fullname;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private Button addFriendButton;
    private List<PostModel> postList = new ArrayList<>();
    private TokenModel tokenModel;

    private SharedPreferences prefs;

    boolean isFriendRequestSent = false; // Chỉ cần đoán trạng thái ban đầu, bạn có thể thay bằng phương thức để kiểm tra trạng thái thực tế


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other);

        // Khởi tạo các thành phần giao diện
        back = findViewById(R.id.back);
        image_profile = findViewById(R.id.image_profile);
        posts = findViewById(R.id.posts);
        follower = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        fullname = findViewById(R.id.fullname);
        recyclerViewPosts = findViewById(R.id.recycler_view);
        addFriendButton = findViewById(R.id.add_friend_button);

        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Lấy dữ liệu người dùng đã chọn
        Intent intent = getIntent();
        String selectedUserId = intent.getStringExtra("selectedUserId");
        // Kiểm tra trạng thái kết bạn
//        checkFriendshipStatus(selectedUserId);
        // Kiểm tra trạng thái gửi lời mời
        updateFriendshipButtonState(selectedUserId);

        // Lấy thông tin và bài đăng của người dùng đã chọn
        getUserInfo(selectedUserId);
        getUserPosts(selectedUserId);
        getCountFollowers(selectedUserId);
        getCountFollowing(selectedUserId);
        // Xử lý sự kiện nút quay lại
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFriendRequestSent) {
                    // Gọi API để hủy lời mời kết bạn
                    deleteFriendRequest(selectedUserId);
                } else {
                    // Gọi API để gửi lời mời kết bạn
                    sendFriendRequest(selectedUserId);
                }
            }
        });
    }

//    private void checkFriendshipStatus(String selectedUserId) {
//        String userId = prefs.getString("userId", "");
//        String token = prefs.getString("accessToken", "");
//        UserApiService service = ApiServiceProvider.getUserApiService();
//
//        Call<List<UserFriendsModel>> call = service.getListUserFriend(userId);
//        call.enqueue(new Callback<List<UserFriendsModel>>() {
//            @Override
//            public void onResponse(Call<List<UserFriendsModel>> call, Response<List<UserFriendsModel>> response) {
//                if (response.isSuccessful()) {
//                    List<UserFriendsModel> friendList = response.body();
//                    // Kiểm tra xem selectedUserId có trong danh sách bạn bè không
//                    for (UserFriendsModel friend : friendList) {
//                        if (friend.getUserFriendId().equals(selectedUserId)) {
//                            // Nếu có, cập nhật trạng thái là bạn bè và giao diện
//                            isFriendRequestSent = true;
//                            addFriendButton.setText("Bạn Bè");
//                            return;
//                        }
//                    }
//                    // Nếu không tìm thấy, giữ nguyên trạng thái là chưa kết bạn
//                    isFriendRequestSent = false;
//                    addFriendButton.setText("Thêm Bạn Bè");
//                } else {
//                    Toast.makeText(getApplicationContext(), "Không thể tải danh sách bạn bè", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<UserFriendsModel>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("API_Error", "Error: ", t);
//            }
//        });
//    }

    private void updateFriendshipButtonState(String selectedUserId) {
        String userId = prefs.getString("userId", "");
        String token = prefs.getString("accessToken", "");
        UserApiService service = ApiServiceProvider.getUserApiService();

        Call<List<UserFriendsModel>> call = service.getListUserFriend(userId);
        call.enqueue(new Callback<List<UserFriendsModel>>() {
            @Override
            public void onResponse(Call<List<UserFriendsModel>> call, Response<List<UserFriendsModel>> response) {
                if (response.isSuccessful()) {
                    List<UserFriendsModel> friendList = response.body();
                    boolean isRequestSent = prefs.getBoolean("FRIEND_REQUEST_SENT_" + selectedUserId, false);

                    for (UserFriendsModel friend : friendList) {
                        if (friend.getUserFriendId().equals(selectedUserId)) {
                            isFriendRequestSent = true;
                            addFriendButton.setText("Bạn Bè");
                            return;
                        }
                    }

                    if (isRequestSent) {
                        isFriendRequestSent = true;
                        addFriendButton.setText("Đã Gửi Lời Mời");
                    } else {
                        isFriendRequestSent = false;
                        addFriendButton.setText("Thêm Bạn Bè");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không thể tải danh sách bạn bè", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserFriendsModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error: ", t);
            }
        });
    }


    // Phương thức để gửi lời mời kết bạn
    private void sendFriendRequest(String friendId) {
        String userId = prefs.getString("userId", "");
        String token = prefs.getString("accessToken", ""); // Lấy token từ SharedPreferences
        UserApiService service = ApiServiceProvider.getUserApiService();

        // Thêm token vào tiêu đề "Authorization"
        UserFriendsModel model = new UserFriendsModel(userId, friendId, "False"); // Status ban đầu là "false"
        Call<ResponseBody> call = service.addFriend("Bearer " + token, model);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đã gửi lời mời kết bạn", Toast.LENGTH_SHORT).show();

                    // Lưu trạng thái gửi lời mời vào SharedPreferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("FRIEND_REQUEST_SENT_" + friendId, true);
                    editor.apply();

                    // Cập nhật nút và thông báo
                    isFriendRequestSent = true;
                    addFriendButton.setText("Đã Gửi Lời Mời");

                } else {
                    Toast.makeText(getApplicationContext(), "Gửi lời mời kết bạn không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error: ", t);
            }
        });
    }


    // Phương thức để hủy lời mời kết bạn
    private void deleteFriendRequest(String friendId) {
        String userId = prefs.getString("userId", "");
        String token = prefs.getString("accessToken", ""); // Lấy token từ SharedPreferences
        UserApiService service = ApiServiceProvider.getUserApiService();

        // Tạo một đối tượng UserFriendsModel với Status được đặt thành "False"
        UserFriendsModel model = new UserFriendsModel(userId, friendId, "False");

        // Gọi phương thức deleteFriend với đối tượng model
        Call<ResponseBody> call = service.deleteFriend("Bearer " + token, model);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đã hủy lời mời kết bạn", Toast.LENGTH_SHORT).show();
                    // Sau khi hủy thành công, xóa trạng thái:
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("FRIEND_REQUEST_SENT_" + friendId);
                    editor.apply();

                    // Cập nhật nút và thông báo
                    isFriendRequestSent = false;
                    addFriendButton.setText("Thêm Bạn Bè");
                } else {
                    Toast.makeText(getApplicationContext(), "Hủy lời mời kết bạn không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error: ", t);
            }
        });
    }




    // Phương thức để lấy thông tin người dùng từ API
    private void getUserInfo(String userId) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<UserModel> call = service.getUserInfo(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel user = response.body();
                    fullname.setText(user.getUserName());
                    Picasso.get().load(user.getImagePath()).into(image_profile);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching user info: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching user info: ", t);
            }
        });
    }

    // Phương thức để lấy bài đăng của người dùng từ API
    private void getUserPosts(String userId) {
        PostApiService service = ApiServiceProvider.getPostApiService();
        Call<List<PostModel>> call = service.getPostSelfUser(userId);
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    List<PostModel> list = response.body();
                    postAdapter = new PostAdapter(ProfileOther.this, list);
                    recyclerViewPosts.setLayoutManager(new LinearLayoutManager(ProfileOther.this));
                    recyclerViewPosts.setAdapter(postAdapter);
                    posts.setText(String.valueOf(list.size()));
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching user posts: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching user posts: ", t);
            }
        });
    }

    // Phương thức để lấy số người theo dõi của người dùng từ API
    private void getCountFollowers(String userId) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<ResponseBody> call = service.CountFollowers(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        JSONObject json = new JSONObject(s);
                        String count = json.get("count").toString();
                        follower.setText(count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching follower count: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching follower count: ", t);
            }
        });
    }

    // Phương thức để lấy số người đang theo dõi của người dùng từ API
    private void getCountFollowing(String userId) {
        UserApiService service = ApiServiceProvider.getUserApiService();
        Call<ResponseBody> call = service.CountUserFollowers(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        JSONObject json = new JSONObject(s);
                        String count = json.get("count").toString();
                        following.setText(count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching following count: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_Error", "Error fetching following count: ", t);
            }
        });
    }
}
