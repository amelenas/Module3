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
    void delete_PositiveTest() {
        Mockito.when(orderDao.delete(1)).thenReturn(true);
        assertTrue(orderService.delete(1));
    }

    @Test
    void delete_InvalidIdTest() {
        assertThrows(ServiceException.class, () -> orderService.delete(-1));
    }

    @Test
    void find_IncorrectIdTest() {
        assertThrows(ServiceException.class, () -> orderService.find(-1));
    }


    @Test
    void findCostAndDateOfBuyForUserByOrderId_PositiveTest() {
        Mockito.when(orderDao.findCostAndDateOfBuyForUserByOrderId(1,2)).thenReturn(Optional.of(new Order(100, Instant.EPOCH)));
        orderService.findCostAndDateOfBuyForUserByOrderId(1,2);
    }
}