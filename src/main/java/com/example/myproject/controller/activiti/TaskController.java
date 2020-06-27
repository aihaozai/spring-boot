package com.example.myproject.controller.activiti;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.HandlerInterceptor.WebSocketHandler;
import com.example.myproject.common.activiti.DefaultProcessDiagramGenerator;
import com.example.myproject.common.utils.imageUtil.ImageEncodeUtil;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.service.sys.IActHiTaskProcessService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-15 13:43
 **/
@Api(tags = "任务接口")
@RequestMapping("/task")
@RestController
public class TaskController extends WebSocketHandler{

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IActHiTaskProcessService hiTaskProcessServiceImpl;

    private static final Integer AGREE = 0;
    private static final Integer REJECT = 1;

    @ApiOperation("显示审批流程图接口")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例id",required = true)
    @PostMapping("/showTaskImage")
    public SystemResponse showTaskImage(String processInstanceId)throws Exception {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String procDefId = processInstance.getProcessDefinitionId();

        // 当前活动节点、活动线
        List<String> activeActivityIds = new ArrayList<>(), highLightedFlows;
        //所有的历史活动节点
        List<String> highLightedFinishes = new ArrayList<>();

        // 如果流程已经结束，则得到结束节点
        if (!isFinished(processInstanceId)) {
            // 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        }

        // 获得历史活动记录实体（通过启动时间正序排序，不然有的线可以绘制不出来）
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();
        //获取任务历史记录
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .asc()
                .list();
        //获取不重复的节点
        List<HistoricActivityInstance> activityInstancesList = new ArrayList<>();
        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            if(historicActivityInstance.getTaskId()!=null){
                for(HistoricTaskInstance historicTaskInstance : htiList){
                    if(historicActivityInstance.getTaskId().equals(historicTaskInstance.getId())){
                        activityInstancesList.add(historicActivityInstance);
                        continue;
                    }
                }
            }else {
                activityInstancesList.add(historicActivityInstance);
            }
        }

        for(HistoricActivityInstance historicActivityInstance : activityInstancesList){
            highLightedFinishes.add(historicActivityInstance.getActivityId());
        }
        // 计算活动线
        highLightedFlows = getHighLightedFlows(
                (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(procDefId),
                activityInstancesList);
        String result ="";
        if (null != activeActivityIds) {
            InputStream imageStream = null;
            try {
                // 获得流程引擎配置
                ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
                // 根据流程定义ID获得BpmnModel
                BpmnModel bpmnModel = repositoryService
                        .getBpmnModel(procDefId);
                // 输出资源内容到相应对象
                imageStream = new DefaultProcessDiagramGenerator().generateDiagram(
                        bpmnModel,
                        "png",
                        highLightedFinishes,//所有活动过的节点，包括当前在激活状态下的节点
                        activeActivityIds,//当前为激活状态下的节点
                        highLightedFlows,//活动过的线
                        "simsun",
                        "simsun",
                        "simsun",
                        processEngineConfiguration.getClassLoader(),
                        1.0);

                result = ImageEncodeUtil.getBase64FromInputStream(imageStream);
            } finally {
                if(imageStream != null){
                    imageStream.close();
                }
            }
        }
        return new SystemResponse().success().data(result);
    }

    @ApiOperation("显示任务回退接口")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例id",required = true)
    @PostMapping("/showTaskBack")
    public SystemResponse showTaskBack(String processInstanceId){
        //获取已完成的任务历史记录
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByTaskCreateTime()
                .desc()
                .list();
        if (htiList.size()>0){
            return new SystemResponse().success();
        }else {
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("显示流程时间线接口")
    @ResponseBody
    @PostMapping("/showFlowLine")
    public SystemResponse showFlowLine(@RequestBody JSONObject jsonObject){
        try {
            PageHelper.orderBy("end_time asc");
            return new SystemResponse().success().data(hiTaskProcessServiceImpl.findAll(jsonObject));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("同意任务接口")
    @ResponseBody
    @PostMapping("/agreeTask")
    public SystemResponse agreeTask(@RequestBody JSONObject jsonObject){
        try {
            ActHiTaskProcess actHiTaskProcess = hiTaskProcessServiceImpl.findByTableId(jsonObject.getString("taskId"));
            // 完成任务
            taskService.addComment(actHiTaskProcess.getId(), actHiTaskProcess.getProcessId(), jsonObject.getString("comment"));
            Map<String, Object> variables = new HashMap<>();
            variables.put("userId",jsonObject.getString("assignee"));
            taskService.complete(actHiTaskProcess.getId(),variables);
            hiTaskProcessServiceImpl.completeActHiTaskProcess(actHiTaskProcess.getId(),jsonObject.getString("comment"),AGREE);
            ActHiTaskProcess hiTaskProcess = getActHiTaskProcess(actHiTaskProcess,actHiTaskProcess.getProcessId());
            hiTaskProcess.setAssignee(jsonObject.getString("assignee"));
            hiTaskProcessServiceImpl.saveActHiTaskProcess(hiTaskProcess);
            sendMessageToUser(hiTaskProcess.getAssignee(), new TextMessage("你有一条新的待办任务"));
            return new SystemResponse().success().data("已同意并发起下一个");
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("完成任务接口")
    @ResponseBody
    @PostMapping("/completeTask")
    public SystemResponse completeTask(@RequestBody JSONObject jsonObject){
        try {
            ActHiTaskProcess actHiTaskProcess = hiTaskProcessServiceImpl.findByTableId(jsonObject.getString("taskId"));
            // 完成任务
            taskService.addComment(actHiTaskProcess.getId(), actHiTaskProcess.getProcessId(), jsonObject.getString("comment"));
            taskService.complete(actHiTaskProcess.getId());
            hiTaskProcessServiceImpl.completeActHiTaskProcess(actHiTaskProcess.getId(),jsonObject.getString("comment"),AGREE);
            return new SystemResponse().success().message("已同意").data(actHiTaskProcess.getProcessKey());
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("回退任务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id",required = true),
            @ApiImplicitParam(name = "comment", value = "意见",required = true)
    })
    @ResponseBody
    @PostMapping("/rejectTask")
    public SystemResponse rejectTask(String taskId,String comment){
        try {
            ActHiTaskProcess actHiTaskProcess = hiTaskProcessServiceImpl.findByTableId(taskId);
            //判断当前任务是否完成
            Task task = taskService.createTaskQuery().taskId(actHiTaskProcess.getId()).singleResult();
            if(task==null) {
                return new SystemResponse().fail().message("流程未启动或已执行完成，无法撤回");
            }
            //获取已完成的任务历史记录
            List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(actHiTaskProcess.getProcessId())
                    .finished()
                    .orderByTaskCreateTime()
                    .desc()
                    .list();
            HistoricTaskInstance hisTask = htiList.get(0);
            //获取上一活动的节点id
            String taskNodeId = hisTask.getTaskDefinitionKey();

            ProcessDefinitionImpl processDefinitionImpl = (ProcessDefinitionImpl) repositoryService.getProcessDefinition(hisTask.getProcessDefinitionId());

            // 取得上一步活动
            ActivityImpl currActivity = processDefinitionImpl.findActivity(taskNodeId);

            //获取节点进口
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();

            // 取得当前待办活动节点
            ActivityImpl execActivity = processDefinitionImpl.findActivity(task.getTaskDefinitionKey());

            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            List<PvmTransition> pvmTransitionList = execActivity.getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            //把进口当做出口，重新建立通道
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            TransitionImpl newTransition = execActivity.createOutgoingTransition();
            newTransition.setDestination(currActivity);
            newTransitions.add(newTransition);
            // 完成任务
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
            Map<String, Object> variables = new HashMap<>();
            variables.put("userId", hisTask.getAssignee());
            taskService.complete(task.getId(),variables);
            //删除历史、此处执行这两行代码在ACT_HI_TASKINST表中是看不到撤回记录的，但是在ACT_HI_ACTINST表中能看到全部记录
            historyService.deleteHistoricTaskInstance(task.getId());
            historyService.deleteHistoricTaskInstance(hisTask.getId());
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                execActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
            hiTaskProcessServiceImpl.completeActHiTaskProcess(taskId,comment,REJECT);
            ActHiTaskProcess hiTaskProcess = getActHiTaskProcess(actHiTaskProcess,actHiTaskProcess.getProcessId());
            hiTaskProcess.setAssignee(hisTask.getAssignee());
            hiTaskProcessServiceImpl.saveActHiTaskProcess(hiTaskProcess);
            return new SystemResponse().success().message("回退成功");
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("检查是否完成接口")
    @ApiImplicitParam(name = "taskId", value = "任务id",required = true)
    @ResponseBody
    @PostMapping("/checkTask")
    public SystemResponse checkTask(String taskId){
        try {
            ActHiTaskProcess actHiTaskProcess = hiTaskProcessServiceImpl.findByTableId(taskId);
            if(actHiTaskProcess.getStatus().equals("Y")){
                return new SystemResponse().success();
            }
            return new SystemResponse().fail();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    private boolean isFinished(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count() > 0;
    }

    /**
     * @description: 获取流程应该高亮的线
     * @param processDefinitionEntity 流程定义实例
     * @param historicActivityInstances 流程活动节点实例
     * @return java.util.List<java.lang.String>
     */
    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,List<HistoricActivityInstance> historicActivityInstances) {

        List<String> highFlows = new ArrayList<>();// 用以保存高亮的线flowId
        List<String> highActivitiImpl = new ArrayList<>();

        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            highActivitiImpl.add(historicActivityInstance.getActivityId());
        }

        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstance.getActivityId());
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
            // 对所有的线进行遍历
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                if (highActivitiImpl.contains(pvmActivityImpl.getId())) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }

        return highFlows;
    }

    private ActHiTaskProcess getActHiTaskProcess(ActHiTaskProcess actHiTaskProcess,String processInstanceId){
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId)
                .singleResult();
        ActHiTaskProcess hiTaskProcess = new ActHiTaskProcess();
        hiTaskProcess.setId(task.getId());
        hiTaskProcess.setProcessId(processInstanceId);
        hiTaskProcess.setProcessKey(actHiTaskProcess.getProcessKey());
        hiTaskProcess.setStatus("N");
        hiTaskProcess.setTableId(actHiTaskProcess.getTableId());
        hiTaskProcess.setTaskName(task.getName());
        hiTaskProcess.setLaunchId(actHiTaskProcess.getAssignee());
        hiTaskProcess.setUrl(actHiTaskProcess.getUrl());
        hiTaskProcess.setStartTime( new Timestamp(new Date().getTime()));
        return hiTaskProcess;
    }

    @ApiOperation("得到下一个任务接口")
    @ApiImplicitParam(name = "taskId", value = "任务id",required = true)
    @ResponseBody
    @PostMapping("/getNextTask")
    public SystemResponse getNextTask(String taskId){
        try {
            ActHiTaskProcess actHiTaskProcess = hiTaskProcessServiceImpl.findByTableId(taskId);
            TaskDefinition nextTaskGroup = getNextTaskInfo(actHiTaskProcess.getProcessId());
            if(nextTaskGroup==null){
                return new SystemResponse().fail();
            }
            return new SystemResponse().success().data(nextTaskGroup.getDescriptionExpression().getExpressionText());
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    public TaskDefinition getNextTaskInfo(String processInstanceId){

        ProcessDefinitionEntity processDefinitionEntity = null;

        String id = null;

        TaskDefinition task = null;


        //获取流程发布Id信息
        String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();

        processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(definitionId);

        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //当前流程节点Id信息
        String activitiId = execution.getActivityId();

        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();

        //遍历所有节点信息
        for(ActivityImpl activityImpl : activitiList){
            id = activityImpl.getId();
            if (activitiId.equals(id)) {
                //获取下一个节点信息
//                TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
//                        .getTaskDefinition();

                //return taskDefinition;
                task = nextTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
                break;
            }
        }
        return task;
    }

    private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId){

        PvmActivity ac = null;

        Object s = null;

        // 如果遍历节点为用户任务并且节点不是当前节点信息
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            // 获取该节点下一个节点信息
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
                    .getTaskDefinition();
            return taskDefinition;
        } else {
            // 获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                ac = tr.getDestination(); // 获取线路的终点节点
                // 如果流向线路为排他网关
                if ("exclusiveGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();

                    // 如果网关路线判断条件为空信息
                    if (StringUtils.isEmpty(elString)) {
                        // 获取流程启动时设置的网关判断条件信息
                        elString = getGatewayCondition(ac.getId(), processInstanceId);
                    }

                    // 如果排他网关只有一条线路信息
                    if (outTransitionsTemp.size() == 1) {
                        return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
                                elString, processInstanceId);
                    } else if (outTransitionsTemp.size() > 1) { // 如果排他网关有多条线路信息
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
                            // 判断el表达式是否成立
                            if (isCondition(ac.getId(), StringUtils.trim(s.toString()), elString)) {
                                return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
                                        processInstanceId);
                            }
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                } else {
                    //开始或者结束节点
                    return null;
                }
            }
            return null;
        }
    }

    private String getGatewayCondition(String gatewayId, String processInstanceId) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        Object object= runtimeService.getVariable(execution.getId(), gatewayId);
        return object==null? "":object.toString();
    }

    private boolean isCondition(String key, String el, String value) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }



    /**
     * 驳回任务方封装
     * @param destinationTaskID 驳回的任务ID 目标任务ID
     * @param messageContent  驳回的理由
     * @param currentTaskID  当前正要执行的任务ID
     * @return 驳回结果 携带下个任务编号
     */
//    public SystemResponse rejectTask(String destinationTaskID, String currentTaskID, String messageContent) {
//        // region 目标任务实例 historicDestinationTaskInstance 带流程变量，任务变量
//        HistoricTaskInstance historicDestinationTaskInstance = historyService
//                .createHistoricTaskInstanceQuery()
//                .taskId(destinationTaskID)
//                .includeProcessVariables()
//                .includeTaskLocalVariables()
//                .singleResult();
//        // endregion
//        // region 正在执行的任务实例 historicCurrentTaskInstance 带流程变量，任务变量
//        HistoricTaskInstance historicCurrentTaskInstance = historyService
//                .createHistoricTaskInstanceQuery()
//                .taskId(currentTaskID)
//                .includeProcessVariables()
//                .includeTaskLocalVariables()
//                .singleResult();
//        // endregion
//        // 流程定义ID
//        String processDefinitionId = historicCurrentTaskInstance.getProcessDefinitionId();
//        // 流程实例ID
//        String processInstanceId = historicCurrentTaskInstance.getProcessInstanceId();
//        // 流程定义实体
//        ProcessDefinitionEntity processDefinition =
//                (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
//        // region 根据任务创建时间正序排序获取历史任务实例集合 historicTaskInstanceList 含流程变量，任务变量
//        List<HistoricTaskInstance> historicTaskInstanceList = historyService
//                .createHistoricTaskInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .includeProcessVariables()
//                .includeTaskLocalVariables()
//                .orderByTaskCreateTime()
//                .asc()
//                .list();
//        // endregion
//        // region 历史活动节点实例集合 historicActivityInstanceList
//        List<HistoricActivityInstance> historicActivityInstanceList =
//                historyService
//                        .createHistoricActivityInstanceQuery()
//                        .processInstanceId(processInstanceId)
//                        .orderByHistoricActivityInstanceStartTime()
//                        .asc()
//                        .list();
//        // endregion
//        // 获取目标任务的节点信息
//        ActivityImpl destinationActivity = processDefinition
//                .findActivity(historicDestinationTaskInstance.getTaskDefinitionKey());
//        // 定义一个历史任务集合，完成任务后任务删除此集合中的任务
//        List<HistoricTaskInstance> deleteHistoricTaskInstanceList = new ArrayList<>();
//        // 定义一个历史活动节点集合，完成任务后要添加的历史活动节点集合
//        List<HistoricActivityInstanceEntity> insertHistoricTaskActivityInstanceList = new ArrayList<>();
//        // 目标任务编号
//        Integer destinationTaskInstanceId = Integer.valueOf(destinationTaskID);
//        // 有序
//        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
//            Integer historicTaskInstanceId = Integer.valueOf(historicTaskInstance.getId());
//            if (destinationTaskInstanceId <= historicTaskInstanceId) {
//                deleteHistoricTaskInstanceList.add(historicTaskInstance);
//            }
//        }
//        // 有序
//        for (int i = 0; i < historicActivityInstanceList.size() - 1; i++) {
//            HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(i);
//            // 历史活动节点的任务编号
//            Integer historicActivityInstanceTaskId;
//            String taskId = historicActivityInstance.getTaskId();
//            if (taskId != null) {
//                historicActivityInstanceTaskId = Integer.valueOf(taskId);
//                if (historicActivityInstanceTaskId <= destinationTaskInstanceId) {
//                    insertHistoricTaskActivityInstanceList.add((HistoricActivityInstanceEntity) historicActivityInstance);
//                }
//            } else {
//                System.out.println(historicActivityInstance.getActivityType());
//                if (historicActivityInstance.getActivityType().equals(ProcessConstant.START_EVENT)) {
//                    insertHistoricTaskActivityInstanceList.add((HistoricActivityInstanceEntity) historicActivityInstance);
//                } else if (historicActivityInstance.getActivityType().equals(ProcessConstant.EXCLUSIVE_GATEWAY)) {
//                    insertHistoricTaskActivityInstanceList.add((HistoricActivityInstanceEntity) historicActivityInstance);
//                }
//            }
//        }
//        // 获取流程定义的节点信息
//        List<ActivityImpl> processDefinitionActivities = processDefinition.getActivities();
//        // 用于保存正在执行的任务节点信息
//        ActivityImpl currentActivity = null;
//        // 用于保存原来的任务节点的出口信息
//        PvmTransition pvmTransition = null;
//        // 保存原来的流程节点出口信息
//        for (ActivityImpl activity : processDefinitionActivities) {
//            if (historicCurrentTaskInstance.getTaskDefinitionKey().equals(activity.getId())) {
//                currentActivity = activity;
//                // 备份
//                pvmTransition = activity.getOutgoingTransitions().get(0);
//                // 清空当前任务节点的出口信息
//                activity.getOutgoingTransitions().clear();
//            }
//        }
//        // 执行流程转向
//        processEngine.getManagementService().executeCommand(
//                new RejectTaskCMD(historicDestinationTaskInstance, historicCurrentTaskInstance, destinationActivity));
//        // 获取正在执行的任务的流程变量
//        Map<String, Object> taskLocalVariables = historicCurrentTaskInstance.getTaskLocalVariables();
//        // 获取目标任务的流程变量，修改任务不自动跳过，要求审批
//        Map<String, Object> processVariables = historicDestinationTaskInstance.getProcessVariables();
//        // 获取流程发起人编号
//        Integer employeeId = (Integer) processVariables.get(ProcessConstant.PROCESS_START_PERSON);
//        processVariables.put(ProcessConstant.SKIP_EXPRESSION, false);
//        taskLocalVariables.put(ProcessConstant.SKIP_EXPRESSION, false);
//        // 设置驳回原因
//        taskLocalVariables.put(ProcessConstant.REJECT_REASON, messageContent);
//        // region 流程变量转换
//        // 修改下个任务的任务办理人
//        processVariables.put(ProcessConstant.DEAL_PERSON_ID, processVariables.get(ProcessConstant.CURRENT_PERSON_ID));
//        // 修改下个任务的任务办理人姓名
//        processVariables.put(ProcessConstant.DEAL_PERSON_NAME, processVariables.get(ProcessConstant.CURRENT_PERSON_NAME));
//        // 修改下个任务的任务办理人
//        taskLocalVariables.put(ProcessConstant.DEAL_PERSON_ID, processVariables.get(ProcessConstant.CURRENT_PERSON_ID));
//        // 修改下个任务的任务办理人姓名
//        taskLocalVariables.put(ProcessConstant.DEAL_PERSON_NAME, processVariables.get(ProcessConstant.CURRENT_PERSON_NAME));
//        // endregion
//        // 完成当前任务，任务走向目标任务
//        String nextTaskId = processService.completeTaskByTaskID(currentTaskID, processVariables, taskLocalVariables);
//        if (currentActivity != null) {
//            // 清空临时转向信息
//            currentActivity.getOutgoingTransitions().clear();
//        }
//        if (currentActivity != null) {
//            // 恢复原来的走向
//            currentActivity.getOutgoingTransitions().add(pvmTransition);
//        }
//        // 删除历史任务
//        for (HistoricTaskInstance historicTaskInstance : deleteHistoricTaskInstanceList) {
//            historyService.deleteHistoricTaskInstance(historicTaskInstance.getId());
//        }
//        // 删除活动节点
//        processEngine.getManagementService().executeCommand(
//                (Command<List<HistoricActivityInstanceEntity>>) commandContext -> {
//                    HistoricActivityInstanceEntityManager historicActivityInstanceEntityManager =
//                            commandContext.getHistoricActivityInstanceEntityManager();
//                    // 删除所有的历史活动节点
//                    historicActivityInstanceEntityManager
//                            .deleteHistoricActivityInstancesByProcessInstanceId(processInstanceId);
//                    // 提交到数据库
//                    commandContext.getDbSqlSession().flush();
//                    // 添加历史活动节点的
//                    for (HistoricActivityInstanceEntity historicActivityInstance : insertHistoricTaskActivityInstanceList) {
//                        historicActivityInstanceEntityManager.insertHistoricActivityInstance(historicActivityInstance);
//                    }
//                    // 提交到数据库
//                    commandContext.getDbSqlSession().flush();
//                    return null;
//                }
//        );
//        // 返回下个任务的任务ID
//        return ResponseResultUtil.success(nextTaskId);
//    }

}
