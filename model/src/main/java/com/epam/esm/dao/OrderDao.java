package com.epam.esm.dao;

import com.epam.esm.dao.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends CRUDDao<Order> {

    /**
     * @return cost and date of receipt of the user order
     */
    Optional<Order> findCostAndDateOfBuyForUserByOrderId(long userId, long orderId);

    /**
     * @param skip values to display
     * @param limit quantity displayed
     * @return list of orders of user orders
     */
    List<Order> findOrdersByUserId(long id, int skip, int limit);

}
