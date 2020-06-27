package com.example.myproject.controller;


import com.example.myproject.common.annotation.RepeatedRequests;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.utils.SpringContextUtil;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.entity.business.ApplyLeave;
import com.example.myproject.entity.sys.User;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.entity.sys.view.UserLoginView;
import com.example.myproject.mapper.ActHiTaskProcessMapper;
import com.example.myproject.mapper.ApplyLeaveMapper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/")
public class UserControllertest {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private ActHiTaskProcessMapper actHiTaskProcessMapper;

    @ResponseBody
    @GetMapping("/page")
    public PageInfo<ActHiTaskProcess> findPage() {
//        PageHelper.startPage(1,1);
//        List<ActHiTaskProcess> list= actHiTaskProcessMapper.findAll();
//        PageInfo<ActHiTaskProcess> pageInfo = new PageInfo<>(list);
        return null;
    }

    @GetMapping("/user")
    public void user(){
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
        System.out.println(currentSessionId);
        List<User> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserLoginView user;
            SimplePrincipalCollection principalCollection;
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (UserLoginView) principalCollection.getPrimaryPrincipal();
            }
            System.out.println(session.getId());
        }
    }
    @GetMapping("/userdel")
    public void del(){
        Session session = sessionDAO.readSession("");
        session.setTimeout(0);
        session.stop();
        sessionDAO.delete(session);
    }
    @Autowired
    private AllDao.ApplyLeaveDao applyLeaveDao;

    @RepeatedRequests
    @ResponseBody
    @GetMapping("/apply")
    public SystemResponse applyLeave(){
        ActHiTaskProcess actHiTaskProcess = new ActHiTaskProcess();
        actHiTaskProcess.setStartTime(new Timestamp(new Date().getTime()));
        return new SystemResponse().success().data(actHiTaskProcess);
    }
    @RepeatedRequests(5000)
    @GetMapping("/up")
    public String upload(){
        return "/layout";
    }
    @Autowired
    private ApplyLeaveMapper applyLeaveMapper;

    @GetMapping("/hi")
    @ResponseBody
    @Transactional
    public void hi(){
        //SpringContextUtil.getBean(this.getClass()).hitest();
        hitest();
    }

    @Transactional
    public void hitest(){
        ApplyLeave  applyLeave = new ApplyLeave();
        applyLeave.setId("1");
        applyLeave.setLeaveType("3434");
        applyLeaveMapper.updateApplyLeave(applyLeave);
        int nym = 1/0;
        applyLeave.setApplyReason("1212");
        applyLeaveMapper.updateApplyLeave(applyLeave);
    }

    @GetMapping("/t1")
    @ResponseBody
    public void resend() {
        log.info("开始执行定时任务(投递消息)");
        rabbitTemplate.convertAndSend("topicExchange","topic.man","111",new CorrelationData(UUIDUtil.currentTime()));// 重新投递
        log.info("定时任务执行结束(投递消息)");
    }
    @GetMapping("/t2")
    @ResponseBody
    public void resend1() {
        log.info("开始执行定时任务(投递消息)");
        rabbitTemplate.convertAndSend("topicExchange","topic.woman","222");// 重新投递
        log.info("定时任务执行结束(投递消息)");
    }
    @GetMapping("/t3")
    @ResponseBody
    public void resend3() {
        log.info("开始执行定时任务(投递消息)");
        rabbitTemplate.convertAndSend("topicExchange","topic.kk","333",new CorrelationData(UUIDUtil.currentTime()));// 重新投递
        log.info("定时任务执行结束(投递消息)");
    }

    @GetMapping("/test")
    public String test() {
      return "test/test";
    }
    @PostMapping("/test1")
    public String test1() {
        return "test/test1";
    }
    @PostMapping("/test2")
    public String test2() {
        return "test/test2";
    }
    @ResponseBody
    @RequestMapping("/pro")
    public void pro1(){
        UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        Map<String, Object> variables = new HashMap<>();
//        List<String> assigneeList = new ArrayList<>();
//        assigneeList.add("haozai");
//        assigneeList.add("jen");
//        variables.put("userId", user.getUserId());
//        variables.put("assigneeList", assigneeList);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1");
        //4.输出实例的相关信息
        System.out.println("流程部署id:" + processInstance.getDeploymentId());//null
        System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());//holiday:1:4
        System.out.println("流程实例id:" + processInstance.getId());//2501
        System.out.println("流程活动id:" + processInstance.getActivityId());//null
        System.out.println(processEngine.getTaskService().toString());
    }
    @ResponseBody
    @PostMapping("/pro1")
    public void pro(String id){
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    TaskService taskService = processEngine.getTaskService();
    Task task = taskService.createTaskQuery()
            .processDefinitionKey("myProcess_1").processInstanceId(id)
//            .taskAssignee("f15bed17b788465c9c2317f155ccb5ef")
            .singleResult();
    System.out.println("流程实例ID:" + task.getProcessInstanceId());//2501
    System.out.println("任务ID:" + task.getId()); //2505
    System.out.println("任务负责人:" + task.getAssignee());//zhangsan
    System.out.println("任务名称:" + task.getName());//填写请假申请单
    }

    @ResponseBody
    @RequestMapping("/pro2")
    public void pro2(String id,String name,String comment){
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", "ggggg");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myProcess_1")
                .taskAssignee(name).processInstanceId(id)
                .singleResult();
        //4.处理任务,结合当前用户任务列表的查询操作的话,任务ID:task.getId()

        taskService.addComment(task.getId(),id,comment);
        taskService.complete(task.getId());
        //5.输出任务的id
        System.out.println(task.getId());
    }

    @ResponseBody
    @PostMapping("/set")
    public void set(String id){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String taskId = id;
        //指定认领的办理者
        String userId = "周芷若";
        processEngine.getTaskService().setAssignee(taskId, userId);

    }
    /**
     * 挂起任务
     * @param processId
     */
    @ResponseBody
    @PostMapping("/gq")
    public void suspendTaskByProcessId(String processId){
        runtimeService.suspendProcessInstanceById(processId);
    }
    @ResponseBody
    @PostMapping("/getnext")
    public void acvitivi(String taskId)
         {
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) processEngine.getRepositoryService()
        .getProcessDefinition(task.getProcessDefinitionId());
        String taskKey = task.getTaskDefinitionKey();
             System.out.println(taskKey);
        ActivityImpl activityImpl = pde.findActivity(taskKey);
        List<PvmTransition> pvms = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvm : pvms)
        {
        PvmActivity pvmActivity = pvm.getDestination();
        if (pvmActivity.getOutgoingTransitions().isEmpty())
        {
        System.out.println(pvmActivity.getId() + "," + pvmActivity.getProperty("type"));
        }

        }
 }


    @ResponseBody
    @PostMapping("/look")
    public void lo(){
        List<HistoricTaskInstance> historicActivityInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("cd8e22c844f7486080283c72c0f10f48")
                .finished()
                .orderByTaskCreateTime()
                .desc()
                .list();
//                List<HistoricActivityInstance> historicActivityInstanceList =
//                historyService
//                        .createHistoricActivityInstanceQuery()
//                        .processInstanceId("cd8e22c844f7486080283c72c0f10f48")
//                        .orderByHistoricActivityInstanceStartTime()
//                        .asc()
//                        .list();
                for(HistoricTaskInstance h:historicActivityInstanceList){
                    System.out.println(h.toString());
                }
    }
    @ResponseBody
    @PostMapping("/back")
    public void ba(String instId,String hisTaskId,String procInstId){
        //instId 为流程表单id  business_key
//hisTaskId  已办任务中的任务历史id(该id为最后一个已办节点，也可以通过 instId来获取list遍历)
        Task task = taskService.createTaskQuery().taskId(instId).singleResult();
        if(task==null) {
            System.out.println("流程未启动或已执行完成，无法撤回");
            return;
        }
//获取当前用户id
        UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
        String userId = "f15bed17b788465c9c2317f155ccb5ef";

//获取已完成的任务历史记录
		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(procInstId)
				.finished()
				.orderByTaskCreateTime()
				.desc()
				.list();
//		//判断上一节点处理人是否为当前用户
		HistoricTaskInstance hisTask1 = null;
		if(htiList != null && htiList.size()>0) {
			HistoricTaskInstance hisTaskObj = htiList.get(0);
			if(userId.equals(hisTaskObj.getAssignee())) {
				hisTask1 = hisTaskObj;
			}
		}

        HistoricTaskInstance hisTask = historyService.createHistoricTaskInstanceQuery().taskId(hisTaskId).singleResult();

        if(null==hisTask || !userId.equals(hisTask.getAssignee())) {
            System.out.println("该任务非当前用户提交，无法撤回");
            return;
        }
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
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        for (Task taskObj : tasks) {
  Map<String,Object> currentVariables = new HashMap<String,Object>();
            //此处可以根据也许需求获取上一节点处理人，本人使用的是自己自定义的动态表单
  List<String> userList = new ArrayList<>();
  userList.add("0122");
  currentVariables.put("transactorIdList",userList);
    
  Authentication.setAuthenticatedUserId(userId);
  taskService.addComment(taskObj.getId(), taskObj.getProcessInstanceId(), "撤回");
    
  taskService.complete(taskObj.getId(), currentVariables);
  //删除历史、此处执行这两行代码在ACT_HI_TASKINST表中是看不到撤回记录的，但是在ACT_HI_ACTINST表中能看到全部记录
            historyService.deleteHistoricTaskInstance(taskObj.getId());
            historyService.deleteHistoricTaskInstance(hisTask.getId());
        }
// 恢复方向
        for (TransitionImpl transitionImpl : newTransitions) {
  execActivity.getOutgoingTransitions().remove(transitionImpl);
        }
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
  pvmTransitionList.add(pvmTransition);
        }

//后面需要处理的就是 已办的流程历史回显 和 已办的流程表单回显，可根据自己业务需求来调整。
    }
}



