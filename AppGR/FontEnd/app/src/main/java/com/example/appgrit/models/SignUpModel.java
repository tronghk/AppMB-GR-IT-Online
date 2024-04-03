package com.example.appgrit.models;

import java.util.Date; // Import thư viện Date

public class SignUpModel {
    private String Email;
    private String Password;
    private String FirstName;
    private String LastName;
    private Date Birthday; // Sử dụng kiểu dữ liệu Date thay vì LocalDateTime

    private String Gender; // Thêm thuộc tính Gender để phù hợp với backend

    private String PasswordConfirmation;

    public SignUpModel(String email, String password, String firstName, String lastName, Date birthday, String gender, String passwordConfirmation) {
        Email = email;
        Password = password;
        FirstName = firstName;
        LastName = lastName;
        Birthday = birthday;
        Gender = gender;
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

    public Date getBirthday() {
        return Birthday;
    }

    public void setBirthday(Date birthday) {
        Birthday = birthday;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPasswordConfirmation() {
        return PasswordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        PasswordConfirmation = passwordConfirmation;
    }
}
