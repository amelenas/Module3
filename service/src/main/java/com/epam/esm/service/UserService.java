package com.epam.esm.service;

import com.epam.esm.dao.entity.User;

public interface UserService {
    User findUserByLogin(String login);

}

