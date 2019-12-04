package com.example.myproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-27 16:03
 **/
@Getter
@Setter
@Entity
@Table(name = "act_re_Model")
public class ActReModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_")
    private String id;
    @Column(name = "rev_")
    private Integer rev;
    @Column(name = "name_")
    private String name;
    @Column(name = "key_")
    private String key;
    @Column(name = "category_")
    private String category;
    @Column(name = "create_time_")
    private Date createTime;
    @Column(name = "last_update_time_")
    private Date lastUpdateTime;
    @Column(name = "version_")
    private Integer version;
    @Column(name = "meta_info_")
    private String metaInfo;
    @Column(name = "deployment_id_")
    private String deploymentId;
    @Column(name = "editor_source_value_id_")
    private String editorSourceValueId;
    @Column(name = "editor_source_extra_value_id_")
    private String editorSourceExtraValueId;
    @Column(name = "tenant_id_")
    private String tenantId;
}
