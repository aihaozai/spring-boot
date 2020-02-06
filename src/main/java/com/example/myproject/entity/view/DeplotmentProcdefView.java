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
 * @Create: 2019-12-05 15:41
 **/
@Getter
@Setter
@Entity
@Table(name="deplotment_procdef_view")
public class DeplotmentProcdefView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "name")
    private String modelName;

    @Column(name = "rev_")
    private Integer rev;

    @Column(name = "category_")
    private String category;

    @Column(name = "name_")
    private String name;

    @Column(name = "key_")
    private String key;

    @Column(name = "version_")
    private Integer version;

    @Column(name = "deployment_id_")
    private String deploymentId;
    
    @Column(name = "resource_name_")
    private String resourceName;
    
    @Column(name = "dgrm_resource_name_")
    private String dgrmResourceName;
    
    @Column(name = "description_")
    private String description;
    
    @Column(name = "has_start_form_key_")
    private boolean hasStartFormKey;
    
    @Column(name = "has_graphical_notation_")
    private boolean hasGraphicalNotation;

    @Column(name = "suspension_state_")
    private Integer suspensionState;
    
    @Column(name = "tenant_id_")
    private String tenantId;

}
