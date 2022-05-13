package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private final DtoConverter<Certificate, CertificateDto> certificateDtoConverter;
    private final CertificateDao certificateDao;
    private final TagDao tagDao;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public CertificateServiceImpl(DtoConverter<Certificate, CertificateDto> certificateDtoConverter, CertificateDao certificateDao,
                                  TagDao tagDao, ExceptionHandler exceptionHandler) {
        this.certificateDtoConverter = certificateDtoConverter;
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public CertificateDto create(CertificateDto certificateDto) throws ServiceException {
        Certificate certificate = certificateDtoConverter.convertToEntity(certificateDto);
        exceptionHandler.clean();
        if (!Validator.validateCertificate(certificate, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Set<Tag> tagNames = new HashSet<>();
        if (certificate.getTagNames() != null) {
            for (Tag tag : certificate.getTagNames()) {
                Optional<Tag> tagControl = tagDao.findByName(tag.getName());
                if (!tagControl.isPresent()) {
                    tagNames.add(tagDao.create(new Tag(tag.getName())).get());
                } else {
                    tagNames.add(tagControl.get());
                }
            }
        }
        certificate.setTagNames(tagNames);
        exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, certificateDto);
        return  certificateDtoConverter.convertToDto(certificateDao.create(certificate).orElseThrow(() -> new ServiceException(exceptionHandler)));

    }

    @Override
    public boolean delete(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        return certificateDao.delete(id);
    }

    @Override
    public CertificateDto update(long id, Map<String, Object> updates) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)){
            exceptionHandler.addException(BAD_ID, updates);
            throw new ServiceException(exceptionHandler);
        }
        if (updates.isEmpty()) {
            exceptionHandler.addException(EMPTY_LIST, updates);
            throw new ServiceException(exceptionHandler);
        }
        if(!Validator.isUpdatesValid(updates, exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, updates);
        return certificateDtoConverter.convertToDto(certificateDao.update(id, updates).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public List<CertificateDto> findCertificatesByAnyParams(String[] tagNames, String substr, String[] sort, int page, int limit) {
        exceptionHandler.clean();
        List<Tag> tags = null;
        if (tagNames != null) {
            for (String tagName : tagNames) {
                tags = new ArrayList<>();
                if (!Validator.validateName(tagName, exceptionHandler)) {
                    throw new ServiceException(exceptionHandler);
                }
                exceptionHandler.addException(TAG_NOT_FOUND, tagName);
                tags.add(tagDao.findByName(tagName).orElseThrow(()-> new ServiceException(exceptionHandler)));
            }
        }
        Integer skip = (page - 1) * limit;
        List<Certificate> result = certificateDao.findByAnyParams(tags, substr, skip, limit, sort);
        if (result.isEmpty()) {
            exceptionHandler.addException(ERR_NO_SUCH_CERTIFICATES, result);
            throw new ServiceException(exceptionHandler);
        }
        return result.stream().map(certificateDtoConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public CertificateDto find(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(ERR_NO_SUCH_CERTIFICATES, id);
        return certificateDtoConverter.convertToDto(certificateDao.find(id).orElseThrow(() -> new ServiceException(exceptionHandler)));
        }

    @Override
    public CertificateDto update(long id, CertificateDto certificateDto) {
        exceptionHandler.clean();
        Certificate certificate = certificateDtoConverter.convertToEntity(certificateDto);
        if (!Validator.isGreaterZero(id, exceptionHandler) ||
                !Validator.validateCertificate(certificate, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<Certificate> result = certificateDao.find(id);
        if (result.isPresent()) {
            certificate.setId(id);
            certificate.setCreateDate(result.get().getCreateDate());
        }
        Set<Tag> tagNames = new HashSet<>();
        if (certificate.getTagNames() != null) {
            for (Tag tag : certificate.getTagNames()) {
                Optional<Tag> resultTag = tagDao.findByName(tag.getName());
                if (resultTag.isPresent()) {
                    tagNames.add(resultTag.get());
                } else {
                    tagNames.add(tagDao.create(tag).get());
                }
            }
            certificate.setTagNames(tagNames);
        }
        Optional<Certificate> updatedCertificate = certificateDao.update(id, certificate);
        if (updatedCertificate.isPresent()) {
            return certificateDtoConverter.convertToDto(updatedCertificate.get());
        } else {
            exceptionHandler.addException(ERR_NO_SUCH_CERTIFICATES, id);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public List<CertificateDto> findAll(int skip, int size) throws ServiceException {
        exceptionHandler.clean();
        List<Certificate> certificates = certificateDao.findAll(skip, size);
        if (certificates.isEmpty()) {
            exceptionHandler.addException(EMPTY_LIST, certificates);
            throw new ServiceException(exceptionHandler);
        } else {
            return certificates.stream().map(certificateDtoConverter::convertToDto).collect(Collectors.toList());
        }
    }

    @Override
    public long findSize() {
        return certificateDao.findSize();
    }
}

