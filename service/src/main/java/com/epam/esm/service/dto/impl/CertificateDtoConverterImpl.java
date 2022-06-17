package com.epam.esm.service.dto.impl;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.dto.entity.TagDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CertificateDtoConverterImpl implements DtoConverter<Certificate, CertificateDto> {
    private final ModelMapper modelMapper;

    private final DtoConverter<Tag, TagDto> tagDtoConverter;

    @Override
    public Certificate convertToEntity(CertificateDto certificateDto) {
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        if (certificateDto.getTagNames() != null
                || !certificateDto.getTagNames().isEmpty()) {
            Set<Tag> tags = certificateDto.getTagNames().stream()
                    .map(tagDtoConverter::convertToEntity)
                    .collect(Collectors.toSet());
            certificate.setTagNames(tags);
        }
        return certificate;
    }

    @Override
    public CertificateDto convertToDto(Certificate certificate) {
        CertificateDto dto = modelMapper.map(certificate, CertificateDto.class);
        if (certificate.getTagNames() != null) {
            List<TagDto> tags = certificate.getTagNames().stream().map(tagDtoConverter::convertToDto)
                    .collect(Collectors.toList());
            dto.setTagNames(tags);
        }
        return dto;
    }
}