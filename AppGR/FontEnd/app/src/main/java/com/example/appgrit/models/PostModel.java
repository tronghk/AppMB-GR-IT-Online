package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostModel {
    @SerializedName("postId")
    private String PostId;
    @SerializedName("userId")
    private String UserId;

    @SerializedName("postTime")
    private String postTimeString; // Đã thay đổi kiểu dữ liệu thành String

    @SerializedName("imagePost")
    private List<ImagePostModel> ImagePost;

    @SerializedName("content")
    private String Content;

    @SerializedName("postType")
    private String PostType;

    public PostModel() {
    }

    public PostModel(String userId, String content, String imageUrl) {
        this.UserId = userId;
        this.Content = content;
        this.ImagePost = new ArrayList<>();
        this.PostType = "";
    }

    public PostModel(String postId, String userId, String postTime, List<ImagePostModel> imagePost, String content, String postType) {
        PostId = postId;
        UserId = userId;
        this.postTimeString = postTime; // Lưu trữ chuỗi thời gian
        ImagePost = imagePost;
        Content = content;
        PostType = postType;
    }

    // Các getter và setter
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

    // Phương thức để lấy thời gian bài đăng dưới dạng đối tượng Date
    public Date getPostTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            return format.parse(postTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức để thiết lập chuỗi thời gian
    public void setPostTimeString(String postTimeString) {
        this.postTimeString = postTimeString;
    }

    // Getter cho chuỗi thời gian
    public String getPostTimeString() {
        return postTimeString;
    }

    public List<ImagePostModel> getImagePost() {
        return ImagePost;
    }

    public void setImagePost(List<ImagePostModel> imagePost) {
        ImagePost = imagePost;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPostType() {
        return PostType;
    }

    public void setPostType(String postType) {
        PostType = postType;
    }
}