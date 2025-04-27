package com.example.informatrackta.model;

public class UserModel {
    public String name, student_id, email, role, registerDate;
    public Boolean isApproved;

    public UserModel() {}

    public UserModel(String name, String student_id, String email, String role, String registerDate, Boolean isApproved) {
        this.name = name;
        this.student_id = student_id;
        this.email = email;
        this.role = role;
        this.registerDate = registerDate;
        this.isApproved = isApproved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }
}
