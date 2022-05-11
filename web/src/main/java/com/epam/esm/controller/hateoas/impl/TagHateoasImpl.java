package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.dto.entity.TagDto;
import com.epam.esm.controller.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TagHateoasImpl implements HateoasAdder<TagDto> {
    private static final Class<TagController> CONTROLLER = TagController.class;

    @Override
    public void addLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(CONTROLLER).findTag(tagDto.getId())).withSelfRel());
        tagDto.add(linkTo(methodOn(CONTROLLER).deleteTag(tagDto.getId())).withRel("delete"));
        tagDto.add(linkTo(methodOn(CONTROLLER).createTag(tagDto)).withRel("new"));
    }
}
