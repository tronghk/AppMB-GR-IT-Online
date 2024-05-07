package com.example.appchatit.models;

public class ChatModel {
    private String userId;
    private String userOrtherId;
    private String messId;

    public ChatModel() {
    }

    public ChatModel(String userId, String userOrtherId, String messId) {
        this.userId = userId;
        this.userOrtherId = userOrtherId;
        this.messId = messId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserOrtherId() {
        return userOrtherId;
    }

    public void setUserOrtherId(String userOrtherId) {
        this.userOrtherId = userOrtherId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }
}