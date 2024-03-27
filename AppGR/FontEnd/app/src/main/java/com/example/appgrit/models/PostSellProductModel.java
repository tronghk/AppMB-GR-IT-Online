package com.example.appgrit.models;

public class PostSellProductModel {
    private String PostSellProductId;
    private String UserId;
    private String Content;
    private double Price;

    // Constructors, getters, and setters

    public PostSellProductModel() {
    }

    public PostSellProductModel(String postSellProductId, String userId, String content, double price) {
        PostSellProductId = postSellProductId;
        UserId = userId;
        Content = content;
        Price = price;
    }

    public String getPostSellProductId() {
        return PostSellProductId;
    }

    public void setPostSellProductId(String postSellProductId) {
        PostSellProductId = postSellProductId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
