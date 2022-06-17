package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.OrderDto;
import com.epam.esm.service.dto.impl.OrderConverterImpl;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private static final Integer ID = 1;
    private final Order order = new Order(1, 1, 1, Instant.EPOCH);
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderService;
    @InjectMocks
    private DtoConverter<Order, OrderDto> dtoConverter = Mockito.mock(OrderConverterImpl.class);
    @Test
    void delete_InvalidId_Test() {
        assertThrows(ServiceException.class, () -> orderService.delete(-1));
    }

    @Test
    void delete_Positive_Test() {
        orderService.delete(ID);
        Mockito.verify(orderRepository).deleteById(ID);
    }

    @Test
    void findOrderById_Positive_Test() {
        when(orderRepository.save(order)).thenReturn(order);
        assertEquals(order, orderRepository.save(order));
    }
    @Test
    void findOrderById_UserNotFoundException_Test() {
        when(orderRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> orderService.find(ID));
    }
    @Test
    void findAllOrders_Test() throws ServiceException {
        Page<Order> mockList = mockPage();
        Page<OrderDto> expList = expectedPage();
        Pageable pageable = PageRequest.of(2, 2);
        when(orderRepository.findAll(pageable)).thenReturn(mockList);
        Page<OrderDto> actualList = orderService.findAll(pageable);
        assertEquals(expList.getTotalElements(), actualList.getTotalElements());
    }
    @Test
    void create_Order_Test() {
        Order order = Order.builder().orderId(1).userId(1).certificateId(1).cost(10.).dateOfBuy(Instant.EPOCH).build();
        OrderDto orderDto = OrderDto.builder().orderId(1).userId(1).certificateId(1).dateOfBuy(Instant.EPOCH).build();
        when(dtoConverter.convertToEntity(orderDto)).thenReturn(order);
        assertEquals(orderService.create(orderDto), dtoConverter.convertToDto(order));
    }
    @Test
    void entity_Order_Test() {
        Order order = Order.builder().orderId(1).userId(1).certificateId(1).cost(10.).dateOfBuy(Instant.EPOCH).build();
        OrderDto orderDto = OrderDto.builder().orderId(1).userId(1).certificateId(1).cost(10.).dateOfBuy(Instant.EPOCH).build();
        assertEquals(orderDto.getOrderId(), order.getOrderId());
        assertEquals(orderDto.getCertificateId(), order.getCertificateId());
        assertEquals(orderDto.getUserId(), order.getUserId());
        assertEquals(orderDto.getDateOfBuy(), order.getDateOfBuy());
        assertEquals(orderDto.getCost(), order.getCost());
    }
    @Test
    void findOrdersByUserId_Test(){
        Pageable pageable = PageRequest.of(2, 2);
        when(orderRepository.findOrdersByUserId(ID, pageable)).thenReturn(mockPage());
        assertEquals(expectedPage().getTotalElements(), orderService.findOrdersByUserId(ID, pageable).getTotalElements());
    }
    private Page<Order> mockPage() {
        List<Order> mockList = new ArrayList<>();
        mockList.add(Order.builder().orderId(1).userId(1).certificateId(1).dateOfBuy(Instant.EPOCH).build());
        mockList.add(Order.builder().orderId(2).userId(2).certificateId(2).dateOfBuy(Instant.EPOCH).build());
        mockList.add(Order.builder().orderId(2).userId(1).certificateId(2).dateOfBuy(Instant.EPOCH).build());
        return new PageImpl<>(mockList);
    }

    private Page<OrderDto> expectedPage() {
        List<OrderDto> expList = new ArrayList<>();
        expList.add(OrderDto.builder().orderId(1).userId(1).certificateId(1).dateOfBuy(Instant.EPOCH).build());
        expList.add(OrderDto.builder().orderId(1).userId(1).certificateId(1).dateOfBuy(Instant.EPOCH).build());
        expList.add(OrderDto.builder().orderId(1).userId(1).certificateId(1).dateOfBuy(Instant.EPOCH).build());
        return new PageImpl<>(expList);
    }
}
