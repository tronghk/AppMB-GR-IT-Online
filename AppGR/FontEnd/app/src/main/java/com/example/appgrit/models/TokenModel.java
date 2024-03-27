package com.example.appgrit.models;

import java.time.LocalDateTime;

public class TokenModel {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiration;
    public TokenModel() {
    }

    // Constructors, getters, and setters


    public TokenModel(String accessToken, String refreshToken, LocalDateTime expiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
