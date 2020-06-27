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
 * @Create: 2019-12-20 16:44
 **/
@Getter
@Setter
@Entity
@Table(name = "organ_role")
public class OrganRole implements Serializable {
    private static final long serialVersionUID = 5161144247567983328L;

    @Id
    private String id;
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "role_organ")
    private String roleOrgan;

}