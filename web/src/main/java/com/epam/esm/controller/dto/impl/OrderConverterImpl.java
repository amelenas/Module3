package com.epam.esm.controller.dto.impl;

import com.epam.esm.controller.dto.DtoConverter;
import com.epam.esm.controller.dto.entity.OrderDto;
import com.epam.esm.dao.entity.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConverterImpl implements DtoConverter<Order, OrderDto> {

    private final ModelMapper modelMapper;

    @Override
    public Order convertToEntity(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

}
