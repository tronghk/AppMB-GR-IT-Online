
package com.example.appgrit.network;

import com.example.appgrit.models.ExpressionModel;
import com.example.appgrit.models.PostIconsModel;
import com.example.appgrit.models.PostModel;
import com.example.appgrit.models.PostSellProductModel;
import com.example.appgrit.models.ResponseModel;
import com.example.appgrit.models.SharePostModel;
import com.example.appgrit.models.UserFriendsModel;
import com.example.appgrit.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.DELETE;

import retrofit2.http.PUT;
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
    @GET("get-sum-post-day")
    Call<ResponseBody> GetSumPost(
            @Header("Authorization") String token

    );
    @GET("get-user-locked")
    Call<List<UserModel>> GetUserLocked(
            @Header("Authorization") String token

    );
    @GET("get-user-add-friend")
    Call<List<UserModel>> GetUserAddFriends(
            @Header("Authorization") String token

    );
    @GET("get-sum-user")
    Call<ResponseBody> GetSumUser(
            @Header("Authorization") String token

    );
    @GET("sum-payment-of-month")
    Call<ResponseBody> GetSumPayMent(
            @Header("Authorization") String token

    );
    @POST("unlock-user")
    Call<ResponseBody> UnlockUser(
            @Header("Authorization") String token,
            @Query("userId") String userId

    );
    @GET("get-sum-post-week")
    Call<ResponseBody> GetSumPostWeek(
            @Header("Authorization") String token

    );
    @GET("compare-gain-week")
    Call<ResponseBody> GetSumPostComPa(
            @Header("Authorization") String token

    );

    @POST("add-post-user")
    Call<PostModel> addPost(
            @Header("Authorization") String token,
            @Body PostModel postModel
    );
    @POST("add-image-instead-user")
    Call<PostModel> addPostAvt(
            @Header("Authorization") String token,
            @Body PostModel postModel
    );

    @POST("add-sell-post")
    Call<PostSellProductModel> addSellPost(
            @Header("Authorization") String token,
            @Body PostSellProductModel postSellProductModel
    );
    @PUT("edit-sell-post")
    Call<PostSellProductModel> updateSellPost(
            @Header("Authorization") String token,
            @Body PostSellProductModel postSellProductModel
    );
    @PUT("update-friend")
    Call<UserFriendsModel> UpdateFriend(@Header("Authorization") String token, @Body UserFriendsModel model);
    @DELETE("delete-friend")
    Call<ResponseBody> DeleteFriend(@Header("Authorization") String token,@Body UserFriendsModel model);

    @GET("get-post")
    Call<List<PostModel>> getPostUser(
            @Header("Authorization") String token,
            @Query("userId") String userId
    );
    @GET("get-post-self")
    Call<List<PostModel>> getPostSelfUser(@Query("userId") String userId);


    @POST("/share-post")
    Call<ResponseModel> sharePost(@Header("Authorization") String token, @Body SharePostModel sharePostModel);

    @DELETE("/delete-share-post")
    Call<ResponseModel> deleteSharePost(@Query("postId") String postId, @Query("userId") String userId);

    @GET("/get-share-post")
    Call<List<PostModel>> getSharedPosts(@Query("userId") String userId);


    @PUT("edit-post")
    Call<PostModel> editPost(
            @Header("Authorization") String token,
            @Body PostModel postModel
    );

    @DELETE("delete-post")
    Call<ResponseModel> deletePost(
            @Header("Authorization") String token,
            @Query("postId") String postId,
            @Query("userId") String userId
    );
    @GET("get-post-expression")
    Call<List<PostIconsModel>> getPostExpressions(
            @Header("Authorization") String token,
            @Query("postId") String postId
    );
    @GET("get-post-expressions")
    Call<List<ExpressionModel>> getPostExpressions(@Query("postId") String postId);
}
