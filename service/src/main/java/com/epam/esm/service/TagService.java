package com.epam.esm.service;

import com.epam.esm.service.dto.entity.TagDto;

import java.util.List;

public interface TagService extends CRUDService<TagDto> {

    /**
     * Returns {@code Tag} which was most frequently used by the user with the highest amount of order.
     */
    List<TagDto> findMostPopularTagOfUserWithHighestCostOfOrder();

}
