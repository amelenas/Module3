package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/certificates_script.sql")
class OrderDaoImplTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    void create_positiveTest() {
        Order order = new Order(2, 3, 560.0, Instant.now());
        Order orderFromDb = orderRepository.save(order);
        order.setOrderId(orderFromDb.getOrderId());
        assertEquals(orderFromDb, order);
    }

    @Test
    void delete_positiveTest() {
        orderRepository.deleteById(1);
        assertFalse(orderRepository.existsById(1));
    }

    @Test
    void findAll_positiveTest() {
        assertEquals(5, orderRepository.findAll(PageRequest.of(0,15)).getTotalElements());
    }

    @Test
    void find_positiveTest() {
        Order orderExpected = new Order(2, 2, 2, 1.0, Instant.now());
        Order orderActual = orderRepository.findById(2).get();
        orderExpected.setDateOfBuy(orderActual.getDateOfBuy());
        assertEquals(orderExpected, orderActual);
    }

    @Test
    void findOrdersByUserId_positiveTest() {
        assertEquals(2, orderRepository.findOrdersByUserId(1, PageRequest.of(0, 15)).getTotalElements());
    }
}
