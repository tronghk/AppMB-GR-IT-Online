// frontend
package com.example.appgrit.models;

import java.util.List;

public class PostCommentsModel {
    private String UserId;
    private String UserName;
    private String Content;
    private List<ExpressionModel> Expressions; // Cập nhật tên thuộc tính
    private int CountExpression;

    // Constructors, getters, and setters

    public PostCommentsModel() {
    }

    public PostCommentsModel(String userId, String userName, String content, int countExpression, List<ExpressionModel> expressions) { // Cập nhật tên tham số
        UserId = userId;
        UserName = userName;
        Content = content;
        CountExpression = countExpression;
        Expressions = expressions; // Cập nhật phần gán
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getCountExpression() {
        return CountExpression;
    }

    public void setCountExpression(int countExpression) {
        CountExpression = countExpression;
    }

    public List<ExpressionModel> getExpressions() { // Cập nhật tên getter
        return Expressions;
    }

    public void setExpressions(List<ExpressionModel> expressions) { // Cập nhật tên setter
        Expressions = expressions;
    }
}
