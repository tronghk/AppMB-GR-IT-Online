package com.example.appgrit.models;

public class ImagePostModel {
    private String ImageContent;
    private String ImagePath;

    // Constructors, getters, and setters

    public ImagePostModel() {
    }

    public ImagePostModel(String imageContent, String imagePath) {
        ImageContent = imageContent;
        ImagePath = imagePath;
    }

    public String getImageContent() {
        return ImageContent;
    }

    public void setImageContent(String imageContent) {
        ImageContent = imageContent;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
