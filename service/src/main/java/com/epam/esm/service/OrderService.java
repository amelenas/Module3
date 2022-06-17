
package com.epam.esm.service;

import com.epam.esm.service.dto.entity.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService extends CRUDService<OrderDto>{
    Page<OrderDto> findOrdersByUserId(Integer id, Pageable pageable);
}
