package com.epam.esm.service;

import com.epam.esm.dao.entity.Order;

import java.util.List;

public interface OrderService extends CRUDService<Order>{

    /**
     * @return cost and date of receipt of the user order
     */
    Order findCostAndDateOfBuyForUserByOrderId(long userId, long orderId);

    /**
     * @param skip values to display
     * @param limit quantity displayed
     * @return list of orders of user orders
     */
    List <Order> findOrdersByUserId(long id, int skip, int limit);

    /**
     * @return size of all actual orders
     */
    long findSize();
}
