package com.example.appgrit.models;

public class UserRenewalModel {
    private String UserId;
    private float Price;

    // Constructors, getters, and setters

    public UserRenewalModel() {
    }

    public UserRenewalModel(String userId, float price) {
        UserId = userId;
        Price = price;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }
}
