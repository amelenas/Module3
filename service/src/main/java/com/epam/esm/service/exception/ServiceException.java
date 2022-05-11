package com.epam.esm.service.exception;


public class ServiceException extends RuntimeException{

    private final ExceptionHandler exceptionHandler;

    public ServiceException(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

}
