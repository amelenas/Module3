package com.epam.esm.dao.entity.audit;

import com.epam.esm.dao.entity.Order;

import javax.persistence.PrePersist;
import java.time.Instant;

public class AuditOrderListener {
    @PrePersist
    public void createOrder(Order order) {
        order.setDateOfBuy(Instant.now());
    }
}
