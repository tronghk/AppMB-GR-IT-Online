package com.example.appgrit.models;

import java.util.List;

public class PostModel {
    private String PostId;
    private String PostTime;
    private String Content;
    private int CountExpression;
    private List<ExpressionModel> expressionModelList;
    private List<ImagePostModel> imagePostModelList;

    // Constructors, getters, and setters

    public PostModel() {
    }

    public PostModel(String postId, String postTime, String content, int countExpression, List<ExpressionModel> expressionModelList, List<ImagePostModel> imagePostModelList) {
        PostId = postId;
        PostTime = postTime;
        Content = content;
        CountExpression = countExpression;
        this.expressionModelList = expressionModelList;
        this.imagePostModelList = imagePostModelList;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
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

    public List<ExpressionModel> getExpressionModelList() {
        return expressionModelList;
    }

    public void setExpressionModelList(List<ExpressionModel> expressionModelList) {
        this.expressionModelList = expressionModelList;
    }

    public List<ImagePostModel> getImagePostModelList() {
        return imagePostModelList;
    }

    public void setImagePostModelList(List<ImagePostModel> imagePostModelList) {
        this.imagePostModelList = imagePostModelList;
    }
}
