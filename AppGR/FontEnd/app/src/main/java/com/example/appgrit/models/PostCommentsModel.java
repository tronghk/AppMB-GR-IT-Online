package com.example.appgrit.models;

import java.util.List;

public class PostCommentsModel {
    private String UserId;
    private String UserName;
    private String Content;
    private int CountExpression;
    private List<ExpressionModel> expressionList;

    // Constructors, getters, and setters

    public PostCommentsModel() {
    }

    public PostCommentsModel(String userId, String userName, String content, int countExpression, List<ExpressionModel> expressionList) {
        UserId = userId;
        UserName = userName;
        Content = content;
        CountExpression = countExpression;
        this.expressionList = expressionList;
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

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getCountExpression() {
        return CountExpression;
    }

    public void setCountExpression(int countExpression) {
        CountExpression = countExpression;
    }

    public List<ExpressionModel> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<ExpressionModel> expressionList) {
        this.expressionList = expressionList;
    }
}
