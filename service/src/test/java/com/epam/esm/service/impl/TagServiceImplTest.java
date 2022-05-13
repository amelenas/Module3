package com.epam.esm.service.impl;

import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.dto.impl.TagConverterImpl;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    Tag tag = new Tag(1, "Appliances");

    @Mock
    TagDaoImpl tagDao = Mockito.mock(TagDaoImpl.class);
    @Mock
    DtoConverter<Tag, TagDto> dtoConverter = Mockito.mock(TagConverterImpl.class);
    @InjectMocks
    TagServiceImpl tagServiceImpl;

    @Test
    void createTag_NullValue_ServiceExceptionTest(){
        assertThrows(NullPointerException.class, () -> tagServiceImpl.create(dtoConverter.convertToDto(new Tag(null))));
    }

    @Test
    void deleteTag_CorrectValueTest() throws ServiceException {

        tagServiceImpl.delete(tag.getId());
        Mockito.verify(tagDao).delete(tag.getId());
    }
    @Test
    void deleteTag_ZeroValue_ServiceExceptionTest() {
        assertThrows(ServiceException.class, () -> tagServiceImpl.delete(0));
    }

    @Test
    void findAllTagsTest() throws ServiceException {
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Mockito.when(tagDao.findAll(1, 15)).thenReturn(tags);
        tagServiceImpl.findAll(1, 15);
        Mockito.verify(tagDao).findAll(1, 15);
    }

    @Test
    void findTag_ZeroValue_ServiceExceptionTest()  {
        assertThrows(ServiceException.class, () -> tagServiceImpl.find(0));
    }

    @Test
    void findTag_CorrectValueTest() throws ServiceException {
        Mockito.when(tagDao.find(tag.getId())).thenReturn(Optional.of(tag));
        tagServiceImpl.find(tag.getId());
        Mockito.verify(tagDao).find(tag.getId());
    }
}