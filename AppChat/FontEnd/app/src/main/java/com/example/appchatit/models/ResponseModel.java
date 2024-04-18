package com.example.appchatit.models;

public class ResponseModel {
    private String status;
    private String message;

    public ResponseModel() {
    }

    public ResponseModel(String status, String message) {
        this.status = status;
        this.message = message;
    }
}