package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.controller.hateoas.impl.OrderHateoasImpl;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.entity.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final HateoasAdder<OrderDto> hateoasAdder;
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto dto) {
        orderService.create(dto);
        return new ResponseEntity<>(Translator.toLocale("new.order.created"), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CollectionModel<OrderDto>> allOrders(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                               @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        Page<OrderDto> list = orderService.findAll(PageRequest.of(page, size, Sort.by("userId").ascending()));
        for (OrderDto dto : list) {
            hateoasAdder.addLinks(dto);
        }
        return OrderHateoasImpl.collectionModelWithPagination(list);
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CollectionModel<OrderDto>> findOrdersByUserId(@PathVariable Integer userId,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Page<OrderDto> list = orderService.findOrdersByUserId(userId, PageRequest.of(page, limit, Sort.by("orderId").ascending()));
        for (OrderDto order : list) {
            hateoasAdder.addLinks(order);
        }
        return OrderHateoasImpl.collectionModelWithPaginationUserId(userId, list);
    }
}
