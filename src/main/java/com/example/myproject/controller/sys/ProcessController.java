package com.example.myproject.controller.sys;

import com.example.myproject.common.utils.imageUtil.ImageEncodeUtil;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.service.sys.IProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-05 15:22
 **/

@Api(tags = "流程接口")
@RequestMapping("/process")
@Controller
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IProcessService processServiceImpl;
    @Autowired
    private ProcessEngine processEngine;

    @ApiOperation("流程页面接口")
    @GetMapping("/processList")
    public String processList(){
        return "sys/process/processList";
    }
    @ApiOperation("流程显示页面接口")
    @GetMapping("/processShow")
    public String processShow(){
        return "sys/process/processShow";
    }
    @ApiOperation("流程分页接口")
    @ResponseBody
    @GetMapping("/getProcessPage")
    public SystemResponse getProcessPage(Page page){
        try {
            Map map =  processServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }
    @ApiOperation("流程显示接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "流程定义id",required = true),
            @ApiImplicitParam(name = "ext", value = "显示类型",required = true)
    })
    @ResponseBody
    @PostMapping("/showXmlOrPng")
    public SystemResponse showXmlOrPng(@RequestParam("deploymentId")String deploymentId, @RequestParam("ext")String ext){
        if (StringUtils.isEmpty(deploymentId) || StringUtils.isEmpty(ext)){
            return new SystemResponse().fail().message("缺少参数");
        }
        InputStream in = null;
        String result ="";
        try {
            ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
            if ("png".equalsIgnoreCase(ext)){
                in = repositoryService.getProcessDiagram(processDefinition.getId());
                //不压缩
                // InputStream inputStream = ImageEncodeUtil.compressFile(in,"png");
                result = ImageEncodeUtil.getBase64FromInputStream(in);
            }else if ("xml".equalsIgnoreCase(ext)){
                in = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
                try {
                    byte[] bytes = new byte[0];
                    bytes = new byte[in.available()];
                    in.read(bytes);
                    result = new String(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new SystemResponse().fail().message(e.getMessage());
                }
            }
        }finally{
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new SystemResponse().success().data(result);
    }

    @ApiOperation("删除流程接口")
    @ApiImplicitParam(name = "id", value = "流程部署id",required = true)
    @ResponseBody
    @PostMapping("/delProcess")
    public SystemResponse delProcess(@RequestParam String id){
        try {
            processEngine.getRepositoryService().deleteDeployment(id); // 流程部署ID
            return new SystemResponse().success().delMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
