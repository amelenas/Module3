package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.service.dto.entity.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasImpl implements HateoasAdder<UserResponseDto> {
    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserResponseDto userDto) {
        userDto.add(linkTo(methodOn(CONTROLLER).userById(userDto.getId())).withSelfRel());
    }

    public static ResponseEntity<CollectionModel<UserResponseDto>> getCollectionModelWithPagination(Page<UserResponseDto> list) {
        int lastPage = list.getTotalPages() - 1;
        int firstPage = 0;
        int nextPage = list.nextOrLastPageable().getPageNumber();
        int prevPage = list.previousOrFirstPageable().getPageNumber();
        Link self = linkTo(methodOn(UserController.class).findAllUsers(list.getNumber(), list.getSize())).withSelfRel();
        Link next = linkTo(methodOn(UserController.class).findAllUsers(nextPage, list.getSize())).withRel("next");
        Link prev = linkTo(methodOn(UserController.class).findAllUsers(prevPage, list.getSize())).withRel("prev");
        Link first = linkTo(methodOn(UserController.class).findAllUsers(firstPage, list.getSize())).withRel("first");
        Link last = linkTo(methodOn(UserController.class).findAllUsers(lastPage, list.getSize())).withRel("last");
        return new ResponseEntity<>(CollectionModel.of(list, first, prev, self, next, last), HttpStatus.OK);
    }
}
