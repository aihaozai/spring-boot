package com.example.myproject.common.HandlerInterceptor;

import com.example.myproject.common.FileListener.FileListenerAdaptor;
import com.example.myproject.common.FileListener.FileMonitorUtil;
import com.example.myproject.common.annotation.FileMonitor;
import com.example.myproject.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
public class SystemInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequiresPermissions permission = method.getAnnotation(RequiresPermissions.class);
            FileMonitor monitor = method.getAnnotation(FileMonitor.class);
            Annotation[] annotations = method.getDeclaredAnnotations();
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
}
