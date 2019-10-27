package com.example.myproject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = -1933600250049197625L;
    // 用户状态：锁定
    public static final Integer STATUS_LOCK = 0;

    @Id
    private String id;

    private String account;

    private String password;

    private String username;

    @Column(name = "status_lock")
    private Integer statusLock;

    @Column(name = "role_id")
    private String roleId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private String createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private String updateTime;

}