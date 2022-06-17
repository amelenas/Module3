package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.service.dto.entity.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoasImpl implements HateoasAdder<TagDto> {
    private static final Class<TagController> CONTROLLER = TagController.class;

    @Override
    public void addLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(CONTROLLER).findTag(tagDto.getId())).withSelfRel());
        tagDto.add(linkTo(methodOn(CONTROLLER).deleteTag(tagDto.getId())).withRel("delete"));
        tagDto.add(linkTo(methodOn(CONTROLLER).createTag(tagDto)).withRel("new"));
    }

    public static ResponseEntity<CollectionModel<TagDto>> getCollectionModelWithPagination(Page<TagDto> list) {
        int lastPage = list.getTotalPages() - 1;
        int firstPage = 0;
        int nextPage = list.nextOrLastPageable().getPageNumber();
        int prevPage = list.previousOrFirstPageable().getPageNumber();
        Link self = linkTo(methodOn(TagController.class).findTags(list.getNumber(), list.getSize())).withSelfRel();
        Link next = linkTo(methodOn(TagController.class).findTags(nextPage, list.getSize())).withRel("next");
        Link prev = linkTo(methodOn(TagController.class).findTags(prevPage, list.getSize())).withRel("prev");
        Link first = linkTo(methodOn(TagController.class).findTags(firstPage, list.getSize())).withRel("first");
        Link last = linkTo(methodOn(TagController.class).findTags(lastPage, list.getSize())).withRel("last");
        return new ResponseEntity<>(CollectionModel.of(list, first, prev, self, next, last), HttpStatus.OK);
    }
}
