package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("userId")
    private String UserId;

    @SerializedName("userName")
    private String UserName;

    @SerializedName("imagePath")
    private String ImagePath;



    // Constructors, getters, and setters

    public UserModel() {
    }

    public UserModel(String userId, String userName, String imagePath) {
        UserId = userId;
        UserName = userName;
        ImagePath = imagePath;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
