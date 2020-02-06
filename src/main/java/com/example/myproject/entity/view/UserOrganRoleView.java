package com.example.myproject.entity.view;

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
 * @Create: 2020-01-06 12:04
 **/
@Getter
@Setter
@Entity
@Table(name = "user_organ_role_view")
public class UserOrganRoleView implements Serializable {
    private static final long serialVersionUID = -6263583581683080437L;

    @Id
    private String id;

    private String username;

    @Column(name = "organ_id")
    private String organId;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_organ")
    private String roleOrgan;
    @Column(name = "organ_level")
    private String organLevel;
}
