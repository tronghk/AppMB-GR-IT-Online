package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserInforModel {
    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("gender")
    private String gender;

    // Lưu ý: birthday giữ dạng String để phù hợp với định dạng trả về từ API
    @SerializedName("birthday")
    private String birthday;

    @SerializedName("address")
    private String address;

    @SerializedName("userId")
    private String userId;

    @SerializedName("phone")
    private String phone;

    // Constructors
    public UserInforModel(String firstname, String lastName, String gender, String birthday, String address, String userId, String phone) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.userId = userId;
        this.phone = phone;
    }

    // Getters
    public String getFirstname() {
        return firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Phương thức chuyển đổi String birthday sang Date
    public Date getBirthdayAsDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(this.birthday);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Hoặc xử lý lỗi tùy ý
        }
    }
}
