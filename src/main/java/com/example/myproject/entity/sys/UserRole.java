package com.example.myproject.entity.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="UserRole表",description = "用户角色对象")
public class UserRole implements Serializable {

    private static final long serialVersionUID = -951520859130539936L;
    @Id
    @ApiModelProperty(value = "主键",name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "用户角色id",name = "userRole")
    @Column(name = "user_role")
    private String userRole;

    @ApiModelProperty(value = "用户拥有角色id",name = "personRole")
    @Column(name = "person_role")
    private String personRole;
}
