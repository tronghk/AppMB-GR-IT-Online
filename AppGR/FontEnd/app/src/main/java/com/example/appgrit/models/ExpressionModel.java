package com.example.appgrit.models;

public class ExpressionModel {
    private String PostId;
    private String UserId;
    private String Expression;
    private String Type;

    // Constructors, getters, and setters

    public ExpressionModel() {
    }

    public ExpressionModel(String postId, String userId, String expression, String type) {
        PostId = postId;
        UserId = userId;
        Expression = expression;
        Type = type;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
