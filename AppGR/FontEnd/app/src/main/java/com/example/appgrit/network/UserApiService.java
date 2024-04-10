package com.example.appgrit.network;

import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApiService {
    @POST("signup")
    Call<Response<TokenModel>> signUp(@Body SignUpModel signUpModel);

    @POST("signin")
    Call<TokenModel> signIn(@Body SignInModel signInModel);

    @GET("reset-password")
    Call<ResponseBody> resetPassword(@Query("email") String email);

}
