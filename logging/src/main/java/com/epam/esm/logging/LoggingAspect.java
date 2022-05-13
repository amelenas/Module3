package com.epam.esm.logging;

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
    private static final String MESSAGE = "Invocation of method %s";

    @Pointcut(value = "execution(* com.epam.esm.service.*.*(..))")
    public void performance() {
    }

    @Before("performance()")
    public void beforeServiceMethodInvocation(JoinPoint joinPoint) {
        LOGGER.log(Level.INFO, String.format(MESSAGE, joinPoint.getSignature()));
    }
}
