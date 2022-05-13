package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";

    private final CertificateDao certificateDao;
    private final TagDao tagDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public Certificate create(Certificate certificate) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
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
        Optional<Certificate> result = certificateDao.create(certificate);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, result);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        return certificateDao.delete(id);
    }

    @Override
    public Certificate update(long id, Map<String, Object> updates) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)){
            exceptionHandler.addException(BAD_ID, updates);
            throw new ServiceException(exceptionHandler);
        }
        if (updates.size() < 1) {
            exceptionHandler.addException(EMPTY_LIST, updates);
            throw new ServiceException(exceptionHandler);
        }
        if (updates.containsKey(NAME)){
            if (!Validator.validateName(updates.get(NAME).toString(), exceptionHandler)){
                exceptionHandler.addException(BAD_NAME, updates);
                throw new ServiceException(exceptionHandler);
            }
        }
        if (updates.containsKey(DESCRIPTION)){
            if (!Validator.validateName(updates.get(DESCRIPTION).toString(), exceptionHandler)){
                exceptionHandler.addException(BAD_NAME, updates);
                throw new ServiceException(exceptionHandler);
            }
        }
        if (updates.containsKey(PRICE)){
            if (Validator.validatePrice((Double) updates.get(PRICE), exceptionHandler)){
                exceptionHandler.addException(BAD_CERTIFICATE_PRICE, updates);
                throw new ServiceException(exceptionHandler);
            }
        }
        if (updates.containsKey(DURATION)){
            if (!Validator.validateDuration((Integer) updates.get(DURATION), exceptionHandler)){
                exceptionHandler.addException(BAD_CERTIFICATE_DURATION, updates);
                throw new ServiceException(exceptionHandler);
            }
        }
        Optional<Certificate> result = certificateDao.update(id, updates);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, result);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public List<Certificate> findCertificatesByAnyParams(String[] tagNames, String substr, String[] sort, int page, int limit) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        List<Tag> tags = null;
        if (tagNames != null) {
            for (String tagName : tagNames) {
                tags = new ArrayList<>();
                if (!Validator.validateName(tagName, exceptionHandler)) {
                    throw new ServiceException(exceptionHandler);
                }
                if (tagDao.findByName(tagName).isPresent()) {
                    tags.add(tagDao.findByName(tagName).get());
                } else {
                    exceptionHandler.addException(TAG_NOT_FOUND, tagName);
                    throw new ServiceException(exceptionHandler);
                }
            }
        }
        Integer skip = (page - 1) * limit;
        List<Certificate> result = certificateDao.findByAnyParams(tags, substr, skip, limit, sort);
        if (result.isEmpty()) {
            exceptionHandler.addException(ERR_NO_SUCH_CERTIFICATES, result);
            throw new ServiceException(exceptionHandler);
        }
        return result;
    }

    @Override
    public Certificate find(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<Certificate> result = certificateDao.find(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(ERR_NO_SUCH_CERTIFICATES, result);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public Certificate update(long id, Certificate certificate) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
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
            return updatedCertificate.get();
        } else {
            exceptionHandler.addException(ERR_NO_SUCH_CERTIFICATES, id);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public List<Certificate> findAll(int skip, int size) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        List<Certificate> certificates = certificateDao.findAll(skip, size);
        if (certificates.size() > 0) {
            return certificates;
        } else {
            exceptionHandler.addException(EMPTY_LIST, certificates);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public long findSize() {
        return certificateDao.findSize();
    }
}

