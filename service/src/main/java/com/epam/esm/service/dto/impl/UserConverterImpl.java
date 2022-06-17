package com.epam.esm.service.dto.impl;

import com.epam.esm.dao.entity.User;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverterImpl implements DtoConverter<User, UserDto> {
    private final ModelMapper modelMapper;
    @Override
    public User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

}
