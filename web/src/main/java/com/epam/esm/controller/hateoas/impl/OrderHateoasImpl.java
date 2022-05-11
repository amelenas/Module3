package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.UserController;
import com.epam.esm.controller.dto.entity.OrderDto;
import com.epam.esm.controller.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class OrderHateoasImpl implements HateoasAdder<OrderDto> {

    @Override
    public void addLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(UserController.class).userById(orderDto.getUserId())).withRel("User"));
        orderDto.add(linkTo(methodOn(CertificateController.class).findCertificate(orderDto.getCertificateId())).withRel("Certificate"));
    }
}
