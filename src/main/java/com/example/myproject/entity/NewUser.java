//package com.example.myproject.entity;
//
//import com.alibaba.fastjson.annotation.JSONField;
//
//import java.io.Serializable;
//import java.util.Date;
//
//public class NewUser implements Serializable {
//
//    private Integer sid;
//
//    private String username;
//
//    private String password;
//
//    private String phone;
//
//    private Float money;
//
//    private String pswd;
//
//    public Integer getSid() {
//        return sid;
//    }
//
//    public void setSid(Integer sid) {
//        this.sid = sid;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//
//    public void setUsername(String username) {
//        this.username = username == null ? null : username.trim();
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password == null ? null : password.trim();
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone == null ? null : phone.trim();
//    }
//
//    public Float getMoney() {
//        return money;
//    }
//
//    public void setMoney(Float money) {
//        this.money = money;
//    }
//
//    public String getPswd() {
//        return pswd;
//    }
//
//    public void setPswd(String pswd) {
//        this.pswd = pswd == null ? null : pswd.trim();
//    }
//
//    @Override
//    public String toString() {
//        return "NewUser{" +
//                "sid=" + sid +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", phone='" + phone + '\'' +
//                ", money=" + money +
//                ", pswd='" + pswd + '\'' +
//                '}';
//    }
//}