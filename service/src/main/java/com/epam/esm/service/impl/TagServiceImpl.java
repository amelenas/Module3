package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag create(Tag tag) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.validateName(tag.getName(), exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        if (tagDao.findByName(tag.getName()).isPresent()) {
            exceptionHandler.addException(TAG_EXIST, tag.getName());
            throw new ServiceException(exceptionHandler);
        }
        Optional<Tag> result = tagDao.create(tag);
        if (result.isPresent()) {
            return result.get();
        }
        exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, result);
        throw new ServiceException(exceptionHandler);
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        return tagDao.delete(id);
    }

    @Override
    public List<Tag> findAll(int page, int size) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        List<Tag> tags = tagDao.findAll(page, size);
        if (tags.size() > 0) {
            return tags;
        } else {
            exceptionHandler.addException(EMPTY_LIST, tags);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public Tag find(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<Tag> result = tagDao.find(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(TAG_NOT_FOUND, id);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public Tag update(long id, Tag tag) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler) ||
                !Validator.validateName(tag.getName(), exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<Tag> result = tagDao.update(id, tag);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(PROBLEM_CREATE, id);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public List<Tag> findMostPopularTagOfUserWithHighestCostOfOrder() {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        Optional<Tag> tagsWithHighestCost = tagDao.findMostPopularTagOfUserWithHighestCostOfOrder();
        List<Tag> result = new ArrayList<>();
        tagsWithHighestCost.ifPresent(result::add);
        if (result.size() > 0) {
            return result;
        }
        exceptionHandler.addException(EMPTY_LIST, tagsWithHighestCost);
        throw new ServiceException(exceptionHandler);
    }

    @Override
    public long findSize() {
        return tagDao.findSize();
    }
}

