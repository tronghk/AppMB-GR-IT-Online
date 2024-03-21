package com.example.appgrit.network;

import com.example.appgrit.models.SignInModel;
import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {
    @POST("signup")
    Call<Response<TokenModel>> signUp(@Body SignUpModel signUpModel);

    @POST("signin")
    Call<TokenModel> signIn(@Body SignInModel signInModel);

}
