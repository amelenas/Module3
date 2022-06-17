
package com.epam.esm.service;

import com.epam.esm.service.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CRUDService<T> {

    T create(T item) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    Page<T> findAll(Pageable pageable) throws ServiceException;

    T find(Integer id) throws ServiceException;
}

