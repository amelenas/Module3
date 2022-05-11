package com.epam.esm.service;

import com.epam.esm.dao.entity.Tag;

import java.util.List;

public interface TagService extends CRUDService<Tag> {

    /**
     * Returns {@code Tag} which was most frequently used by the user with the highest amount of order.
     */
    List<Tag> findMostPopularTagOfUserWithHighestCostOfOrder();

    /**
     * @return size of all actual tags
     */
    long findSize();
}
