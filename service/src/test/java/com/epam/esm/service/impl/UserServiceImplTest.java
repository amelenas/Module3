package com.epam.esm.service.impl;

import com.epam.esm.dao.entity.User;
import com.epam.esm.dao.impl.UserDaoImpl;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.dto.impl.UserConverterImpl;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDaoImpl userDao = Mockito.mock(UserDaoImpl.class);
    @Mock
    private DtoConverter <User, UserDto> dtoConverter = Mockito.mock(UserConverterImpl.class);
    @InjectMocks
    private UserServiceImpl userService;
    UserDto userDto = new UserDto(1, "Petr");
    @Test
    void create_PositiveTest() {
        User user = new User(1, "Petr");
        Mockito.when(dtoConverter.convertToEntity(userDto)).thenReturn(user);
        Mockito.when(userDao.create(user)).thenReturn(Optional.of(user));
        userService.create(userDto);
    }

    @Test
    void delete_PositiveTest() {
        Mockito.when(userDao.delete(1)).thenReturn(true);
        assertTrue(userService.delete(1));
    }

    @Test
    void delete_InvalidIdTest() {
        assertThrows(ServiceException.class, () -> userService.delete(-1));
    }

    @Test
    void findAll_PositiveTest() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "Victor"));
        Mockito.when(userDao.findAll(1, 15)).thenReturn(users);
        userService.findAll(1,15);
        Mockito.verify(userDao).findAll(1, 15);
    }

    @Test
    void find_PositiveTest() {
        Mockito.when(userDao.find(2)).thenReturn(Optional.of(new User(2, "Victor")));
        userService.find(2);
        Mockito.verify(userDao).find(2);
    }

    @Test
    void find_IncorrectIdTest() {
        assertThrows(ServiceException.class, () -> userService.find(-1));
    }

}