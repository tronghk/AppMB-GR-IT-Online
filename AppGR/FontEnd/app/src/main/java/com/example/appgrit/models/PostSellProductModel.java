package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostSellProductModel {
    @SerializedName("postSellProductId")
    private String PostSellProductId;
    @SerializedName("userId")
    private String UserId;
    @SerializedName("content")
    private String Content;
    @SerializedName("productName")
    private String ProductName;
    @SerializedName("imagePosts")
    private List<ImagePostModel> ImagePosts;
    @SerializedName("postTime")
    private String PostTime;
    @SerializedName("price")
    private float Price;

    public PostSellProductModel() {
    }

    public PostSellProductModel(String postSellProductId, String userId, String content, String productName, List<ImagePostModel> imagePosts, String postTime, float price) {
        this.PostSellProductId = postSellProductId;
        this.UserId = userId;
        this.Content = content;
        this.ProductName = productName;
        this.ImagePosts = imagePosts;
        this.PostTime = postTime;
        this.Price = price;
    }

    public String getPostSellProductId() {
        return PostSellProductId;
    }

    public void setPostSellProductId(String postSellProductId) {
        this.PostSellProductId = postSellProductId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public List<ImagePostModel> getImagePosts() {
        return ImagePosts;
    }

    public void setImagePosts(List<ImagePostModel> imagePosts) {
        this.ImagePosts = imagePosts;
    }

    // Phương thức để lấy thời gian bài đăng dưới dạng đối tượng Date
    public Date getPostTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            return format.parse(PostTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getter cho chuỗi thời gian
    public String getPostTimeAsString() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        this.PostTime = postTime;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        this.Price = price;
    }
}
