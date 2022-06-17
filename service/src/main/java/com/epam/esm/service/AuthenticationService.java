package com.epam.esm.service;

import com.epam.esm.dao.entity.User;

public interface AuthenticationService {
    User login(String username, String password);
}
