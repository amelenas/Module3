package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.dto.DtoConverter;
import com.epam.esm.controller.dto.entity.TagDto;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/tags")
@RestController
public class TagController {
    private final TagService tagService;
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    private final HateoasAdder<TagDto> hateoasAdder;

    @Autowired
    public TagController(TagService tagService, DtoConverter<Tag, TagDto> tagDtoConverter, HateoasAdder<TagDto> hateoasAdder) {
        this.tagService = tagService;
        this.tagDtoConverter = tagDtoConverter;
        this.hateoasAdder = hateoasAdder;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createTag(@RequestBody @Valid TagDto tagDto) throws ServiceException {
        tagService.create(tagDtoConverter.convertToEntity(tagDto));
        return new ResponseEntity<>(Translator.toLocale("new.tag.created"), HttpStatus.CREATED);
    }

    @GetMapping
    public CollectionModel<TagDto> findTags(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) throws ServiceException {
        List<Tag> tags = tagService.findAll(page, size);
        List<TagDto> tagDto = convertList(tags);
        return getCollectionModelWithPagination(skipQuantity(page, size), size, tagDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findTag(@PathVariable long id) throws ServiceException {
        TagDto tagDto = tagDtoConverter.convertToDto(tagService.find(id));
        hateoasAdder.addLinks(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable long id) throws ServiceException {
        tagService.delete(id);
        return new ResponseEntity<>(Translator.toLocale("tag.deleted") + id, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<?> mostPopularTagOfUserWithHighestCostOfAllOrders() {
        List<Tag> tags = tagService.findMostPopularTagOfUserWithHighestCostOfOrder();
        List<TagDto> tagDto = convertList(tags);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    private List<TagDto> convertList(List<Tag> tags) {
        List<TagDto> listDto = new ArrayList<>();
        for (Tag tagDto : tags) {
            TagDto dto = tagDtoConverter.convertToDto(tagDto);
            listDto.add(dto);
            hateoasAdder.addLinks(dto);
        }
        return listDto;
    }
    private CollectionModel<TagDto> getCollectionModelWithPagination(int page, int limit, List<TagDto> list) {
        int lastPage = lastPage(tagService.findSize(), limit);
        int firstPage = 1;
        int nextPage = page >= lastPage ? lastPage : page+1;
        int prevPage = page <= firstPage ? firstPage : page-1;
        Link self = linkTo(methodOn(TagController.class).findTags(page, limit)).withSelfRel();
        Link next = linkTo(methodOn(TagController.class).findTags(nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(TagController.class).findTags(prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(TagController.class).findTags(firstPage, limit)).withRel("first");
        Link last = linkTo(methodOn(TagController.class).findTags(lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
    }

    private int lastPage(long sizeOfList, int limit){
        return Math.toIntExact((sizeOfList % limit) > 0 ? sizeOfList / limit + 1 : sizeOfList / limit);
    }

    private int skipQuantity(int page, int size) {
        int skip = 0;
        if (page <= 1) {
            page = 1;
        } else {
            skip = (page - 1) * size;
        }
        return skip;
    }
}
