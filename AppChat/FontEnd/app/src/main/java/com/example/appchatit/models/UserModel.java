package com.example.appchatit.models;

public class UserModel {
    private String userId;
    private String userName;
    private String imagePath;

    public UserModel() {
    }

    public UserModel(String userId, String userName, String imagePath) {
        this.userId = userId;
        this.userName = userName;
        this.imagePath = imagePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}