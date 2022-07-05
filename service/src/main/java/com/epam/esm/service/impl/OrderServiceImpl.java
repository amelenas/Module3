package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.OrderDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DtoConverter<Order, OrderDto> orderDtoConverter;

    public OrderDto create(OrderDto orderDto) throws ServiceException {
        Order order = orderDtoConverter.convertToEntity(orderDto);
        Validator.validateOrder(order);
        return orderDtoConverter.convertToDto(orderRepository.save(order));
    }

    public void delete(Integer id) throws ServiceException {
         orderRepository.deleteById(id);
    }

    public Page<OrderDto> findAll(Pageable pageable) throws ServiceException {
        Page<Order> result = orderRepository.findAll(pageable);
        if (result.isEmpty()) {
            throw new ServiceException(EMPTY_LIST);
        }
        return result.map(orderDtoConverter::convertToDto);
    }

    public OrderDto find(Integer id) throws ServiceException {
        Validator.isGreaterZero(id);
        return orderDtoConverter.convertToDto(orderRepository.findById(id).orElseThrow(() -> new ServiceException(ORDER_NOT_FOUND)));
    }

    public Page<OrderDto> findOrdersByUserId(Integer id, Pageable pageable) {
        Validator.isGreaterZero(id);
        Page<Order> result = orderRepository.findOrdersByUserId(id, pageable);
        if (result.isEmpty()) {
            throw new ServiceException(EMPTY_LIST);
        } else {
            return result.map(orderDtoConverter::convertToDto);
        }
    }
}
