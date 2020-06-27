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
@Table(name = "organ")
@ApiModel(value="Organ表",description = "机构对象")
public class Organ implements Serializable {
    private static final long serialVersionUID = 800335886544606075L;

    @Id
    @ApiModelProperty(value = "主键",name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "上级机构id",name = "pid")
    private String pid;

    @ApiModelProperty(value = "机构id",name = "organId")
    @Column(name = "organ_id")
    private String organId;

    @ApiModelProperty(value = "机构名称",name = "organName")
    @Column(name = "organ_name")
    private String organName;

    @ApiModelProperty(value = "机构等级",name = "organLevel")
    @Column(name = "organ_level")
    private String organLevel;

    @ApiModelProperty(value = "排序",name = "sort",example = "1")
    private Integer sort;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(value = "更新时间",name = "updateTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private String updateTime;

}
