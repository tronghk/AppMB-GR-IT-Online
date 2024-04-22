package com.example.appchatit.network;

import com.example.appchatit.services.ChatApiService;
import com.example.appchatit.services.UserApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceProvider {
    private static final String BASE1_URL = "http://appgrit.somee.com/";
    private static final String BASE2_URL = "http://appchat.somee.com/";
    private static Retrofit retrofit1 = null;
    private static Retrofit retrofit2 = null;

    private static Retrofit getRetrofitInstance1() {
        if (retrofit1 == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder().create();

            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BASE1_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit1;
    }

    private static Retrofit getRetrofitInstance2() {
        if (retrofit2 == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder().create();

            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE2_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit2;
    }

    public static UserApiService getUserApiService() {
        return getRetrofitInstance1().create(UserApiService.class);
    }

    public static ChatApiService getChatApiService() {
        return getRetrofitInstance2().create(ChatApiService.class);
    }
}