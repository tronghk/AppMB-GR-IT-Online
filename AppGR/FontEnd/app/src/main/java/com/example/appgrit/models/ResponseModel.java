package com.example.appgrit.models;

public class ResponseModel {
    private String Status;
    private String Message;

    // Constructors, getters, and setters

    public ResponseModel() {
    }

    public ResponseModel(String status, String message, TokenModel token) {
        Status = status;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
