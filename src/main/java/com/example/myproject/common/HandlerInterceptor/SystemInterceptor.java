package com.example.myproject.common.HandlerInterceptor;

import com.example.myproject.common.FileListener.FileListenerAdaptor;
import com.example.myproject.common.FileListener.FileMonitorUtil;
import com.example.myproject.common.annotation.FileMonitor;
import com.example.myproject.common.annotation.RepeatedRequests;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SystemInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequiresPermissions permission = method.getAnnotation(RequiresPermissions.class);
            FileMonitor monitor = method.getAnnotation(FileMonitor.class);
            RepeatedRequests repeatedRequests = method.getAnnotation(RepeatedRequests.class);
            if (permission == null) {
                log.warn(method.getName()+"方法没有添加访问权限注解");
            }
            if (monitor != null) {
                log.info("正在访问FTP上传接口");
                try {
                    if(FileMonitorUtil.monitor!=null&&!FileMonitorUtil.running){
                        log.info("文件监测服务已关闭...正在开启");
                        FileMonitorUtil.start();
                    }else if(FileMonitorUtil.monitor==null){
                        log.info("文件监测服务已关闭...正在开启");
                        FileListenerAdaptor listenerAdaptor = new FileListenerAdaptor();
                        listenerAdaptor.startFileLister(Long.parseLong(PropertiesUtil.getProperty("system.listener.time")),PropertiesUtil.getProperty("system.listener.path"),Long.parseLong(PropertiesUtil.getProperty("system.listener.overtime")),PropertiesUtil.getProperty("system.listener.overopen"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (repeatedRequests != null) {
                if(repeatDataValidator(request,repeatedRequests.value())){
                    //请求数据相同
                    log.info("重复请求路径：{"+request.getRequestURI()+"}");
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    response.getWriter().write(new SystemResponse().fail().message("请勿重复请求").toString());
                    response.getWriter().close();
                    return false;
                }else {
                    //如果不是重复相同数据
                    return true;
                }
            }
        }
        log.info("请求路径：{"+request.getRequestURI()+"}");
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    /**
     * 验证同一个url数据是否相同提交,相同返回true
     */
    public boolean repeatDataValidator(HttpServletRequest request, long time){
        String redisKey = request.getRequestURI() + request.getHeader("token");
        if(!redisTemplate.hasKey(redisKey)){
            //如果上一个数据为null,表示还没有请求
            redisTemplate.opsForValue().set(redisKey,"yes",time, TimeUnit.MILLISECONDS);
            return false;
        }else{
            //否则，已经有过请求
            return true;
        }
    }
}
