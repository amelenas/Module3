package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.hateoas.HateoasAdder;
import com.epam.esm.service.dto.entity.CertificateDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CertificateHateoasImpl implements HateoasAdder<CertificateDto> {
    private static final Class<CertificateController> CONTROLLER = CertificateController.class;
    private static final Class<TagController> TAG_CONTROLLER = TagController.class;


    @Override
    public void addLinks(CertificateDto certificateDto) {
        certificateDto.add(linkTo(methodOn(CONTROLLER)
                .findCertificate(certificateDto.getId())).withSelfRel());
        certificateDto.add(linkTo(methodOn(CONTROLLER)
                .deleteCertificate(certificateDto.getId())).withRel("delete"));
        certificateDto.add(linkTo(methodOn(CONTROLLER).createCertificate(certificateDto)).withRel("new"));
        if (certificateDto.getTagNames() != null) {
            certificateDto.getTagNames().forEach(
                    tagDto -> tagDto.add(linkTo(methodOn(TAG_CONTROLLER).findTag(tagDto.getId())).withSelfRel()));
        }
    }

    public static CollectionModel<CertificateDto> getCollectionModelWithPagination(String[] tagNames, String partName,
                                                                             String[] sort, int page, int limit, List<CertificateDto> list) {
        int firstPage = 1;
        int nextPage = list.size() < limit ? page : page + 1;
        int prevPage = page == firstPage ? firstPage : page - 1;
        if (tagNames == null || tagNames.length < 1) {
            tagNames = new String[]{""};
        }
        if (partName == null) {
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


    public static CollectionModel<CertificateDto> getCollectionModelWithPagination(long size, int page, int limit, List<CertificateDto> list) {
        int lastPage = lastPage(size, limit);
        int firstPage = 1;
        int nextPage = page >= lastPage ? lastPage : page + 1;
        int prevPage = page <= firstPage ? firstPage : page - 1;
        Link self = linkTo(methodOn(CertificateController.class).findCertificates(page, limit)).withSelfRel();
        Link next = linkTo(methodOn(CertificateController.class).findCertificates(nextPage, limit)).withRel("next");
        Link prev = linkTo(methodOn(CertificateController.class).findCertificates(prevPage, limit)).withRel("prev");
        Link first = linkTo(methodOn(CertificateController.class).findCertificates(firstPage, limit)).withRel("first");
        Link last = linkTo(methodOn(CertificateController.class).findCertificates(lastPage, limit)).withRel("last");
        return CollectionModel.of(list, first, prev, self, next, last);
    }

    private static int lastPage(long sizeOfList, int limit) {
        return Math.toIntExact((sizeOfList % limit) > 0 ? sizeOfList / limit + 1 : sizeOfList / limit);
    }

}
