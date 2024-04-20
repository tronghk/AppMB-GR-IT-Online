package com.example.appgrit.services;

import com.example.appgrit.models.PostSellProductModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MarketplaceApiService {
    @GET("post-sell-product")
    Call<List<PostSellProductModel>> getPostSell(
            @Header("Authorization") String token,
            @Query("userId") String userId
    );

    @POST("add-sell-post")
    Call<PostSellProductModel> addSellPost(
            @Header("Authorization") String token,
            @Body PostSellProductModel postSellProductModel
    );

    @Multipart
    @POST("add-list-image-from-post")
    Call<List<String>> uploadImages(
            @Header("Authorization") String token,
            @Part List<MultipartBody.Part> images
    );
}