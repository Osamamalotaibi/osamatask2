//package com.example.taskapi.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//@Aspect
//@Component
//@Slf4j
//public class LoggingAspect {
//
//    @Pointcut("within(com.example.taskapi.controller..*)")
//    public void controllerMethods() {}
//
//    @Pointcut("within(com.example.taskapi.service.impl..*)")
//    public void serviceMethods() {}
//
//    @Around("controllerMethods()")
//    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
//        String method = pjp.getSignature().toShortString();
//        log.debug(" BEFORE {}", method);
//
//        long start = System.nanoTime();
//        Object result = pjp.proceed();
//        long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
//
//        log.info(" {} took {} ms", method, ms);
//        log.debug(" AFTER {}", method);
//
//        return result;
//    }
//
//    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
//    public void logAfterThrowing(JoinPoint jp, Exception ex) {
//        String method = jp.getSignature().toShortString();
//        log.error(" Method {} threw exception: {}", method, ex.getClass().getSimpleName(), ex);
//    }
//}
