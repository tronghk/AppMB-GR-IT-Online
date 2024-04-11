package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TokenModel {

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    // Sử dụng Long để lưu giữ timestamp của Expiration
    @SerializedName("expiration")
    private String expiration;

    public TokenModel() {
    }

    public TokenModel(String accessToken, String refreshToken, String expiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    // Getter cho accessToken
    public String getAccessToken() {
        return accessToken;
    }

    // Setter cho accessToken
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // Getter cho refreshToken
    public String getRefreshToken() {
        return refreshToken;
    }

    // Setter cho refreshToken
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getter cho expiration dưới dạng timestamp
    public String getExpiration() {
        return expiration;
    }

    // Setter cho expiration dưới dạng timestamp
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Date getExpirationAsDate() {
        if (expiration != null && !expiration.isEmpty()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                return format.parse(expiration);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
