package com.example.myproject.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
public class Organ implements Serializable {
    private static final long serialVersionUID = 800335886544606075L;
    @Id
    private String id;

    private String pid;
    @Column(name = "organ_id")
    private String organId;

    @Column(name = "organ_name")
    private String organName;

    @Column(name = "organ_level")
    private String organLevel;

    private Integer sort;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private String createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private String updateTime;

}
