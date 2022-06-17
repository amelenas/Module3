
package com.epam.esm.controller.config.security.jwt;

import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String ROLE = "ROLE_";
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Validator.validateName(username);
        User user = userService.findUserByLogin(username);
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                        createAuthorityList(ROLE + user.getRole().getRoleType()));
    }
}
