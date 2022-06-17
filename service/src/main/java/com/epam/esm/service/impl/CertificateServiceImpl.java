package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private static final String NAME = "name";
    private static final String SORTING_ASC = "ASC";
    private static final String SORTING_DESC = "DESC";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private final DtoConverter<Certificate, CertificateDto> certificateDtoConverter;
    private final CertificateDao certificateDao;
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;

    public CertificateDto create(CertificateDto certificateDto) throws ServiceException {
        Validator.validateTagsDto(certificateDto.getTagNames());
        Certificate certificate = certificateDtoConverter.convertToEntity(certificateDto);
        Validator.validateCertificate(certificate);
        Set<Tag> tagNames = new HashSet<>();
        if (!certificate.getTagNames().isEmpty()) {
            for (Tag tag : certificate.getTagNames()) {
                Validator.validateName(tag.getName());
                Optional<Tag> tagControl = tagRepository.findTagByName(tag.getName());
                if (tagControl.isPresent()) {
                    tagNames.add(tagControl.get());
                } else {
                    tagNames.add(tagRepository.save(new Tag(tag.getName())));
                }
            }
            certificate.setTagNames(tagNames);
            return certificateDtoConverter.convertToDto(certificateRepository.save(certificate));
        }
        throw new ServiceException(TAG_EMPTY);
    }

    public void delete(Integer id) throws ServiceException {
        Validator.isGreaterZero(id);
        if (certificateRepository.existsById(id)) {
            certificateRepository.deleteById(id);
        }else {
        throw new ServiceException(ERR_NO_SUCH_CERTIFICATES);}
    }

    public CertificateDto update(Integer id, Map<String, Object> updates) throws ServiceException {
        Validator.isGreaterZero(id);
        if (!certificateRepository.existsById(id)) {
            throw new ServiceException(ERR_NO_SUCH_CERTIFICATES);
        }
        if (updates.isEmpty()) {
            throw new ServiceException(EMPTY_LIST);
        }
        Validator.isUpdatesValid(updates);
        if (updates.containsKey(NAME)) {
            certificateRepository.updateName(id, updates.get(NAME).toString());
        } else if (updates.containsKey(DESCRIPTION)) {
            certificateRepository.updateDescription(id, updates.get(DESCRIPTION).toString());
        } else if (updates.containsKey(PRICE)) {
            certificateRepository.updatePrice(id, (Double) updates.get(PRICE));
        } else if (updates.containsKey(DURATION)) {
            certificateRepository.updateDuration(id, (Integer) updates.get(DURATION));
        }
        return certificateDtoConverter.convertToDto(certificateRepository.findById(id).orElseThrow(() ->
                new ServiceException(EXTRACTING_OBJECT_ERROR)));
    }

    public List<CertificateDto> findCertificatesByAnyParams(String[] tagNames, String substr,
                                                            String[] sort, String sortDirection, int skip, int limit) {
        List<Tag> tags = null;
        if (tagNames != null) {
            for (String tagName : tagNames) {
                tags = new ArrayList<>();
                Validator.validateName(tagName);
                tags.add(tagRepository.findTagByName(tagName).orElseThrow(() -> new ServiceException(TAG_NOT_FOUND)));
            }
        }
        if (sortDirection.contains(SORTING_ASC) || sortDirection.contains(SORTING_DESC)) {
            List<Certificate> result = certificateDao.findByAnyParams(tags, substr, sortDirection, skip, limit, sort);
            if (result.isEmpty()) {
                throw new ServiceException(ERR_NO_SUCH_CERTIFICATES);
            }
            return result.stream().map(certificateDtoConverter::convertToDto).collect(Collectors.toList());
        }
        throw new ServiceException(EXTRACTING_OBJECT_ERROR);
    }

    public CertificateDto find(Integer id) throws ServiceException {
        Validator.isGreaterZero(id);
        return certificateDtoConverter.convertToDto(certificateRepository.findById(id).orElseThrow(() ->
                new ServiceException(ERR_NO_SUCH_CERTIFICATES)));
    }

    public CertificateDto update(Integer id, CertificateDto certificateDto) {
        Validator.isGreaterZero(id);
        Validator.validateTagsDto(certificateDto.getTagNames());
        if (!certificateRepository.existsById(id)) {
            throw new ServiceException(ERR_NO_SUCH_CERTIFICATES);
        }
        Certificate certificate = certificateDtoConverter.convertToEntity(certificateDto);
        Validator.validateCertificate(certificate);
        Optional<Certificate> result = certificateRepository.findById(id);
        if (result.isPresent()) {
            certificate.setId(id);
            certificate.setCreateDate(result.get().getCreateDate());
        } else {
            throw new ServiceException(ERR_NO_SUCH_CERTIFICATES);
        }
        Set<Tag> tagNames = new HashSet<>();
        if (!certificate.getTagNames().isEmpty()) {
            for (Tag tag : certificate.getTagNames()) {
                Validator.validateName(tag.getName());
                Optional<Tag> resultTag = tagRepository.findTagByName(tag.getName());
                if (resultTag.isPresent()) {
                    tagNames.add(resultTag.get());
                } else {
                    tagNames.add(tagRepository.save(tag));
                }
            }
            certificate.setTagNames(tagNames);
            Certificate resultCertificate = certificateRepository.save(certificate);
            resultCertificate.setLastUpdateDate(Instant.now());
            return certificateDtoConverter.convertToDto(resultCertificate);
        }
        throw new ServiceException(TAG_EMPTY);
    }

    public Page<CertificateDto> findAll(Pageable pageable) throws ServiceException {
        Page<Certificate> certificates = certificateRepository.findAll(pageable);
        if (certificates.isEmpty()) {
            throw new ServiceException(EMPTY_LIST);
        } else {
            return certificates.map(certificateDtoConverter::convertToDto);
        }
    }
}

