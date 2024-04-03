package com.example.appgrit.models;

import java.util.Date; // Import thư viện Date

public class TokenModel {

    private String AccessToken;
    private String RefreshToken;
    private Date Expiration;

    public TokenModel() {
    }

    public TokenModel(String accessToken, String refreshToken, Date expiration) {
        this.AccessToken = accessToken;
        this.RefreshToken = refreshToken;
        this.Expiration = expiration;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.AccessToken = accessToken;
    }

    public String getRefreshToken() {
        return RefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.RefreshToken = refreshToken;
    }

    public Date getExpiration() {
        return Expiration;
    }

    public void setExpiration(Date expiration) {
        this.Expiration = expiration;
    }
}
