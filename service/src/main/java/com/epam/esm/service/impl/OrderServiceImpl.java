package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.CostAndDateOfBuyDto;
import com.epam.esm.service.dto.entity.OrderDto;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final DtoConverter<Order, OrderDto> orderDtoConverter;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, DtoConverter<Order, OrderDto> orderDtoConverter, ExceptionHandler exceptionHandler) {
        this.orderDao = orderDao;
        this.orderDtoConverter = orderDtoConverter;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public OrderDto create(OrderDto orderDto) throws ServiceException {
        exceptionHandler.clean();
        Order order = orderDtoConverter.convertToEntity(orderDto);
        if (Validator.validateOrder(order, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, orderDto);
        return orderDtoConverter.convertToDto(orderDao.create(order).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        return orderDao.delete(id);
    }

    @Override
    public List<OrderDto> findAll(int page, int size) throws ServiceException {
        exceptionHandler.clean();
        List<Order> result = orderDao.findAll(page, size);
        if(result.isEmpty()){
            exceptionHandler.addException(EMPTY_LIST, result);
            throw new ServiceException(exceptionHandler);
        }
        return result.stream().map(orderDtoConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto find(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(ORDER_NOT_FOUND, id);
       return orderDtoConverter.convertToDto(orderDao.find(id).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public OrderDto update(long id, OrderDto orderDto) {
        exceptionHandler.clean();
        Order order = orderDtoConverter.convertToEntity(orderDto);
        if (!Validator.isGreaterZero(id, exceptionHandler) ||
                Validator.validateOrder(order, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        return orderDtoConverter.convertToDto(orderDao.update(id, order).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public CostAndDateOfBuyDto findCostAndDateOfBuyForUserByOrderId(long userId, long orderId) {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(userId, exceptionHandler) ||
                !Validator.isGreaterZero(orderId, exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(ORDER_NOT_FOUND, orderId);
        Order orderResult = orderDao.findCostAndDateOfBuyForUserByOrderId(userId, orderId).orElseThrow(() -> new ServiceException(exceptionHandler));
        return CostAndDateOfBuyDto.builder().cost(orderResult.getCost()).dateOfBuy(orderResult.getDateOfBuy()).build();
    }

    @Override
    public List<OrderDto> findOrdersByUserId(long id, int skip, int limit) {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        List<Order> result = orderDao.findOrdersByUserId(id, skip, limit);
        if (result.isEmpty()) {
            exceptionHandler.addException(EMPTY_LIST, result);
            throw new ServiceException(exceptionHandler);
        } else {
            return result.stream().map(orderDtoConverter::convertToDto).collect(Collectors.toList());
        }
    }

    @Override
    public long findSize() {
        return orderDao.findSize();
    }
}
