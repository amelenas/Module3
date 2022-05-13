package com.epam.esm.controller.exception;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.controller.exception.ExceptionCodes.*;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
        Iterator<Map.Entry<String, Object[]>> exceptions = ex.getExceptionHandler().
                getExceptionMessages().entrySet().iterator();
        StringBuilder details = new StringBuilder();
        while (exceptions.hasNext()) {
            Map.Entry<String, Object[]> exception = exceptions.next();
            String message = Translator.toLocale(exception.getKey());
            String detail = String.format(message, exception.getValue());
            details.append(detail).append(' ');
        }
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST_EXCEPTION.toString(), details.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR_EXCEPTION.toString(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("malformed.json", ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorResponse errorResponse;
        errorResponse = new ErrorResponse(Translator.toLocale("exception.badRequest"), errors.toString());

        return new ResponseEntity<>(errorResponse, status);
    }
}
