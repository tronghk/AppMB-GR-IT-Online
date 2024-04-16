package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SharePostModel {
    @SerializedName("userId")
    private String UserId;

    @SerializedName("postId")
    private String PostId;

    @SerializedName("content")
    private String Content;

    @SerializedName("timeShare")
    private String timeShareString; // Chuỗi thời gian đã thay đổi kiểu dữ liệu thành String

    private Date TimeShare; // Thêm trường TimeShare kiểu Date

    // Constructors, getters, and setters

    public SharePostModel() {
    }

    public SharePostModel(String userId, String postId, String content, Date timeShare) {
        UserId = userId;
        PostId = postId;
        Content = content;
        TimeShare = timeShare;
    }

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

    // Phương thức để lấy thời gian chia sẻ dưới dạng đối tượng Date
    public Date getTimeShare() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            return format.parse(timeShareString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức để thiết lập chuỗi thời gian chia sẻ
    public void setTimeShareString(String timeShareString) {
        this.timeShareString = timeShareString;
    }

    // Getter cho chuỗi thời gian chia sẻ
    public String getTimeShareString() {
        return timeShareString;
    }

    public void setTimeShare(Date timeShare) {
        TimeShare = timeShare;
    }
}
