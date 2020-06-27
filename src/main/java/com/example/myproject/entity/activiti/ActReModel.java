package com.example.myproject.entity.activiti;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="ActReModel表",description = "流程设计模型部署对象")
public class ActReModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ApiModelProperty(value = "主键",name = "id",required = true)
    @Column(name = "id_")
    private String id;

    @ApiModelProperty(value = "乐观锁",name = "rev",example = "1")
    @Column(name = "rev_")
    private Integer rev;

    @ApiModelProperty(value = "名称",name = "name")
    @Column(name = "name_")
    private String name;

    @ApiModelProperty(value = "部署key",name = "key")
    @Column(name = "key_")
    private String key;

    @ApiModelProperty(value = "分类",name = "category")
    @Column(name = "category_")
    private String category;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    @Column(name = "create_time_")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间",name = "lastUpdateTime")
    @Column(name = "last_update_time_")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "版本",name = "version",example = "1")
    @Column(name = "version_")
    private Integer version;

    @ApiModelProperty(value = "以json格式保存流程定义的信息",name = "metaInfo")
    @Column(name = "meta_info_")
    private String metaInfo;

    @ApiModelProperty(value = "部署id",name = "deploymentId")
    @Column(name = "deployment_id_")
    private String deploymentId;

    @ApiModelProperty(value = "编辑源值id",name = "editorSourceValueId")
    @Column(name = "editor_source_value_id_")
    private String editorSourceValueId;

    @ApiModelProperty(value = "编辑源额外值id",name = "editorSourceExtraValueId")
    @Column(name = "editor_source_extra_value_id_")
    private String editorSourceExtraValueId;

    @ApiModelProperty(value = "名称",name = "name")
    @Column(name = "tenant_id_")
    private String tenantId;
}
