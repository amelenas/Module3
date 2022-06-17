package com.epam.esm.controller.config.security.jwt;

import com.epam.esm.dao.entity.User;
import com.epam.esm.service.AuthenticationService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.service.exception.ExceptionMessage.INVALID_CREDENTIALS;

@Component
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public User login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return userService.findUserByLogin(username);
        } catch (AuthenticationException e) {
            throw new ServiceException(INVALID_CREDENTIALS);
        }
    }
}
