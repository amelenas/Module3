package com.epam.esm.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException{
        httpServletResponse.getWriter().print(String.format("error.message",
                e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
