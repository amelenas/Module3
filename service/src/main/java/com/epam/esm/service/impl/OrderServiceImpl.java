package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order create(Order order) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (Validator.validateOrder(order, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<Order> result = orderDao.create(order);
        if (result.isPresent()) {
            return result.get();
        }
        exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, result);
        throw new ServiceException(exceptionHandler);
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        return orderDao.delete(id);
    }

    @Override
    public List<Order> findAll(int page, int size) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        List<Order> result = orderDao.findAll(page, size);
        if(result.isEmpty()){
            exceptionHandler.addException(EMPTY_LIST, result);
            throw new ServiceException(exceptionHandler);
        }
        return result;
    }

    @Override
    public Order find(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<Order> result = orderDao.find(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(ORDER_NOT_FOUND, id);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public Order update(long id, Order order) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler) ||
                Validator.validateOrder(order, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<Order> result = orderDao.update(id, order);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, id);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public Order findCostAndDateOfBuyForUserByOrderId(long userId, long orderId) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(userId, exceptionHandler) ||
                !Validator.isGreaterZero(orderId, exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        Optional<Order> result = orderDao.findCostAndDateOfBuyForUserByOrderId(userId, orderId);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(ORDER_NOT_FOUND, orderId);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public List<Order> findOrdersByUserId(long id, int skip, int limit) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        List<Order> result = orderDao.findOrdersByUserId(id, skip, limit);
        if (result.size()>0) {
            return result;
        } else {
            exceptionHandler.addException(EMPTY_LIST, result);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public long findSize() {
        return orderDao.findSize();
    }
}
