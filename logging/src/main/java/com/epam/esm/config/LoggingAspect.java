package com.epam.esm.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {
    private static final Logger LOGGER = LogManager.getLogger();

    @Pointcut("execution(* com.epam.esm.service.*.*(..))")
    public void performance() {
    }

    @Before("performance()")
    public void beforeServiceMethodInvocation(JoinPoint joinPoint) {
        LOGGER.log(Level.INFO, "Invocation of method " + joinPoint.getSignature());
    }
}
