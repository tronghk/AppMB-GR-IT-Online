package com.example.appgrit.network;

import com.example.appgrit.models.ChangePasswordModel;
import com.example.appgrit.models.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChangePassWordApiService {

    @POST("change-password") // Địa chỉ API thay đổi mật khẩu
    Call<ResponseModel> changePassword(@Header("Authorization") String authorization, @Body ChangePasswordModel changePasswordModel);
}
