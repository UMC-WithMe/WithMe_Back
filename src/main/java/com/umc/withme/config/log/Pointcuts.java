package com.umc.withme.config.log;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.umc.withme.controller..*(..))")
    public void controllerPoint(){}

    @Pointcut("execution(* com.umc.withme.service..*(..))")
    public void servicePoint(){}

    @Pointcut("execution(* com.umc.withme.repository..*(..))")
    public void repositoryPoint(){}
}
