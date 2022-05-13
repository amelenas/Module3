package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.exception.ExceptionHandler;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final DtoConverter<User, UserDto> userDtoConverter;
    private final  ExceptionHandler exceptionHandler;

    @Autowired
    public UserServiceImpl(UserDao userDao, DtoConverter<User, UserDto> userDtoConverter, ExceptionHandler exceptionHandler) {
        this.userDao = userDao;
        this.userDtoConverter = userDtoConverter;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public UserDto create(UserDto userDto) throws ServiceException {
        exceptionHandler.clean();
        User user = userDtoConverter.convertToEntity(userDto);
        if (!Validator.validateName(user.getUserName(), exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(PROBLEM_CREATE, user);
        return userDtoConverter.convertToDto(userDao.create(user).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler)){
            throw new ServiceException(exceptionHandler);
        }
        return userDao.delete(id);
    }

    @Override
    public List<UserDto> findAll(int page, int size) throws ServiceException {
        exceptionHandler.clean();
        List<User> result = userDao.findAll(page, size);
        if (result.isEmpty()) {
            exceptionHandler.addException(EMPTY_LIST, result);
            throw new ServiceException(exceptionHandler);
        } else {
            return result.stream()
                    .map(userDtoConverter::convertToDto).collect(Collectors.toList());
        }
    }

    @Override
    public UserDto find(long id) throws ServiceException {
        exceptionHandler.clean();
        if (!Validator.isGreaterZero(id, exceptionHandler) ) {
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(USER_NOT_FOUND, id);
        return userDtoConverter.convertToDto(userDao.find(id).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        exceptionHandler.clean();
        User user = userDtoConverter.convertToEntity(userDto);
        if (!Validator.isGreaterZero(id, exceptionHandler) ||
                !Validator.validateName(user.getUserName(), exceptionHandler)) {
            throw new ServiceException(exceptionHandler);
        }
        exceptionHandler.addException(PROBLEM_UPDATE, user);
        return userDtoConverter.convertToDto(userDao.update(id, user).orElseThrow(() -> new ServiceException(exceptionHandler)));
    }

    @Override
    public long findSize() {
        return userDao.findSize();
    }
}
