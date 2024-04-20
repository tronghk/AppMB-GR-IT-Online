package com.example.appchatit.models;

public class ChatModel {
    private String userId;
    private String userOtherId;
    private String messId;

    public ChatModel() {
    }

    public ChatModel(String userId, String userOtherId, String messId) {
        this.userId = userId;
        this.userOtherId = userOtherId;
        this.messId = messId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserOtherId() {
        return userOtherId;
    }

    public void setUserOtherId(String userOtherId) {
        this.userOtherId = userOtherId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }
}