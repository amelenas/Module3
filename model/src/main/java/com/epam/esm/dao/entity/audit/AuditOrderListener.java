package com.epam.esm.dao.entity.audit;

import com.epam.esm.dao.entity.Order;

import javax.persistence.PrePersist;
import java.time.Instant;

public class AuditOrderListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createOrder(Order order) {
        order.setDateOfBuy(Instant.now());
        order.setLock(LOCK);
    }
}
