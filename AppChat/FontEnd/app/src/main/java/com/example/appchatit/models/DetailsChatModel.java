package com.example.appchatit.models;

import java.time.LocalDateTime;

public class DetailsChatModel {
    private String chatId;
    private String userId;
    private String detailId;
    private LocalDateTime time;
    private String content;
    private String status;
    private String imagePath;

    public DetailsChatModel() {
    }

    public DetailsChatModel(String chatId, String userId, String detailId, LocalDateTime time, String content, String status, String imagePath) {
        this.chatId = chatId;
        this.userId = userId;
        this.detailId = detailId;
        this.time = time;
        this.content = content;
        this.status = status;
        this.imagePath = imagePath;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}