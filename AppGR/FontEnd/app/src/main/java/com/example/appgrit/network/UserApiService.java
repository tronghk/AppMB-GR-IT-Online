package com.example.appgrit.network;

import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;
import com.example.appgrit.models.UserInforModel;
import com.example.appgrit.models.ResponseModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("reset-password")
    Call<ResponseBody> resetPassword(@Query("email") String email);
}
