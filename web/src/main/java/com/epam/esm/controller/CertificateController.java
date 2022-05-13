package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.dto.DtoConverter;
import com.epam.esm.controller.dto.entity.CertificateDto;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/certificates")
@RestController
public class CertificateController {

    private final DtoConverter<Certificate, CertificateDto> certificateDtoConverter;
    private final HateoasAdder<CertificateDto> hateoasAdder;
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(DtoConverter<Certificate, CertificateDto> certificateDtoConverter,
                                 HateoasAdder<CertificateDto> hateoasAdder, CertificateService certificateService) {
        this.certificateDtoConverter = certificateDtoConverter;
        this.hateoasAdder = hateoasAdder;
        this.certificateService = certificateService;
    }

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody @Valid CertificateDto certificateDto) throws ServiceException {
        Certificate addedGiftCertificate = certificateService.create(
                certificateDtoConverter.convertToEntity(certificateDto));
        CertificateDto giftCertificateDto = certificateDtoConverter.convertToDto(addedGiftCertificate);
        hateoasAdder.addLinks(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/param")
    public CollectionModel<CertificateDto> findGiftCertificatesByAnyParams(
            @RequestParam(value = "tag", required = false) String[] tagNames,
            @RequestParam(value = "partName", required = false) String partName,
            @RequestParam(value = "sort_by", defaultValue = "id") String[] sort,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<CertificateDto> list = certificateService.findCertificatesByAnyParams(tagNames, partName, sort,
                page, limit).stream().map(certificateDtoConverter::convertToDto).collect(Collectors.toList());
        for (CertificateDto dto : list) {
            hateoasAdder.addLinks(dto);
        }
        return getCollectionModelWithPagination(tagNames, partName, sort, page, limit, list);
    }

    @GetMapping(value = "/all")
    public CollectionModel<CertificateDto> findCertificates(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) throws ServiceException {
        List<CertificateDto> list = certificateService.findAll(skipQuantity(page, size), size).stream().map(certificateDtoConverter::convertToDto).collect(Collectors.toList());
        for (CertificateDto dto : list) {
            hateoasAdder.addLinks(dto);
        }
        return getCollectionModelWithPagination(page, size, list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findCertificate(@PathVariable long id) throws ServiceException {
        Certificate giftCertificate = certificateService.find(id);
        CertificateDto giftCertificateDto = certificateDtoConverter.convertToDto(giftCertificate);
        hateoasAdder.addLinks(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) throws ServiceException {
        certificateService.delete(id);
        return new ResponseEntity<>(Translator.toLocale("certificate.deleted")+id, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateCertificatePartly(@PathVariable long id, @RequestBody Map<String, Object> updates) throws ServiceException {
        Certificate updatedCertificate = certificateService.update(id, updates);
        CertificateDto giftCertificateDto = certificateDtoConverter.convertToDto(updatedCertificate);
        hateoasAdder.addLinks(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable long id, @RequestBody @Valid  CertificateDto certificateDto) throws ServiceException {
        Certificate updatedCertificate = certificateService.update(id, certificateDtoConverter.convertToEntity(certificateDto));
        CertificateDto giftCertificateDto = certificateDtoConverter.convertToDto(updatedCertificate);
        hateoasAdder.addLinks(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    private CollectionModel<CertificateDto> getCollectionModelWithPagination(String[] tagNames, String partName,
                                                                             String[] sort, Integer page, Integer limit, List<CertificateDto> list) {
        int firstPage = 1;
        int nextPage = list.size() < limit ? page : page + 1;
        int prevPage = (page.equals(firstPage)) ? firstPage : page - 1;
        if (tagNames == null || tagNames.length < 1){
            tagNames = new String[]{""};
        } if (partName == null){
            partName = "";
        }
        Link self = linkTo(methodOn(CertificateController.class).findGiftCertificatesByAnyParams(tagNames, partName,
                sort, page, limit)).withSelfRel();
        Link next = linkTo(methodOn(CertificateController.class).findGiftCertificatesByAnyParams(tagNames, partName,
                sort, nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(CertificateController.class).findGiftCertificatesByAnyParams(tagNames, partName,
                sort, prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(CertificateController.class).findGiftCertificatesByAnyParams(tagNames, partName,
                sort, firstPage, limit)).withRel("first");
        return CollectionModel.of(list, first, prev, self, next);
    }

    private CollectionModel<CertificateDto> getCollectionModelWithPagination(int page, int limit, List<CertificateDto> list) {
        int lastPage = lastPage(certificateService.findSize(), limit);
        int firstPage = 1;
        int nextPage = page >= lastPage ? lastPage : page+1;
        int prevPage = page <= firstPage ? firstPage : page-1;
        Link self = linkTo(methodOn(CertificateController.class).findCertificates(page, limit)).withSelfRel();
        Link next = linkTo(methodOn(CertificateController.class).findCertificates(nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(CertificateController.class).findCertificates(prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(CertificateController.class).findCertificates(firstPage, limit)).withRel("first");
        Link last = linkTo(methodOn(CertificateController.class).findCertificates(lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
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

    private int lastPage(long sizeOfList, int limit){
       return Math.toIntExact((sizeOfList % limit) > 0 ? sizeOfList / limit + 1 : sizeOfList / limit);
    }
}
