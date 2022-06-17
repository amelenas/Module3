package com.epam.esm.dao;

import com.epam.esm.dao.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository  extends PagingAndSortingRepository<Order, Integer>{
    Page<Order> findOrdersByUserId(Integer id, Pageable pageable);
    List<Order> findOrdersByUserId(Integer id);
}
