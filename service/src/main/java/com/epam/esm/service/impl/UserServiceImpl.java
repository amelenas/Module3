package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(User user) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.validateName(user.getUserName(), exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        Optional<User> result = userDao.create(user);
        if(result.isPresent()){
            return result.get();
        }
        exceptionHandler.addException(PROBLEM_CREATE, user);
        throw new ServiceException(exceptionHandler);
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        return userDao.delete(id);
    }

    @Override
    public List<User> findAll(int page, int size) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        List<User> result = userDao.findAll(page, size);
        if (result.size() > 0) {
            return result;
        } else {
            exceptionHandler.addException(EMPTY_LIST, result);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public User find(long id) throws ServiceException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler) ) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<User> result = userDao.find(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(USER_NOT_FOUND, id);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public User update(long id, User user) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (!Validator.isGreaterZero(id, exceptionHandler) ||
                !Validator.validateName(user.getUserName(), exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        Optional<User> result = userDao.update(id, user);
        if (result.isPresent()) {
            return result.get();
        } else {
            exceptionHandler.addException(PROBLEM_UPDATE, user);
            throw new ServiceException(exceptionHandler);
        }
    }

    @Override
    public Long findSize() {
        return userDao.findSize();
    }
}
