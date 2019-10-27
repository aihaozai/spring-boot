package com.example.myproject.common.HandlerInterceptor;

import com.example.myproject.common.annotation.Permission;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class SystemInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Permission permission = method.getAnnotation(Permission.class);
            if (permission != null) {
                String[] roles = permission.value();
                String login = "login";
                for (String role : roles) {
                    if (role.equals(login)) {//判断用户是否有权限访问
                        return true;
                    }
                }
            }else {
                log.warn(method.getName()+"方法没有添加权限注解");
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
