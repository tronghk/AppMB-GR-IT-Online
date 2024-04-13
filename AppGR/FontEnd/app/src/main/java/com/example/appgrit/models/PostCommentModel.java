package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostCommentModel {
    @SerializedName("userId")
    private String UserId;
    private UserModel user; // User details for the comment
    @SerializedName("postId")
    private String PostId;

    @SerializedName("content")
    private String Content;

    @SerializedName("commentId")
    private String CommentId;

    @SerializedName("commentTime")
    private String CommentTime; // Đã thay đổi kiểu dữ liệu thành String

    // Thêm các thuộc tính mới
    @SerializedName("userName")
    private String UserName;

    @SerializedName("userImage")
    private String UserImage;

    // Các getter và setter cho các thuộc tính mới

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    // Các getter và setter cũ

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }


    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
    }

    // Phương thức để lấy thời gian bình luận dưới dạng đối tượng Date
    public Date getComment() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            return format.parse(CommentTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức để thiết lập chuỗi thời gian
    public void setCommentTimeString(String commentTimeString) {
        this.CommentTime = commentTimeString;
    }

    // Getter cho chuỗi thời gian
    public String getCommentTime() {
        return CommentTime;
    }

    public void setCommentTime(Date commentTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        this.CommentTime = format.format(commentTime);
    }
}
