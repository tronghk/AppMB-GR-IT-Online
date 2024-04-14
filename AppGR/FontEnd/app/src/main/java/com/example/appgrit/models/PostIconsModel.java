package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

public class PostIconsModel {
    @SerializedName("userId")
    private String UserId;
    @SerializedName("expression")
    private int Expression;
    @SerializedName("userName")
    private String UserName;
    @SerializedName("userImage")
    private String UserImage;

    // Constructors, getters, and setters

    public PostIconsModel() {
    }

    public PostIconsModel(String userId, int expression, String userName, String userImage) {
        UserId = userId;
        Expression = expression;
        UserName = userName;
        UserImage = userImage;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getExpression() {
        return Expression;
    }

    public void setExpression(int expression) {
        Expression = expression;
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