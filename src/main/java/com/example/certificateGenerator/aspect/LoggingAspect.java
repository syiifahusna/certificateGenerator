package com.example.certificateGenerator.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.certificateGenerator.controller.*.*(..))")
    private void poincutController(){}

   @Pointcut("execution(* com.example.certificateGenerator.service.*.*(..))")
    private void poincutService(){}

    @Before("poincutController()")
    public void logController(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();//get method name
        logger.info("Executing Controller Method: " + methodName);
    }

    @AfterThrowing( pointcut = "poincutController()",
                    throwing = "exception")
    public void afterControllerThrowsException(JoinPoint joinPoint,Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Executed controller method: " + methodName + " throw error : " + exception.getMessage());
    }

    @Before("poincutService()")
    public void logService(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();//get method name
        logger.info("Executing Service Method: " + methodName);
    }

    @AfterThrowing( pointcut = "poincutService()",
            throwing = "exception")
    public void afterServiceThrowsException(JoinPoint joinPoint,Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Executed service method: " + methodName + " throw error : " + exception.getMessage());
    }
}
