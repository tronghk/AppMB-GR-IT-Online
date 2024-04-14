package com.example.appgrit.network;

import com.example.appgrit.models.ExpressionModel;
import com.example.appgrit.models.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ExpressionApiService {
    @POST("/add-expression-post")
    Call<ResponseModel> addExpression(@Header("Authorization") String token,
                                      @Body ExpressionModel expression);
    @HTTP(method = "DELETE", path = "/delete-expression-post", hasBody = true)
    Call<ResponseModel> deleteExpression(@Header("Authorization") String token,
                                         @Body ExpressionModel expression);

    @GET("count-expression-type")
    Call<String> countExpressionInPost(@Query("postId") String postId, @Query("expression") String expression);

}
