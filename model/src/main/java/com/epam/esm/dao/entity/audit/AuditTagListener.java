package com.epam.esm.dao.entity.audit;

import com.epam.esm.dao.entity.Tag;

import javax.persistence.PrePersist;

public class AuditTagListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createTag(Tag tag) {
        tag.setLock(LOCK);
    }
}
