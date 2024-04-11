package com.example.appgrit.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostSellProductModel {
    private String PostSellProductId;
    private String UserId;
    private List<ImagePostModel> imagePosts;
    private String ProductName;
    private String Content;
    private Date PostTime;
    private float Price;

    // Constructors, getters, and setters

    public PostSellProductModel() {
    }

    public PostSellProductModel(String postSellProductId, String userId, List<ImagePostModel> imagePosts, String productName, String content, String postTime, float price) {
        PostSellProductId = postSellProductId;
        UserId = userId;
        this.imagePosts = imagePosts;
        ProductName = productName;
        Content = content;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            PostTime = format.parse(postTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public List<ImagePostModel> getImagePosts() {
        return imagePosts;
    }

    public void setImagePosts(List<ImagePostModel> imagePosts) {
        this.imagePosts = imagePosts;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Date getPostTime() {
        return PostTime;
    }

    public void setPostTime(Date postTime) {
        PostTime = postTime;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }
}
