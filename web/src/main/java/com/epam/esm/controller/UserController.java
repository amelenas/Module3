package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.controller.hateoas.impl.UserHateoasImpl;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final HateoasAdder<UserDto> hateoasAdder;

    @Autowired
    public UserController(UserService userService, DtoConverter<User, UserDto> userDtoConverter,
                          HateoasAdder<UserDto> hateoasAdder) {
        this.userService = userService;
        this.hateoasAdder = hateoasAdder;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDto> findAllUsers(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                 @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        int skip = skipQuantity(page, size);
        List<UserDto> usersDto = userService.findAll(skip, size);
        for (UserDto dto : usersDto) {
           hateoasAdder.addLinks(dto);
        }
        return UserHateoasImpl.getCollectionModelWithPagination(userService.findSize(), page, size, usersDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> userById(@PathVariable long id) {
        UserDto userDto  = userService.find(id);
        hateoasAdder.addLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        UserDto userDto = userService.create(dto);
        hateoasAdder.addLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    private int skipQuantity(int page, int size) {
        int skip = 0;
        if (page > 0) {
            skip = (page - 1) * size;
        }
        return skip;
    }
}
