package com.example.myproject.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
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
 * @Create: 2019-12-20 16:20
 **/
@Getter
@Setter
@Entity
@Table(name = "dict")
@ApiModel(value="Dict表",description = "数据字典对象")
public class Dict implements Serializable {
    private static final long serialVersionUID = 800335886544606075L;
    @Id
    @ApiModelProperty(value = "主键",name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "上级id",name = "pid")
    private String pid;

    @ApiModelProperty(value = "上级名称",name = "pname")
    @Column(name = "p_name")
    private String pname;

    @ApiModelProperty(value = "字典id",name = "createTime")
    @Column(name = "dict_id")
    private String dictId;

    @ApiModelProperty(value = "字典名称",name = "dictName")
    @Column(name = "dict_name")
    private String dictName;

    @ApiModelProperty(value = "字典代码",name = "dictCode")
    @Column(name = "dict_code")
    private String dictCode;

    @ApiModelProperty(value = "排序",name = "sort",example = "1")
    private Integer sort;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(value = "更新时间",name = "updateTime")
    @Column(name = "update_time")
    private String updateTime;

}
