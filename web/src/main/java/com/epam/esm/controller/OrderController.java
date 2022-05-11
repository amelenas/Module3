package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.dto.DtoConverter;
import com.epam.esm.controller.dto.entity.*;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    private final DtoConverter<Order, OrderDto> orderDtoConverter;
    private final HateoasAdder<OrderDto> hateoasAdder;

    @Autowired
    public OrderController(OrderService orderService, DtoConverter<Order, OrderDto> orderDtoConverter, HateoasAdder<OrderDto> hateoasAdder) {
        this.orderService = orderService;
        this.orderDtoConverter = orderDtoConverter;
        this.hateoasAdder = hateoasAdder;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDto dto) {
        orderService.create(orderDtoConverter.convertToEntity(dto));
        return new ResponseEntity<>(Translator.toLocale("new.order.created"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}")
    public ResponseEntity<CostAndDateOfBuyDto> getCostAndDateOfBuyForUserByOrderId(@PathVariable long userId,
                                                                        @PathVariable long orderId) {
      Order orderResult = orderService.findCostAndDateOfBuyForUserByOrderId(userId, orderId);
        CostAndDateOfBuyDto dto = CostAndDateOfBuyDto.builder().cost(orderResult.getCost()).dateOfBuy(orderResult.getDateOfBuy()).build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public CollectionModel<OrderDto> allOrders(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                               @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        List<OrderDto> list = orderService.findAll(skipQuantity(page, size), size).stream().map(orderDtoConverter::convertToDto).collect(Collectors.toList());
        for (OrderDto dto : list) {
            hateoasAdder.addLinks(dto);
        }
        return getCollectionModelWithPagination(page, size, list);
    }

    @GetMapping(value = "/{userId}/orders")
    public CollectionModel<OrderDto> findOrdersByUserId(@PathVariable long userId,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Order> list = orderService.findOrdersByUserId(userId,skipQuantity(page, limit), limit);
        List<OrderDto> listDto = new ArrayList<>();
        for (Order order : list) {
            OrderDto orderDto = orderDtoConverter.convertToDto(order);
            listDto.add(orderDto);
            hateoasAdder.addLinks(orderDto);
        }
        return getCollectionModelWithPagination(userId, page, limit, listDto);
    }

    private CollectionModel<OrderDto> getCollectionModelWithPagination(long userId, int page, int limit,
                                                                       List<OrderDto> list) {
        long sizeOfList = orderService.findSize();
        int lastPage = Math.toIntExact((sizeOfList % limit) > 0 ? sizeOfList / limit + 1 : sizeOfList / limit);
        int firstPage = 1;
        int nextPage = list.size() < limit ? page : page + 1;
        int prevPage = (page<firstPage) ? firstPage : page - 1;
        Link self = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, page, limit)).withSelfRel();
        Link next = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, firstPage, limit))
                .withRel("first");
        Link last = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
    }

    private CollectionModel<OrderDto> getCollectionModelWithPagination(int page, int limit, List<OrderDto> list) {
        long sizeOfList = orderService.findSize();
        int lastPage = Math.toIntExact((sizeOfList % limit) > 0 ? sizeOfList / limit + 1 : sizeOfList / limit);
        int firstPage = 1;
        int nextPage = page >= lastPage ? lastPage : page+1;
        int prevPage = page <= firstPage ? firstPage : page-1;
        Link self = linkTo(methodOn(OrderController.class).allOrders(page, limit)).withSelfRel();
        Link next = linkTo(methodOn(OrderController.class).allOrders(nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(OrderController.class).allOrders(prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(OrderController.class).allOrders(firstPage, limit)).withRel("first");
        Link last = linkTo(methodOn(OrderController.class).allOrders(lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
    }

    private int skipQuantity(int page, int size) {
        int skip = 0;
        if (page <= 1) {
            page = 1;
        } else {
            skip = (page - 1) * size;
        }
        return skip;
    }
}
