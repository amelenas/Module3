package com.epam.esm.controller;

import com.epam.esm.controller.config.language.Translator;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.controller.hateoas.impl.CertificateHateoasImpl;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RequestMapping("/certificates")
@RestController
public class CertificateController {

    private final HateoasAdder<CertificateDto> hateoasAdder;
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(HateoasAdder<CertificateDto> hateoasAdder, CertificateService certificateService) {
        this.hateoasAdder = hateoasAdder;
        this.certificateService = certificateService;
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody @Valid CertificateDto certificateDto) throws ServiceException {
        CertificateDto giftCertificateDto = certificateService.create(certificateDto);
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
                page, limit);
        for (CertificateDto dto : list) {
            hateoasAdder.addLinks(dto);
        }
        return CertificateHateoasImpl.getCollectionModelWithPagination(tagNames, partName, sort, page, limit, list);
    }

    @GetMapping(value = "/all")
    public CollectionModel<CertificateDto> findCertificates(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                            @RequestParam(value = "size", defaultValue = "10", required = false) int size) throws ServiceException {
        List<CertificateDto> list = certificateService.findAll(skipQuantity(page, size), size);
        for (CertificateDto dto : list) {
            hateoasAdder.addLinks(dto);
        }
        return CertificateHateoasImpl.getCollectionModelWithPagination(certificateService.findSize(), page, size, list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CertificateDto> findCertificate(@PathVariable long id) throws ServiceException {
        CertificateDto giftCertificateDto = certificateService.find(id);
        hateoasAdder.addLinks(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable long id) throws ServiceException {
        certificateService.delete(id);
        return new ResponseEntity<>(Translator.toLocale("certificate.deleted") + id, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CertificateDto> updateCertificatePartly(@PathVariable long id, @RequestBody Map<String, Object> updates) throws ServiceException {
        CertificateDto giftCertificateDto = certificateService.update(id, updates);
        hateoasAdder.addLinks(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<CertificateDto> updateCertificate(@PathVariable long id, @RequestBody @Valid CertificateDto certificateDto) throws ServiceException {
        CertificateDto giftCertificateDto = certificateService.update(id, certificateDto);
        hateoasAdder.addLinks(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    private int skipQuantity(int page, int size) {
        int skip = 0;
        if (page > 0) {
            skip = (page - 1) * size;
        }
        return skip;
    }


}
