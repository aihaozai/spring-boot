package com.example.myproject.controller.activiti;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.entity.view.UserLoginView;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-07 15:01
 **/
@Slf4j
@Controller
@RequestMapping("/actProcess")
public class ActProcessController extends TaskController{

    @Autowired
    private AllDao.ActHiTaskProcessDao hiTaskProcessDao;

    @ResponseBody
    @PostMapping("/startProcess")
    public SystemResponse startProcess(@RequestBody JSONObject jsonObject){
        try {
            UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
            Map<String, Object> variables = new HashMap<>();
            variables.put("userId", user.getId());
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(jsonObject.getString("key"),variables);
            if(StringUtils.isNotEmpty(processInstance.getId())){
                TaskService taskService = processEngine.getTaskService();
                Task task = taskService.createTaskQuery()
                        .processDefinitionKey(jsonObject.getString("key")).processInstanceId(processInstance.getId())
                        .singleResult();
                //保存记录
                saveActHiTaskProcess(jsonObject,task,processInstance.getId(),user.getId());
                //经办人节点
                taskService.addComment(task.getId(),processInstance.getId(),jsonObject.getString("reason"));
                TaskDefinition nextTaskGroup = getNextTaskInfo(processInstance.getId());
                if(nextTaskGroup==null){
                    return new SystemResponse().fail();
                }
                JSONObject data = new JSONObject();
                data.put("taskId",task.getId());
                data.put("level",nextTaskGroup.getDescriptionExpression().getExpressionText());
                return new SystemResponse().success().approveSuccess().data(data);
            }
            return new SystemResponse().fail().message("流程key为空");
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

//    @ResponseBody
//    @PostMapping("/startProcess")
//    public SystemResponse startProcess(@RequestBody JSONObject jsonObject){
//        try {
//            UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("userId", user.getId());
//            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//            RuntimeService runtimeService = processEngine.getRuntimeService();
//            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(jsonObject.getString("key"),variables);
//            if(StringUtils.isNotEmpty(processInstance.getId())){
//                TaskService taskService = processEngine.getTaskService();
//                Task task = taskService.createTaskQuery()
//                        .processDefinitionKey(jsonObject.getString("key")).processInstanceId(processInstance.getId())
//                        .singleResult();
//                //保存记录
//                saveActHiTaskProcess(jsonObject,task,processInstance.getId(),user.getId(),true);
//                //经办人节点
//                taskService.addComment(task.getId(),processInstance.getId(),jsonObject.getString("reason"));
//                //完成当前节点设置下一个节点会签
//                variables.put("userId", jsonObject.getString("assignee"));
//                taskService.complete(task.getId(),variables);
//                //下一个节点
//                task = taskService.createTaskQuery()
//                        .processDefinitionKey(jsonObject.getString("key")).processInstanceId(processInstance.getId())
//                        .singleResult();
//                //保存记录
//                saveActHiTaskProcess(jsonObject,task,processInstance.getId(),user.getId(),false);
//                return new SystemResponse().success().approveSuccess();
//            }
//            return new SystemResponse().fail().message("流程key为空");
//        }catch (Exception e){
//            e.printStackTrace();
//            return new SystemResponse().fail().message(e.getMessage());
//        }
//    }

    private void saveActHiTaskProcess(JSONObject jsonObject,Task task,String processInstanceId,String userId){
        //保存记录
        ActHiTaskProcess actHiTaskProcess = new ActHiTaskProcess();
        actHiTaskProcess.setTableId(jsonObject.getString("tableId"));
        actHiTaskProcess.setProcessId(processInstanceId);
        actHiTaskProcess.setProcessKey(jsonObject.getString("key"));
        actHiTaskProcess.setId(task.getId());
        actHiTaskProcess.setTaskName(task.getName());
        actHiTaskProcess.setStartTime( new Timestamp(new Date().getTime()));
        actHiTaskProcess.setLaunchId(userId);
        actHiTaskProcess.setOpinion(jsonObject.getString("reason"));
        actHiTaskProcess.setAssignee(userId);
        actHiTaskProcess.setStatus("N");    //Y完成 N未完成
        actHiTaskProcess.setUrl(jsonObject.getString("url"));
        hiTaskProcessDao.save(actHiTaskProcess);
    }
}
