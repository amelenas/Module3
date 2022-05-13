package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.controller.hateoas.impl.TagHateoasImpl;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/tags")
@RestController
public class TagController {
    private final TagService tagService;
    private final HateoasAdder<TagDto> hateoasAdder;

    @Autowired
    public TagController(TagService tagService, HateoasAdder<TagDto> hateoasAdder) {
        this.tagService = tagService;
        this.hateoasAdder = hateoasAdder;
    }

    @PostMapping(value = "/")
    public ResponseEntity<String> createTag(@RequestBody @Valid TagDto tagDto) throws ServiceException {
        tagService.create(tagDto);
        return new ResponseEntity<>(Translator.toLocale("new.tag.created"), HttpStatus.CREATED);
    }

    @GetMapping
    public CollectionModel<TagDto> findTags(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) throws ServiceException {
        List<TagDto> tagDto = tagService.findAll(skipQuantity(page, size), size);
        for (TagDto dto : tagDto) {
            hateoasAdder.addLinks(dto);
        }
        return TagHateoasImpl.getCollectionModelWithPagination(tagService.findSize(), page, size, tagDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TagDto> findTag(@PathVariable long id) throws ServiceException {
        TagDto tagDto = tagService.find(id);
        hateoasAdder.addLinks(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable long id) throws ServiceException {
        tagService.delete(id);
        return new ResponseEntity<>(Translator.toLocale("tag.deleted") + id, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<TagDto>> mostPopularTagOfUserWithHighestCostOfAllOrders() {
        List<TagDto> tagDto = tagService.findMostPopularTagOfUserWithHighestCostOfOrder();
        for (TagDto dto : tagDto) {
            hateoasAdder.addLinks(dto);
        }
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    private int skipQuantity(int page, int size) {
        int skip = 0;
        if (page > 0) {
            skip = (page - 1) * size;
        }
        return skip;
    }
}
