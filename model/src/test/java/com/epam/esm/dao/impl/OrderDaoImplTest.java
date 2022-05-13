package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/certificates_script.sql")
class OrderDaoImplTest {
    @Autowired
    OrderDao orderDao;

    @Test
    void create_positiveTest() {
        Order order = new Order(2, 3, 560.0, Instant.now());
        Order orderFromDb = orderDao.create(order).get();
        order.setOrderId(orderFromDb.getOrderId());
        assertEquals(orderFromDb, order);
    }

    @Test
    void delete_positiveTest() {
        assertTrue(orderDao.delete(1));
    }

    @Test
    void findAll_positiveTest() {
        assertEquals(5, orderDao.findAll(0,15).size());
    }

    @Test
    void find_positiveTest() {
        Order orderExpected = new Order(2, 2, 2, 1.0, Instant.now());
        orderExpected.setLock(0);
        Order orderActual = orderDao.find(2).get();
        orderExpected.setDateOfBuy(orderActual.getDateOfBuy());
        assertEquals(orderExpected, orderActual);
    }

    @Test
    void update_positiveTest() {
        Order orderExpected = new Order(2, 3, 2, 900.0, Instant.now());
        orderExpected.setLock(0);
        Order orderActual = orderDao.update(2, orderExpected).get();
        orderExpected.setDateOfBuy(orderActual.getDateOfBuy());
        orderExpected.setDateOfBuy(orderActual.getDateOfBuy());
        assertEquals(orderExpected, orderActual);
    }

    @Test
    void findCostAndDateOfBuyForUserByOrderId_positiveTest() {
        assertEquals(1200, orderDao.findCostAndDateOfBuyForUserByOrderId(3, 3).get().getCost());
    }

    @Test
    void findOrdersByUserId_positiveTest() {
        assertEquals(2, orderDao.findOrdersByUserId(1, 0, 15).size());
    }
}