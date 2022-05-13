package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.controller.hateoas.impl.OrderHateoasImpl;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.entity.CostAndDateOfBuyDto;
import com.epam.esm.service.dto.entity.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    private final HateoasAdder<OrderDto> hateoasAdder;

    @Autowired
    public OrderController(OrderService orderService, HateoasAdder<OrderDto> hateoasAdder) {
        this.orderService = orderService;
        this.hateoasAdder = hateoasAdder;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDto dto) {
        orderService.create(dto);
        return new ResponseEntity<>(Translator.toLocale("new.order.created"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}")
    public ResponseEntity<CostAndDateOfBuyDto> getCostAndDateOfBuyForUserByOrderId(@PathVariable long userId,
                                                                                   @PathVariable long orderId) {
        return new ResponseEntity<>(orderService.findCostAndDateOfBuyForUserByOrderId(userId, orderId), HttpStatus.OK);
    }

    @GetMapping
    public CollectionModel<OrderDto> allOrders(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                               @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        List<OrderDto> list = orderService.findAll(skipQuantity(page, size), size);
        for (OrderDto dto : list) {
            hateoasAdder.addLinks(dto);
        }
        return OrderHateoasImpl.collectionModelWithPagination(orderService.findSize(), page, size, list);
    }

    @GetMapping(value = "/{userId}/orders")
    public CollectionModel<OrderDto> findOrdersByUserId(@PathVariable long userId,
                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<OrderDto> list = orderService.findOrdersByUserId(userId, skipQuantity(page, limit), limit);
        for (OrderDto order : list) {
            hateoasAdder.addLinks(order);
        }
        return OrderHateoasImpl.collectionModelWithPaginationUserId(userId, page, limit, list);
    }

    private int skipQuantity(int page, int size) {
        int skip = 0;
        if (page > 0) {
            skip = (page - 1) * size;
        }
        return skip;
    }
}
