package com.example.appgrit.models;

public class UnUserModel1 {
    private String UserId;
    private String UserName;
    private String UserImage;

    // Constructors, getters, and setters

    public UnUserModel1() {
    }

    public UnUserModel1(String userId, String userName, String userImage) {
        UserId = userId;
        UserName = userName;
        UserImage = userImage;
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

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
