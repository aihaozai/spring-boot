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
 * @Create: 2019-10-19 11:20
 **/
@Getter
@Setter
@Entity
@Table(name = "person")
@ApiModel(value="Person表",description = "人员角色对象")
public class Person implements Serializable {
    private static final long serialVersionUID = -6505534113789687182L;

    @Id
    @ApiModelProperty(value = "主键",name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    @Column(name = "role_name")
    private String roleName;
}
