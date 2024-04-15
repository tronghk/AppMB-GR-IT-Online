package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

public class ExpressionModel {
    @SerializedName("postId")
    private String PostId;

    @SerializedName("commentId")
    private String CommentId;

    @SerializedName("userId")
    private String UserId;

    @SerializedName("expression")
    private int Expression;

    @SerializedName("type")
    private String Type;

    public ExpressionModel() {
    }

    public ExpressionModel(String postId, String commentId, String userId, int expression, String type) {
        PostId = postId;
        CommentId = commentId;
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

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
