package com.example.myproject.controller;


import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.activiti.engine.repository.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/")
public class UserControllertest {
//    @Autowired
//    private UserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/user")
    public ModelAndView user(){
        List list = new ArrayList();
        User user = new User();
        User user1 = new User();
        list.add(user);
        list.add(user1);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user");
        mv.addObject("user",list);
        return mv;
    }
    @ResponseBody
    @GetMapping("/user1")
    public User user1(){
        User user = new User();

        return user;
    }
    @GetMapping("/up")
    public String upload(){
        return "/layout";
    }
    @GetMapping("/hi")
    @ResponseBody
    public void hi(Model model){
//        Map<String,Object> map = model.asMap();
//        Set<String> keyset = map.keySet();
//        Iterator<String> iterator = keyset.iterator();
//        while (iterator.hasNext()){
//            String key = iterator.next();
//            Object value = map.get(key);
//            System.out.println(key+">>>>>"+value);
//        }
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


//    @GetMapping("/look")
//    @ResponseBody
//    public void look (){
//        System.out.println( "调用流程存储服务，查询部署数量：" + repositoryService.createDeploymentQuery().count());
//
//    }
//    @GetMapping("/g")
//    public String getUserById(Integer id){
//        return  userService.getUserById(id);
//    }
//    @GetMapping("/d")
//    public void deleteUserById(Integer id){
//        userService.deleteUserById(id);
//    }
//
//    @GetMapping("/getUser")
//    @ResponseBody
//    public void getUser(){
//        List list = userService.getAllUser();
//        System.out.println(list);
//    }
//    @GetMapping("/addUser")
//    @ResponseBody
//    public void addUser(){
//        userService.adduser();
//    }
//    @GetMapping("/findAllUser")
//    @ResponseBody
//    public void findAllUser(){
//        PageRequest pageRequest = PageRequest.of(0,1);
//        Page<JpaUser> page = userService.getUserByPage(pageRequest);
//        System.out.println(page.getTotalPages());
//        System.out.println(page.getTotalElements());
//        System.out.println(page.getContent());
//        System.out.println((page.getNumber()+1));
//        System.out.println(page.getNumberOfElements());
//        System.out.println(page.getSize());
//    }

//    @GetMapping("/findAllUserTwo")
//    @ResponseBody
//    public void findAllUserMoreDataSource(){
//       userService.getAllUserMoreDataSource();
//    }
//    @GetMapping("/findAllUserTwo1")
//    @ResponseBody
//    public void findAllUserMoreDataSource1(){
//        userService.getAllUserMoreDataSourceMybatis();
//    }
//
//    @GetMapping("/findAllUserTwo2")
//    @ResponseBody
//    public void getAllUserMoreDataSourceJpa(){
//        userService.getAllUserMoreDataSourceJpa();
//    }

//    @GetMapping("/redis")
//    @ResponseBody
//    public void showRedis(){
//        ValueOperations<String,String> ops1 = stringRedisTemplate.opsForValue();
//        ops1.set("name","me");
//        String name = ops1.get("name");
//        System.out.println(name);
//        ValueOperations ops2 = redisTemplate.opsForValue();
//        NewUser newUser = new NewUser();
//        newUser.setUsername("ddddd");
//        newUser.setPassword("qqqq");
//        ops2.set("user",newUser);
//        NewUser user = (NewUser)ops2.get("user");
//        System.out.println(user);

    }

//    @GetMapping("/hello")
//    @ResponseBody
//    public String hello(){
//        return "Hello";
//        }


