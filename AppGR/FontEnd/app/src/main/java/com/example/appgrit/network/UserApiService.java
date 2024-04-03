package com.example.appgrit.network;

import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {
    @POST("signup")
    Call<TokenModel> signUp(@Body SignUpModel signUpModel); // Sửa kiểu trả về từ ResponseModel thành TokenModel

    @POST("signin")
    Call<TokenModel> signIn(@Body SignInModel signInModel);
}