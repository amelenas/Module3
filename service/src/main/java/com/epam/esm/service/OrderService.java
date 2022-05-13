package com.epam.esm.service;

import com.epam.esm.service.dto.entity.CostAndDateOfBuyDto;
import com.epam.esm.service.dto.entity.OrderDto;

import java.util.List;

public interface OrderService extends CRUDService<OrderDto>{

    /**
     * @return cost and date of receipt of the user order
     */
    CostAndDateOfBuyDto findCostAndDateOfBuyForUserByOrderId(long userId, long orderId);

    /**
     * @param skip values to display
     * @param limit quantity displayed
     * @return list of orders of user orders
     */
    List<OrderDto>findOrdersByUserId(long id, int skip, int limit);


}
