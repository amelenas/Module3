package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.service.dto.entity.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public static CollectionModel<TagDto> getCollectionModelWithPagination(long size, int page, int limit, List<TagDto> list) {
        int lastPage = lastPage(size, limit);
        int firstPage = 1;
        int nextPage = page >= lastPage ? lastPage : page + 1;
        int prevPage = page <= firstPage ? firstPage : page - 1;
        Link self = linkTo(methodOn(TagController.class).findTags(page, limit)).withSelfRel();
        Link next = linkTo(methodOn(TagController.class).findTags(nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(TagController.class).findTags(prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(TagController.class).findTags(firstPage, limit)).withRel("first");
        Link last = linkTo(methodOn(TagController.class).findTags(lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
    }


    private static int lastPage(long sizeOfList, int limit) {
        return Math.toIntExact((sizeOfList % limit) > 0 ? sizeOfList / limit + 1 : sizeOfList / limit);

    }
}
