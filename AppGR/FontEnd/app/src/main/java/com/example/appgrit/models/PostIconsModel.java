package com.example.appgrit.models;

public class PostIconsModel {
    private String UserId;
    private String Expression;
    private String UserName;
    private String UserImage;

    // Constructors, getters, and setters

    public PostIconsModel() {
    }

    public PostIconsModel(String userId, String expression, String userName, String userImage) {
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

    public String getExpression() {
        return Expression;
    }

    public void setExpression(String expression) {
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
