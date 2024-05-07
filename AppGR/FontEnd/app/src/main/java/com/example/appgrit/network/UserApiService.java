package com.example.appgrit.network;

import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserFollowsModel;
import com.example.appgrit.models.UserFriendsModel;
import com.example.appgrit.models.UserInforModel;
import com.example.appgrit.models.UserModel;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApiService {
    @POST("signup")
    Call<TokenModel> signUp(@Body SignUpModel signUpModel);

    @POST("signin")
    Call<TokenModel> signIn(@Body SignInModel signInModel);
    @POST("sign-in-google")
    Call<TokenModel> SignInGoogle(@Query("idToken") String idToken);

    @GET("user")
    Call<UserInforModel> getUserInfo(@Header("Authorization") String token, @Query("email") String email);

    @GET("get-post")
    Call<List<PostModel>> getPostUser(@Query("userId") String userId);

    @GET("count-followers")
    Call<ResponseBody> CountFollowers
            (@Query("userId") String userId);
    @GET("/get-user-id")
    Call<UserModel> getUserBasic(@Header("Authorization") String token, @Query("userId") String userId);



    @GET("count-user-followers")
    Call<ResponseBody> CountUserFollowers
            (@Query("userId") String userId);

    @GET("reset-password")
    Call<ResponseBody> resetPassword(@Query("email") String email);

    @GET("get-listUserFriend")
    Call<List<UserFriendsModel>> getListUserFriend(@Query("userId") String userId);

    @POST("add-friend")
    Call<ResponseBody> addFriend(@Header("Authorization") String token, @Body UserFriendsModel model);


    @HTTP(method = "DELETE", path = "/delete-friend", hasBody = true)
    Call<ResponseBody> deleteFriend(@Header("Authorization") String token, @Body UserFriendsModel model);








    @GET("get-user-id")
    Call<UserModel> getUserInfo(@Query("userId") String userId);

    @PUT("edit-user")
    Call<UserInforModel> editUserInfo(@Header("Authorization") String accessToken, @Body UserInforModel userInfo);

    @GET("get-user-follow")
    Call<List<UserFollowsModel>> getUserFollow(@Header("Authorization") String token, @Query("userId") String userId);

    @POST("add-follow")
    Call<Void> addFollow(@Header("Authorization") String token, @Body UserFollowsModel followModel);

    @HTTP(method = "DELETE", path = "delete-follow", hasBody = true)
    Call<Void> deleteFollow(@Header("Authorization") String token, @Body UserFollowsModel followModel);
}
