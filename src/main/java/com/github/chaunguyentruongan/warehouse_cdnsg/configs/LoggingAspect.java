package com.github.chaunguyentruongan.warehouse_cdnsg.configs;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Pointcut target to all Controllers and Services inside com.github.chaunguyentruongan.warehouse_cdnsg.modules package
    @Pointcut("within(com.github.chaunguyentruongan.warehouse_cdnsg.modules..*) && " +
              "(execution(* *..*Service.*(..)) || execution(* *..*Controller.*(..)))")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());

        log.debug("--> Executing: {}.{}() | Args: {}", className, methodName, args);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            
            // Format output safely to prevent large outputs in console logs
            String resultStr = result != null ? result.toString() : "null";
            if (resultStr.length() > 200) {
                resultStr = resultStr.substring(0, 200) + "... (truncated)";
            }
            
            log.debug("<-- Completed: {}.{}() | Time: {}ms | Result: {}", className, methodName, elapsedTime, resultStr);
            return result;
        } catch (Throwable e) {
            long elapsedTime = System.currentTimeMillis() - start;
            log.error("[!] Exception in {}.{}() | Time: {}ms | Cause: {}", 
                    className, methodName, elapsedTime, e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            throw e;
        }
    }
}
