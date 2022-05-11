package com.epam.esm.controller.dto;

import org.springframework.stereotype.Component;

@Component
public interface DtoConverter<E, D>{

    E convertToEntity(D d);

    D convertToDto(E e);
}

