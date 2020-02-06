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
@Table(name = "dict")
public class Dict implements Serializable {
    private static final long serialVersionUID = 800335886544606075L;
    @Id
    private String id;

    private String pid;

    @Column(name = "p_name")
    private String pname;

    @Column(name = "dict_id")
    private String dictId;

    @Column(name = "dict_name")
    private String dictName;

    @Column(name = "dict_code")
    private String dictCode;

    private Integer sort;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

}
