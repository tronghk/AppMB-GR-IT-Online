package com.example.appgrit.network;

import com.example.appgrit.services.MarketplaceApiService;
import com.example.appgrit.services.PaymentApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceProvider {
    private static final String BASE_URL = "http://appgrit.somee.com/";
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static UserApiService getUserApiService() {
        return getRetrofitInstance().create(UserApiService.class);
    }

    public static PostApiService getPostApiService() {
        return getRetrofitInstance().create(PostApiService.class);
    }

    public static ExpressionApiService getExpressionApiService() {
        return getRetrofitInstance().create(ExpressionApiService.class);
    }

    public static PostCommentApiService getPostCommentApiService() {
        return getRetrofitInstance().create(PostCommentApiService.class);
    }

    public static MarketplaceApiService getMarketplaceApiService() {
        return getRetrofitInstance().create(MarketplaceApiService.class);
    }

    public static ChangePassWordApiService getChangePasswordApiService() {
        return getRetrofitInstance().create(ChangePassWordApiService.class);
    }

    public static PaymentApiService getPaymentApiService() {
        return getRetrofitInstance().create(PaymentApiService.class);
    }
}