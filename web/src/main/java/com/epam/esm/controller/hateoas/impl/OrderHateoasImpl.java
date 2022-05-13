package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.service.dto.entity.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderHateoasImpl implements HateoasAdder<OrderDto> {

    @Override
    public void addLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(UserController.class).userById(orderDto.getUserId())).withRel("User"));
        orderDto.add(linkTo(methodOn(CertificateController.class).findCertificate(orderDto.getCertificateId())).withRel("Certificate"));
    }

    public static CollectionModel<OrderDto> collectionModelWithPaginationUserId(long userId, int page, int limit, List<OrderDto> list) {
        int firstPage = 1;
        int nextPage = list.size() < limit ? page : page + 1;
        int prevPage = page <= firstPage ? firstPage : page - 1;
        Link self = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, page, limit)).withSelfRel();
        Link next = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, firstPage, limit))
                .withRel("first");
        return CollectionModel.of(list, first, prev, self, next);
    }

    public static CollectionModel<OrderDto> collectionModelWithPagination(long size, int page, int limit, List<OrderDto> list) {
        int lastPage = Math.toIntExact((size % limit) > 0 ? size / limit + 1 : size / limit);
        int firstPage = 1;
        int nextPage = page >= lastPage ? lastPage : page + 1;
        int prevPage = page <= firstPage ? firstPage : page - 1;
        Link self = linkTo(methodOn(OrderController.class).allOrders(page, limit)).withSelfRel();
        Link next = linkTo(methodOn(OrderController.class).allOrders(nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(OrderController.class).allOrders(prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(OrderController.class).allOrders(firstPage, limit)).withRel("first");
        Link last = linkTo(methodOn(OrderController.class).allOrders(lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
    }

}
