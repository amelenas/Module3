package com.epam.esm.service;

import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface CRUDService<T> {
    /**
     * @param item entity used to create entity
     * @return created entity
     */
    T create(T item) throws ServiceException;

    /**
     * Returns {@code true} if item was deleted.
     * @return {@code false} if item wasn't deleted.
     */
    boolean delete(long id) throws ServiceException;

    /**
     * @return a list of all items.
     */
    List<T> findAll(int skip, int size) throws ServiceException;

    /**
     * @return entity that was found by @param id
     */
    T find(long id) throws ServiceException;

    /**
     * @return entity that was updated by @param id
     */
    T update(long id, T item);

    /**
     * @return size of all actual entities
     */
    long findSize();
}
