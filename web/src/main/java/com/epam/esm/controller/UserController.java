package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.controller.hateoas.impl.UserHateoasImpl;
import com.epam.esm.service.dto.entity.CostAndDateOfBuyDto;
import com.epam.esm.service.dto.entity.UserResponseDto;
import com.epam.esm.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final HateoasAdder<UserResponseDto> hateoasAdder;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CollectionModel<UserResponseDto>> findAllUsers(
                                                                 @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                 @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        Page<UserResponseDto> usersDto = userService.findAll(PageRequest.of(page, size, Sort.by("userId").ascending()));
        for (UserResponseDto dto : usersDto) {
           hateoasAdder.addLinks(dto);
        }
        return UserHateoasImpl.getCollectionModelWithPagination(usersDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> userById(@PathVariable Integer id) {
        UserResponseDto userDto  = userService.find(id);
        hateoasAdder.addLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CostAndDateOfBuyDto> getCostAndDateOfBuyForUserByOrderId(@PathVariable Integer userId,
                                                                                   @PathVariable Integer orderId) {
        return new ResponseEntity<>(userService.findCostAndDateOfBuyForUserByOrderId(userId, orderId), HttpStatus.OK);
    }
}
