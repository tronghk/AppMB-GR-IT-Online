package com.example.appgrit.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PaymentApiService {
    @POST("payment-user")
    Call<ResponseBody> payment(
            @Header("Authorization") String token,
            @Query("userId") String userId,
            @Query("month") String month
    );
}