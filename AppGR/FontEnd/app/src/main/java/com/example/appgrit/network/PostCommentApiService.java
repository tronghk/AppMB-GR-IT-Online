package com.example.appgrit.network;

import com.example.appgrit.models.PostCommentModel;
import com.example.appgrit.models.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostCommentApiService {
    @GET("get-post-comment")
    Call<List<PostCommentModel>> getComments(@Header("Authorization") String token, @Query("postId") String postId);

    @POST("add-comment-post")
    Call<PostCommentModel> addComment(@Header("Authorization") String token, @Body PostCommentModel comment);

    @DELETE("delete-comment-post")
    Call<ResponseModel> deleteComment(@Header("Authorization") String token, @Query("commentId") String commentId, @Query("userId") String userId);
}

