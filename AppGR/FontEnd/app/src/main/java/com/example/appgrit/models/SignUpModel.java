package com.example.appgrit.models;

public class SignUpModel {
    private String Email;
    private String Password;
    private String FirstName;
    private String LastName;
    private String Birthday;
    private String PasswordConfirmation;

    public SignUpModel(String email, String password, String firstName, String lastName, String birthday, String passwordConfirmation) {
        Email = email;
        Password = password;
        FirstName = firstName;
        LastName = lastName;
        Birthday = birthday;
        PasswordConfirmation = passwordConfirmation;
    }

    public SignUpModel() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getPasswordConfirmation() {
        return PasswordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        PasswordConfirmation = passwordConfirmation;
    }
}
