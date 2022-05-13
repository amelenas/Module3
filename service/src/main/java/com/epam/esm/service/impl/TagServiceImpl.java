package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    private final  ExceptionHandler exceptionHandler;

    @Autowired
    public TagServiceImpl(TagDao tagDao, DtoConverter<Tag, TagDto> tagDtoConverter, ExceptionHandler exceptionHandler) {
        this.tagDao = tagDao;
        this.tagDtoConverter = tagDtoConverter;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public TagDto create(TagDto tagDto) throws ServiceException {
        exceptionHandler.clean();
        Tag tag = tagDtoConverter.convertToEntity(tagDto);

        if (!Validator.validateName(tag.getName(), exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        if (tagDao.findByName(tag.getName()).isPresent()) {
            exceptionHandler.addException(TAG_EXIST, tag.getName());
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(EXTRACTING_OBJECT_ERROR, tag);
        return tagDtoConverter.convertToDto(tagDao.create(tag).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        return tagDao.delete(id);
    }

    @Override
    public List<TagDto> findAll(int page, int size) throws ServiceException {
        exceptionHandler.clean();
        List<Tag> tags = tagDao.findAll(page, size);
        if (tags.isEmpty()) {
            exceptionHandler.addException(EMPTY_LIST, tags);
            throw new ServiceException(exceptionHandler);
        } else {
            return tags.stream().map(tagDtoConverter::convertToDto).collect(Collectors.toList());
        }
    }

    @Override
    public TagDto find(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(TAG_NOT_FOUND, id);
        return tagDtoConverter.convertToDto(tagDao.find(id).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public TagDto update(long id, TagDto tagDto) {
        exceptionHandler.clean();
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        if (!Validator.isGreaterZero(id, exceptionHandler) ||
                !Validator.validateName(tag.getName(), exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(PROBLEM_CREATE, id);
        return  tagDtoConverter.convertToDto(tagDao.update(id, tag).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public List<TagDto> findMostPopularTagOfUserWithHighestCostOfOrder() {
        exceptionHandler.clean();
        Optional<Tag> tagsWithHighestCost = tagDao.findMostPopularTagOfUserWithHighestCostOfOrder();
        List<Tag> result = new ArrayList<>();
        tagsWithHighestCost.ifPresent(result::add);
        if (result.isEmpty()) {
            exceptionHandler.addException(EMPTY_LIST, tagsWithHighestCost);
            throw new ServiceException(exceptionHandler);
        }
        return result.stream().map(tagDtoConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public long findSize() {
        return tagDao.findSize();
    }
}

