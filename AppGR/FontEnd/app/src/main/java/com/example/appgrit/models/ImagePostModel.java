package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

public class ImagePostModel {
    @SerializedName("imageContent")
    private String ImageContent;

    @SerializedName("imagePath")
    private String ImagePath;

    @SerializedName("imageId")
    private String ImageId;

    // Constructors, getters, and setters

    public ImagePostModel() {
    }

    public ImagePostModel(String imageContent, String imagePath, String imageId) {
        ImageContent = imageContent;
        ImagePath = imagePath;
        ImageId = imageId;
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

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }
}
