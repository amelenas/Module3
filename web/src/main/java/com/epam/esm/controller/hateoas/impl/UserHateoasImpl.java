package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.dto.entity.UserDto;
import com.epam.esm.controller.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasImpl implements HateoasAdder<UserDto> {
    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(CONTROLLER).userById(userDto.getId())).withSelfRel());
    }
}
