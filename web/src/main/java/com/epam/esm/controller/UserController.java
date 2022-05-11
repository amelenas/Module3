package com.epam.esm.controller;

import com.epam.esm.controller.dto.DtoConverter;
import com.epam.esm.controller.dto.entity.UserDto;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    private final DtoConverter<User, UserDto> userDtoConverter;
    private final HateoasAdder<UserDto> hateoasAdder;

    @Autowired
    public UserController(UserService userService, DtoConverter<User, UserDto> userDtoConverter,
                          HateoasAdder<UserDto> hateoasAdder) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
        this.hateoasAdder = hateoasAdder;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDto> findAllUsers(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                 @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        int skip = skipQuantity(page, size);
        List<User> users = userService.findAll(skip, size);
        List<UserDto> usersDto = users.stream()
                .map(userDtoConverter::convertToDto)
                .peek(hateoasAdder::addLinks)
                .collect(Collectors.toList());
        return getCollectionModelWithPagination(page, size, usersDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> userById(@PathVariable long id) {
        User user = userService.find(id);
        UserDto userDto = userDtoConverter.convertToDto(user);
        hateoasAdder.addLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto dto) {
        User result = userService.create(userDtoConverter.convertToEntity(dto));
        UserDto userDto = userDtoConverter.convertToDto(result);
        hateoasAdder.addLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    private CollectionModel<UserDto> getCollectionModelWithPagination(int page, int limit, List<UserDto> list) {
        Long sizeOfList = userService.findSize();
        int lastPage = Math.toIntExact((sizeOfList % limit) > 0 ? sizeOfList / limit + 1 : sizeOfList / limit);
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
