package com.example.appgrit.models;

public class UnUserModel {
    private String UserId;
    private String UnUserId;

    // Constructors, getters, and setters

    public UnUserModel() {
    }

    public UnUserModel(String userId, String unUserId) {
        UserId = userId;
        UnUserId = unUserId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUnUserId() {
        return UnUserId;
    }

    public void setUnUserId(String unUserId) {
        UnUserId = unUserId;
    }
}
