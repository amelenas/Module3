package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.service.dto.entity.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderHateoasImpl implements HateoasAdder<OrderDto> {

    @Override
    public void addLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(UserController.class).userById(orderDto.getUserId())).withRel("User"));
        orderDto.add(linkTo(methodOn(CertificateController.class).findCertificate(orderDto.getCertificateId())).withRel("Certificate"));
    }

    public static ResponseEntity<CollectionModel<OrderDto>> collectionModelWithPaginationUserId(Integer userId,  Page<OrderDto> list) {
        int lastPage = list.getTotalPages() - 1;
        int firstPage = 0;
        int nextPage = list.nextOrLastPageable().getPageNumber();
        int prevPage = list.previousOrFirstPageable().getPageNumber();
        Link self = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, list.getNumber(), list.getSize())).withSelfRel();
        Link next = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, nextPage, list.getSize())).withRel("next");
        Link prev = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, prevPage, list.getSize())).withRel("prev");
        Link last = linkTo(methodOn(OrderController.class).allOrders(lastPage, list.getSize())).withRel("last");
        Link first = linkTo(methodOn(OrderController.class).findOrdersByUserId(userId, firstPage, list.getSize()))
                .withRel("first");
        return new ResponseEntity<>(CollectionModel.of(list, first, prev, self, next, last), HttpStatus.OK);
    }

    public static ResponseEntity<CollectionModel<OrderDto>> collectionModelWithPagination(Page<OrderDto> list) {
        int lastPage = list.getTotalPages() - 1;
        int firstPage = 0;
        int nextPage = list.nextOrLastPageable().getPageNumber();
        int prevPage = list.previousOrFirstPageable().getPageNumber();
        Link self = linkTo(methodOn(OrderController.class).allOrders(list.getNumber(), list.getSize())).withSelfRel();
        Link next = linkTo(methodOn(OrderController.class).allOrders(nextPage, list.getSize())).withRel("next");
        Link prev = linkTo(methodOn(OrderController.class).allOrders(prevPage, list.getSize())).withRel("prev");
        Link first = linkTo(methodOn(OrderController.class).allOrders(firstPage, list.getSize())).withRel("first");
        Link last = linkTo(methodOn(OrderController.class).allOrders(lastPage, list.getSize())).withRel("last");
        return new ResponseEntity<>(CollectionModel.of(list, first, prev, self, next, last), HttpStatus.OK);
    }

}
