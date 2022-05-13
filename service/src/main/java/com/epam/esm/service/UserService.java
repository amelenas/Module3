package com.epam.esm.service;

import com.epam.esm.dao.entity.User;

public interface UserService extends CRUDService<User>{

    /**
     * @return size of all actual users
     */
    Long findSize();
}
