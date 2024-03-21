package com.example.appgrit.models;

public class ChangePasswordModel {
    private String Email;
    private String OldPassword;
    private String NewPassword;

    // Constructors, getters, and setters

    public ChangePasswordModel() {
    }

    public ChangePasswordModel(String email, String oldPassword, String newPassword) {
        Email = email;
        OldPassword = oldPassword;
        NewPassword = newPassword;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }
}
