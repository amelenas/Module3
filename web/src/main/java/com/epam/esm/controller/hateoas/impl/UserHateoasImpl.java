package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.service.dto.entity.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasImpl implements HateoasAdder<UserDto> {
    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(CONTROLLER).userById(userDto.getId())).withSelfRel());
    }

    public static CollectionModel<UserDto> getCollectionModelWithPagination(long size, int page, int limit, List<UserDto> list) {
        int lastPage = Math.toIntExact((size % limit) > 0 ? size / limit + 1 : size / limit);
        int firstPage = 1;
        int nextPage = (page == lastPage) ? lastPage : ++page;
        int prevPage = (page == firstPage) ? firstPage : --page;
        Link self = linkTo(methodOn(UserController.class).findAllUsers(page, limit)).withSelfRel();
        Link next = linkTo(methodOn(UserController.class).findAllUsers(nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(UserController.class).findAllUsers(prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(UserController.class).findAllUsers(firstPage, limit)).withRel("first");
        Link last = linkTo(methodOn(UserController.class).findAllUsers(lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
    }

}
