package com.example.myproject.controller;

import com.example.myproject.common.activiti.DefaultProcessDiagramGenerator;
import com.example.myproject.common.imageUtil.ImageEncodeUtil;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IProcessService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-05 15:22
 **/
@RequestMapping("/process")
@Controller
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IProcessService processServiceImpl;
    @Autowired
    private ProcessEngine processEngine;

    @RequestMapping("/processList")
    public String processList(){
        return "process/processList";
    }

    @RequestMapping("/processShow")
    public String processShow(){
        return "process/processShow";
    }

    @ResponseBody
    @RequestMapping("/getProcessPage")
    public SystemResponse getProcessPage(Page page){
        try {
            Map map =  processServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }
    @ResponseBody
    @RequestMapping("/showXmlOrPng")
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

    @ResponseBody
    @RequestMapping("/delProcess")
    public SystemResponse delProcess(@RequestParam String id){
        try {
            processEngine.getRepositoryService().deleteDeployment(id); // 流程部署ID
            return new SystemResponse().success().message("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }


}
