package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.dto.entity.CertificateDto;
import com.epam.esm.controller.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

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
}
