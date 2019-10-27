package com.example.myproject.common.customException;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *@author Jen
 *@Point 全局异常
 */
@ControllerAdvice
public class CustomExceptionHandler {
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationExceptionException(HttpServletRequest request, HttpServletResponse response, AuthorizationException e){
        return "你没有该权限！" ;
    }
}
