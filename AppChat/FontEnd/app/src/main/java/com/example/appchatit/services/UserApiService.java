package com.example.appchatit.services;

import com.example.appchatit.models.SignInModel;
import com.example.appchatit.models.TokenModel;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApiService {

    @POST("signin")
    Call<TokenModel> signIn(@Body SignInModel signInModel);
}