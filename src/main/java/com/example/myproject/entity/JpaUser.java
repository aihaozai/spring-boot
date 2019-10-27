//package com.example.myproject.entity;
//import org.hibernate.annotations.Proxy;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "user")
//@Proxy(lazy =false)
//public class JpaUser implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
//   // @Column(name = "sid")
//    public Integer getSid() {
//        return sid;
//    }
//
//    public void setSid(Integer sid) {
//        this.sid = sid;
//    }
//    //@Column(name = "username")
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//    //@Column(name = "password")
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//    //@Column(name = "phone")
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//    //@Column(name = "money")
//    public Float getMoney() {
//        return money;
//    }
//
//    public void setMoney(Float money) {
//        this.money = money;
//    }
//    //@Column(name = "pswd")
//    public String getPswd() {
//        return pswd;
//    }
//
//    public void setPswd(String pswd) {
//        this.pswd = pswd;
//    }
//
//    @Override
//    public String toString() {
//        return "JpaUser{" +
//                "sid=" + sid +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", phone='" + phone + '\'' +
//                ", money=" + money +
//                ", pswd='" + pswd + '\'' +
//                '}';
//    }
//}
