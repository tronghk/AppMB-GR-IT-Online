package com.example.appchatit.services;

import com.example.appchatit.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ChatApiService {
    @GET("get-listMessOrtherUser")
    Call<List<UserModel>> getListMessOtherUser(
            @Header("Authorization") String token,
            @Query("userId") String userId
    );

    @GET("get-chat")
    Call<List<UserModel>> getChat(
            @Header("Authorization") String token,
            @Query("userId") String userId
    );
}