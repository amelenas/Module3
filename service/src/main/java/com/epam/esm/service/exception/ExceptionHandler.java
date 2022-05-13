package com.epam.esm.service.exception;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionHandler {

    private final Map<String, Object[]> exceptionMessages;

    public ExceptionHandler() {
        exceptionMessages = new HashMap<>();
    }

    public void addException(String messageCode, Object... arguments) {
        exceptionMessages.put(messageCode, arguments);
    }

    public void clean() {
        exceptionMessages.clear();
    }

    public Map<String, Object[]> getExceptionMessages() {
        return exceptionMessages;
    }
}
