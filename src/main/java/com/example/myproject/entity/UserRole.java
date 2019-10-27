package com.example.myproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-19 15:01
 **/
@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = -951520859130539936L;
    @Id
    private String id;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "person_role")
    private String personRole;
}
