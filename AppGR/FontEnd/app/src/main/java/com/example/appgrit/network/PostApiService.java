
package com.example.appgrit.network;

import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.PostSellProductModel;
import com.example.appgrit.models.ResponseModel;

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

public interface PostApiService {
    @Multipart
    @POST("add-image-from-post")
    Call<List<String>> uploadImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("add-list-image-from-post")
    Call<List<String>> uploadImages(
            @Header("Authorization") String token,
            @Part List<MultipartBody.Part> images
    );

    @POST("add-post-user")
    Call<PostModel> addPost(
            @Header("Authorization") String token,
            @Body PostModel postModel
    );
    @POST("/add-sell-post")
    Call<PostSellProductModel> addSellPost(
            @Header("Authorization") String token,
            @Body PostSellProductModel postSellProductModel
    );

    @GET("get-post")
    Call<List<PostModel>> getPostUser(
            @Header("Authorization") String token,
            @Query("userId") String userId
    );
    @GET("get-post-self")
    Call<List<PostModel>> getPostSelfUser(@Query("userId") String userId);


}
