package com.epam.esm.service.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandler {

    private final Map<String, Object[]> exceptionMessages;

    public ExceptionHandler() {
        exceptionMessages = new HashMap<>();
    }

    public void addException(String messageCode, Object... arguments) {
        exceptionMessages.put(messageCode, arguments);
    }

    public Map<String, Object[]> getExceptionMessages() {
        return exceptionMessages;
    }
}
