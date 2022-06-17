package com.epam.esm.dao;

import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findByLogin(String login);
    @Query("select o from Order o where o.orderId = :orderId and o.userId=:userId")
    Optional<Order> findCostAndDateOfBuyForUserByOrderId(Integer userId, Integer orderId);

    @Query(nativeQuery = true, value = "select user_id from users_orders order by cost desc LIMIT 1")
    Integer findUserIdWithTheBiggestSumOrders();
}

