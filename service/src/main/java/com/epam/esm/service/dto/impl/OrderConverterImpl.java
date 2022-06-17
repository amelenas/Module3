package com.epam.esm.service.dto.impl;

import com.epam.esm.dao.entity.Order;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.OrderDto;
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
