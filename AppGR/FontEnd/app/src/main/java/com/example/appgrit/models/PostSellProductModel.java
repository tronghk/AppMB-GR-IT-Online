package com.example.appgrit.models;

import java.util.Date;
import java.util.List;

public class PostSellProductModel {
    private String postSellProductId;
    private String userId;
    private String content;
    private String productName;
    private List<ImagePostModel> imagePosts;
    private String postTime;
    private float price;

    public PostSellProductModel() {
    }

    public PostSellProductModel(String postSellProductId, String userId, String content, String productName, List<ImagePostModel> imagePosts, String postTime, float price) {
        this.postSellProductId = postSellProductId;
        this.userId = userId;
        this.content = content;
        this.productName = productName;
        this.imagePosts = imagePosts;
        this.postTime = postTime;
        this.price = price;
    }

    public String getPostSellProductId() {
        return postSellProductId;
    }

    public void setPostSellProductId(String postSellProductId) {
        this.postSellProductId = postSellProductId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ImagePostModel> getImagePosts() {
        return imagePosts;
    }

    public void setImagePosts(List<ImagePostModel> imagePosts) {
        this.imagePosts = imagePosts;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}