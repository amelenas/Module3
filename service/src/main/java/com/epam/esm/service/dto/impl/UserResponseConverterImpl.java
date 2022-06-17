package com.epam.esm.service.dto.impl;

import com.epam.esm.dao.entity.User;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResponseConverterImpl implements DtoConverter<User, UserResponseDto> {
    private final ModelMapper modelMapper;

        @Override
    public User convertToEntity(UserResponseDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @Override
    public UserResponseDto convertToDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }

}
