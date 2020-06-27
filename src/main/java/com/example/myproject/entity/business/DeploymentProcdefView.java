package com.example.myproject.entity.business;

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
 * @Create: 2019-12-05 15:41
 **/
@Getter
@Setter
@Entity
@Table(name="deployment_procdef_view")
@ApiModel(value="DeploymentProcdefView视图",description = "部署流程视图对象")
public class DeploymentProcdefView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "permission_role表主键",name = "id")
    @Column(name = "id_")
    private String id;

    @ApiModelProperty(value = "模型名称",name = "modelName")
    @Column(name = "name")
    private String modelName;

    @ApiModelProperty(value = "乐观锁",name = "rev",example = "1")
    @Column(name = "rev_")
    private Integer rev;

    @ApiModelProperty(value = "流程定义的Namespace",name = "category",example = "http://www.activiti.org/test")
    @Column(name = "category_")
    private String category;

    @ApiModelProperty(value = "名称",name = "name")
    @Column(name = "name_")
    private String name;

    @ApiModelProperty(value = "流程定义ID",name = "key")
    @Column(name = "key_")
    private String key;

    @ApiModelProperty(value = "版本",name = "version",example = "1")
    @Column(name = "version_")
    private Integer version;

    @ApiModelProperty(value = "部署表ID",name = "deploymentId")
    @Column(name = "deployment_id_")
    private String deploymentId;

    @ApiModelProperty(value = "bpmn文件名称",name = "resourceName")
    @Column(name = "resource_name_")
    private String resourceName;

    @ApiModelProperty(value = "流程图片名称",name = "dgrmResourceName")
    @Column(name = "dgrm_resource_name_")
    private String dgrmResourceName;

    @ApiModelProperty(value = "描述",name = "description")
    @Column(name = "description_")
    private String description;

    @ApiModelProperty(value = "start节点是否存在formKey",name = "hasStartFormKey",example = "0")
    @Column(name = "has_start_form_key_")
    private boolean hasStartFormKey;
    
    @Column(name = "has_graphical_notation_")
    private boolean hasGraphicalNotation;

    @ApiModelProperty(value = "是否挂起",name = "hasStartFormKey",example = "1")
    @Column(name = "suspension_state_")
    private Integer suspensionState;
    
    @Column(name = "tenant_id_")
    private String tenantId;

}
