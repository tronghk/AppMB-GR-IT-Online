package com.example.appgrit.models;

public class UserModel {
    private String userId;
    private String userName;
    private String imagePath;

    // Constructors, getters, and setters

    public UserModel() {
    }

    public UserModel(String userId, String userName, String imagePath) {
        userId = userId;
        userName = userName;
        imagePath = imagePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        userName = userName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        imagePath = imagePath;
    }
}
