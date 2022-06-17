package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.Tag;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private static final Integer USER_ID = 1;
    Tag tag = new Tag(1, "Appliances");
    @Mock
    private TagRepository tagRepository;
    @Mock
    DtoConverter<Tag, TagDto> dtoConverter = Mockito.mock(TagConverterImpl.class);
    @InjectMocks
    TagServiceImpl tagServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CertificateRepository certificateRepository;

    @Test
    void deleteTag_CorrectValue_Test() throws ServiceException {
        when(tagRepository.existsById(tag.getId())).thenReturn(true);
        tagServiceImpl.delete(tag.getId());
        verify(tagRepository).deleteById(tag.getId());
    }

    @Test
    void deleteTag_ZeroValue_ServiceException_Test() {
        assertThrows(ServiceException.class, () -> tagServiceImpl.delete(-1));
    }

    @Test
    void findAllTags_Test() throws ServiceException {
        Page<Tag> mockList = mockPage();
        Page<TagDto> expList = expectedPage();
        Pageable pageable = PageRequest.of(2, 2);
        when(tagRepository.findAll(pageable)).thenReturn(mockList);
        Page<TagDto> actualList = tagServiceImpl.findAll(pageable);
        assertEquals(expList.getTotalElements(), actualList.getTotalElements());
    }

    @Test
    void findTag_ZeroValue_ServiceException_Test() {
        assertThrows(ServiceException.class, () -> tagServiceImpl.find(0));
    }

    @Test
    void findTag_CorrectValue_Test() throws ServiceException {
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        tagServiceImpl.find(tag.getId());
        verify(tagRepository).findById(tag.getId());

    }

    @Test
    void findMostPopularTagOfUserWithHighestCostOfOrder_Test() {
        Certificate certificate = new Certificate(2, "Pet Store",
                "Pet Store", 200.0, 12, Instant.EPOCH, Instant.EPOCH);
        Tag tag = new Tag(1, "Animal");
        Set<Tag> tagNames = new HashSet<>();
        tagNames.add(tag);
        certificate.setTagNames(tagNames);
        Order order = new Order(1, 1, 1, Instant.EPOCH);
        List<Order> mockList = new ArrayList<>();
        mockList.add(order);
        when(userRepository.findUserIdWithTheBiggestSumOrders()).thenReturn(USER_ID);
        when(orderRepository.findOrdersByUserId(USER_ID)).thenReturn(mockList);
        when(certificateRepository.findById(USER_ID)).thenReturn(Optional.of(certificate));
        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        assertEquals(tagServiceImpl.findMostPopularTagOfUserWithHighestCostOfOrder(), dtoConverter.convertToDto(tag));
    }

    @Test
    void create_Tag_Test() {
        Tag tag = new Tag(1, "Animal");
        TagDto tagDto = TagDto.builder().id(1).name("Animal").build();
        when(tagRepository.findTagByName(tagDto.getName())).thenReturn(Optional.empty());
        assertEquals(tagServiceImpl.create(tagDto), dtoConverter.convertToDto(tag));
    }

    private Page<Tag> mockPage() {
        List<Tag> mockList = new ArrayList<>();
        mockList.add(Tag.builder().id(1).name("tag1").build());
        mockList.add(Tag.builder().id(2).name("tag2").build());
        mockList.add(Tag.builder().id(3).name("tag3").build());
        return new PageImpl<>(mockList);
    }

    private Page<TagDto> expectedPage() {
        List<TagDto> expList = new ArrayList<>();
        expList.add(TagDto.builder().id(1).name("tag1").build());
        expList.add(TagDto.builder().id(2).name("tag2").build());
        expList.add(TagDto.builder().id(3).name("tag3").build());
        return new PageImpl<>(expList);
    }
}
