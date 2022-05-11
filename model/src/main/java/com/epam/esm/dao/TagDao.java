package com.epam.esm.dao;

import com.epam.esm.dao.entity.Tag;

import java.util.Optional;

/**
 * Interface {@code TagDao} describes operations for working with Tag database table.
 */

public interface TagDao extends CRUDDao<Tag> {

    /**
     * Returns {@code true} if tag with @param name exist.
     * @return {@code false} if tag with @param name doesn't exist.
     */
    Optional<Tag> findByName(String name);

    /**
     * Returns {@code Tag} which was most frequently used by the user with the highest amount of order.
     */
    Optional<Tag> findMostPopularTagOfUserWithHighestCostOfOrder();

    /**
     * @return size of all actual tags
     */
    Long findSize();

}