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
}