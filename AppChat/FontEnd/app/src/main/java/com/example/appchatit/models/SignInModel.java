package com.example.appchatit.models;

public class SignInModel {
    private String email;
    private String password;

    public SignInModel() {
    }

    public SignInModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}