package com.example.appchatit.models;

import java.time.LocalDateTime;

public class TokenModel {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiration;

    public TokenModel() {
    }

    public TokenModel(String accessToken, String refreshToken, LocalDateTime expiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}