package com.example.appgrit.models;

public class EditUserImageModel {
    private String UserId;
    private String ImagePath;

    // Constructors, getters, and setters

    public EditUserImageModel() {
    }

    public EditUserImageModel(String userId, String imagePath) {
        UserId = userId;
        ImagePath = imagePath;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
