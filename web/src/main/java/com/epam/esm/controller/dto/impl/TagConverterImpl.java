package com.epam.esm.controller.dto.impl;

import com.epam.esm.controller.dto.DtoConverter;
import com.epam.esm.controller.dto.entity.TagDto;
import com.epam.esm.dao.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagConverterImpl implements DtoConverter<Tag, TagDto> {

    private final ModelMapper modelMapper;

    @Override
    public Tag convertToEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    @Override
    public TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

}
