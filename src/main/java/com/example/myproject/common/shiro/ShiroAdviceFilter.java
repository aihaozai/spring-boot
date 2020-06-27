package com.example.myproject.common.shiro;

import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-09 17:24
 **/
public class ShiroAdviceFilter extends AdviceFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws
            Exception {
        System.out.println("====预处理/前置处理");
        return true;//返回 false 将中断后续拦截器链的执行
    }
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws
            Exception {
        System.out.println("====后处理/后置返回处理");
    }
    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception
            exception) throws Exception {
        System.out.println("====完成处理/后置最终处理");
    }
}
