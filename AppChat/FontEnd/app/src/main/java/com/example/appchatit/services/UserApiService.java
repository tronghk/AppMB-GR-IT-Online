package com.example.appchatit.services;

import com.example.appchatit.models.SignInModel;
import com.example.appchatit.models.TokenModel;
import com.example.appchatit.models.UserFriendModel;
import com.example.appchatit.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApiService {
    @POST("signin")
    Call<TokenModel> signIn(@Body SignInModel signInModel);

    @GET("get-user-id")
    Call<UserModel> getUserBasic(@Query("userId") String userId);

    @GET("get-listUserFriend")
    Call<List<UserFriendModel>> getListUserFriend(@Query("userId") String userId);

    @Multipart
    @POST("add-image-from-post")
    Call<List<String>> uploadImage(
            @Part MultipartBody.Part image
    );
}