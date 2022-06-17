package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.controller.hateoas.impl.TagHateoasImpl;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RequestMapping("/tags")
@RestController
public class TagController {
    private final TagService tagService;
    private final HateoasAdder<TagDto> hateoasAdder;

    @Autowired
    public TagController(TagServiceImpl tagService, HateoasAdder<TagDto> hateoasAdder) {
        this.tagService = tagService;
        this.hateoasAdder = hateoasAdder;
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> createTag(@RequestBody @Valid TagDto tagDto) throws ServiceException {
        tagService.create(tagDto);
        return new ResponseEntity<>(Translator.toLocale("new.tag.created"), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CollectionModel<TagDto>> findTags(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) throws ServiceException {
        Page<TagDto> tagDto = tagService.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
        for (TagDto dto : tagDto) {
            hateoasAdder.addLinks(dto);
        }
        return TagHateoasImpl.getCollectionModelWithPagination(tagDto);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<TagDto> findTag(@PathVariable Integer id) throws ServiceException {
        TagDto tagDto = tagService.find(id);
        hateoasAdder.addLinks(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteTag(@PathVariable Integer id) throws ServiceException {
        tagService.delete(id);
        return new ResponseEntity<>(Translator.toLocale("tag.deleted") + id, HttpStatus.OK);
    }

    @GetMapping("/popular")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TagDto> mostPopularTagOfUserWithHighestCostOfAllOrders() {
        TagDto tagDto = tagService.findMostPopularTagOfUserWithHighestCostOfOrder();
        hateoasAdder.addLinks(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }
}
