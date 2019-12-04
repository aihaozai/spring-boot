package com.example.myproject.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    
    @Pointcut("execution(* com.example.myproject.service.*.*(..))")
    public void pcl(){

    }
    @Before(value = "pcl()")
    public void before(JoinPoint jp){
        String name = jp.getSignature().getName();
        logger.info(name+"方法开始执行...");
    }
    @After(value = "pcl()")
    public void after(JoinPoint jp){
        String name = jp.getSignature().getName();
        logger.info(name+"方法执行结束...");
    }
    @AfterReturning(value = "pcl()",returning = "result")
    public void afterReturning(JoinPoint jp,Object result){
        String name = jp.getSignature().getName();
        logger.info(name+"方法返回值:"+result);
    }

    @AfterThrowing(value = "pcl()",throwing = "e")
    public void afterThrowing(JoinPoint jp,Exception e){
        String name = jp.getSignature().getName();
        logger.info(name+"方法抛出异常:"+e.getMessage());
    }
    @Around("pcl()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}
