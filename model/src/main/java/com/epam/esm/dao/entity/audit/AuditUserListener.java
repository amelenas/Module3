package com.epam.esm.dao.entity.audit;

import com.epam.esm.dao.entity.User;

import javax.persistence.PrePersist;

public class AuditUserListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createUser(User user) {
        user.setLock(LOCK);
    }
}