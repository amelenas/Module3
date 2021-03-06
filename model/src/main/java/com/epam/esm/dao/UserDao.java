package com.epam.esm.dao;

import com.epam.esm.dao.entity.User;

public interface UserDao extends CRUDDao<User> {

    /**
     * @return size of all actual users
     */
    Long findSize();
}
