package com.epam.esm.service.impl;

 import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.impl.OrderDaoImpl;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderDaoImpl orderDao = Mockito.mock(OrderDaoImpl.class);
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void create_PositiveTest() {
        Order order = new Order(1,1, 1, 120., Instant.EPOCH);
        order.setLock(0);
        Mockito.when(orderDao.create(order)).thenReturn(Optional.of(order));
        orderService.create(order);
    }

    @Test
    void create_InvalidDataTest() {
        Order order = new Order(0, -1,  0, Instant.EPOCH);
        assertThrows(ServiceException.class, () -> orderService.create(order));
    }

    @Test
    void delete_PositiveTest() {
        Mockito.when(orderDao.delete(1)).thenReturn(true);
        assertTrue(orderService.delete(1));
    }

    @Test
    void delete_InvalidIdTest() {
        assertThrows(ServiceException.class, () -> orderService.delete(-1));
    }

    @Test
    void find_PositiveTest() {
        Order order = new Order(1, 1, 120, Instant.EPOCH);
        Mockito.when(orderDao.find(2)).thenReturn(Optional.of(order));
        orderService.find(2);
        Mockito.verify(orderDao).find(2);
    }
    @Test
    void find_IncorrectIdTest() {
        assertThrows(ServiceException.class, () -> orderService.find(-1));
    }

    @Test
    void update_PositiveTest() {
        Order order = new Order(1, 1, 120, Instant.EPOCH);
        Mockito.when(orderDao.update(1, order)).thenReturn(Optional.of(order));
        orderService.update(1, order);
        Mockito.verify(orderDao).update(1, order);
    }

    @Test
    void findCostAndDateOfBuyForUserByOrderId_PositiveTest() {
        Mockito.when(orderDao.findCostAndDateOfBuyForUserByOrderId(1,2)).thenReturn(Optional.of(new Order(100, Instant.EPOCH)));
        orderService.findCostAndDateOfBuyForUserByOrderId(1,2);
    }

    @Test
    void findOrdersByUserId_PositiveTest() {
        Order order1 = new Order(1, 1, 120, Instant.EPOCH);
        Order order2 = new Order(1, 2, 121, Instant.EPOCH);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Mockito.when(orderDao.findOrdersByUserId(1,0,15)).thenReturn(orders);
        orderService.findOrdersByUserId(1, 0, 15);
    }
}