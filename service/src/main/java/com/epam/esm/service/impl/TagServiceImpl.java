package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;

    private final OrderRepository orderRepository;

    public TagDto create(TagDto tagDto) throws ServiceException {
        if(tagDto == null) {throw new ServiceException(TAG_EMPTY);}
        Validator.validateName(tagDto.getName());
        if (tagRepository.findTagByName(tagDto.getName()).isPresent()){
            throw new ServiceException(TAG_EXIST);
        }
        return tagDtoConverter.convertToDto(tagRepository.save(tagDtoConverter.convertToEntity(tagDto)));
    }

    public void delete(Integer id) throws ServiceException {
        Validator.isGreaterZero(id);
        if (!tagRepository.existsById(id)){
            throw new ServiceException(TAG_NOT_FOUND);
        }else {
            tagRepository.deleteById(id);}
    }
    public Page<TagDto> findAll(Pageable pageable) throws ServiceException {
        Page<Tag> tags = tagRepository.findAll(pageable);
        if (tags.isEmpty()) {
            throw new ServiceException(EMPTY_LIST);
        } else {
            return tags.map(tagDtoConverter::convertToDto);
        }
    }
    public TagDto find(Integer id) throws ServiceException {
        Validator.isGreaterZero(id);
        return tagDtoConverter.convertToDto(tagRepository.findById(id).orElseThrow(() -> new ServiceException(TAG_NOT_FOUND)));
    }
    public TagDto findMostPopularTagOfUserWithHighestCostOfOrder() {
        Integer userId = userRepository.findUserIdWithTheBiggestSumOrders();
        List<Order> usersOrders = orderRepository.findOrdersByUserId(userId);
        List<Certificate> certificates = new ArrayList<>();
        for(Order order : usersOrders){
            certificates.add(certificateRepository.findById(order.getCertificateId()).orElseThrow(()-> new ServiceException(ERR_NO_SUCH_CERTIFICATES)));
        }
        List<Tag> tags = new ArrayList<>();
        for(Certificate certificate : certificates){
           if (!certificate.getTagNames().isEmpty()){
               for (Tag tag : certificate.getTagNames()){
            tags.add(tagRepository.findById(tag.getId()).orElseThrow(()-> new ServiceException(TAG_NOT_FOUND)));
               }
           }
        }

        return tagDtoConverter.convertToDto(tags.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElseThrow(() ->  new ServiceException(EMPTY_LIST)));
    }
}

