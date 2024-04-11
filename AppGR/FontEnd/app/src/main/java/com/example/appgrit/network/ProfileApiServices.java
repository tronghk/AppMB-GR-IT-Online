package com.example.appgrit.network;

import com.example.appgrit.models.SignUpModel;
import com.example.appgrit.models.TokenModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfileApiServices {
    @GET("get-post")
    Call<TokenModel> GetPostSum(@Body String userId);
}
