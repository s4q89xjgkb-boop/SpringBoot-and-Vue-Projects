package com.example.entity;

/**
 * 父类实体
 */
public class Account {
    private Integer id; // 不用int使得前端可以穿Null
    private String username;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String gender;
    private String role;
    private String token;
    private String avatar;
    private String newpassword;
    private String newpassword2;

    public String getNewpassword2() {
        return newpassword2;
    }
    public void setNewpassword2(String newpassword2) {
        this.newpassword2 = newpassword2;
    }

    public String getNewpassword() {
        return newpassword;
    }
    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public  String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role=role;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}