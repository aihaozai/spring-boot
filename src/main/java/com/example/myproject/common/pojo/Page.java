package com.example.myproject.common.pojo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-15 12:51
 **/
@Getter
@Setter
@ApiModel(value="Page",description = "分页")
public class Page implements Serializable {
    private static final long serialVersionUID = -5601574509801101671L;
    @ApiModelProperty(value="每页多少",name="limit",example = "10")
    private Integer limit;
    @ApiModelProperty(value="第几页",name="page",example = "1")
    private Integer page;
    @ApiModelProperty(value="请求参数",name="data")
    private JSONObject data;
}
