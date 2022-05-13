package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@code CRUDDao} describes similar operations for working with database tables.
 */
public interface CRUDDao<T> {
    /**
     * @param item entity used to create entity
     * @return created entity
     */
    Optional<T> create(T item) ;

    /**
     * Returns {@code true} if item was deleted.
     * @return {@code false} if item wasn't deleted.
     */
    boolean delete(long id);

    /**
     * @return a list of all items.
     */
    List<T> findAll(int page, int size);

    /**
     * @return entity that was found by @param id
     */
    Optional<T> find(long id);

    /**
     * @return entity that was updated by @param id
     */
    Optional<T> update(long id, T item);

}
