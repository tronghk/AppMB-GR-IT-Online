package com.example.appgrit.models;

public class UnUserModel {
    private String UserId;
    private String UnUserId;
    private String UnUserName;
    private String UnUserImage;

    // Constructors, getters, and setters

    public UnUserModel() {
    }

    public UnUserModel(String userId, String unUserId, String unUserName, String unUserImage) {
        UserId = userId;
        UnUserId = unUserId;
        UnUserName = unUserName;
        UnUserImage = unUserImage;
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

    public String getUnUserName() {
        return UnUserName;
    }

    public void setUnUserName(String unUserName) {
        UnUserName = unUserName;
    }

    public String getUnUserImage() {
        return UnUserImage;
    }

    public void setUnUserImage(String unUserImage) {
        UnUserImage = unUserImage;
    }
}
