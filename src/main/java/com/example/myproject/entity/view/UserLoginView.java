package com.example.myproject.entity.view;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "user_login_view")
public class UserLoginView implements Serializable {

    private static final long serialVersionUID = -1933600250049197625L;
    @Id
    private String id;

    private String account;

    private String password;

    private String username;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "organ_id")
    private String organId;

    @Column(name = "status_lock")
    private Integer statusLock;

    @Column(name = "organ_name")
    private String organName;

}