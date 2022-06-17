package com.epam.esm.service.impl;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.*;
import com.epam.esm.service.dto.impl.UserConverterImpl;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Integer USER_ID = 1;
    private static final String LOGIN = "user1";
    private static final String PASSWORD = "12345";
    private final User user = new User(LOGIN, LOGIN, PASSWORD);
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private DtoConverter<User, UserDto> dtoConverter = Mockito.mock(UserConverterImpl.class);
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void findUserById_Positive_Test() {
        user.setUserId(USER_ID);
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userRepository.save(user));
    }

    @Test
    void findUserById_UserNotFoundException_Test() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> userService.find(USER_ID));
    }

    @Test
    void findUserByLogin_Test() {
        User expected = new User();
        expected.setLogin(LOGIN);
        when(userRepository.findByLogin(LOGIN)).thenReturn(Optional.of(expected));
        User actual = userService.findUserByLogin(LOGIN);
        assertEquals(expected, actual);
    }

    @Test
    void findUserByLogin_UserNotFoundException_Test() {
        when(userRepository.findByLogin(LOGIN)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> userService.findUserByLogin(LOGIN));
    }

    @Test
    void delete_InvalidId_Test() {
        assertThrows(ServiceException.class, () -> userService.delete(-1));
    }
    @Test
    void delete_Positive_Test() {
        userService.delete(USER_ID);
        Mockito.verify(userRepository).deleteById(USER_ID);
    }

    @Test
    void findAllUsers_Test() {
        Page<User> mockList = mockPage();
        Page<UserResponseDto> expList = expectedPage();
        Pageable pageable = PageRequest.of(2, 2);
        when(userRepository.findAll(pageable)).thenReturn(mockList);

        Page<UserResponseDto> actualList = userService.findAll(pageable);

        assertEquals(expList.getTotalElements(), actualList.getTotalElements());
    }

    @Test
    void findCostAndDateOfBuyForUserByOrderId_Positive_Test() {
        CostAndDateOfBuyDto expected = new CostAndDateOfBuyDto(1.0, Instant.EPOCH);
        Integer userId = 1;
        Integer orderId = 1;
        when(userRepository.findCostAndDateOfBuyForUserByOrderId(userId, orderId))
                .thenReturn(Optional.of(new Order(1, 1, 1, Instant.EPOCH)));
        assertEquals(expected, userService.findCostAndDateOfBuyForUserByOrderId(1, 1));
    }

    @Test
    void create_ExistingObject_Test() {
        UserDto userDto = new UserDto(USER_ID, LOGIN, LOGIN, PASSWORD);
        when(dtoConverter.convertToEntity(userDto)).thenReturn(user);
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));
        assertThrows(ServiceException.class, () -> userService.create(userDto));
    }

    @Test
    void authentication_Test() {
        AuthenticationDto authenticationDto = AuthenticationDto.builder().login(LOGIN).password(PASSWORD).build();
        AuthenticationResponseDto authenticationResponseDto =AuthenticationResponseDto.builder().token("token").login(LOGIN).build();
        assertEquals(authenticationDto.getLogin(), authenticationResponseDto.getLogin());
    }
    @Test
    void user_Entity_Test() {
    User user = User.builder().userId(1).userName("User1").login("User1").password(PASSWORD).build();
    UserDto userDto = UserDto.builder().id(1).name("User1").login("User1").password(PASSWORD).build();
    assertEquals(user.getLogin(), userDto.getLogin());
    assertEquals(user.getUserName(), userDto.getName());
    assertEquals(user.getPassword(), userDto.getPassword());
    }
        private Page<User> mockPage() {
        List<User> mockList = new ArrayList<>();
        mockList.add(User.builder().userId(1).userName("User1").login("User1").password(PASSWORD).build());
        mockList.add(User.builder().userId(2).userName("User2").login("User2").password(PASSWORD).build());
        mockList.add(User.builder().userId(3).userName("User3").login("User3").password(PASSWORD).build());
        return new PageImpl<>(mockList);
    }

    private Page<UserResponseDto> expectedPage() {
        List<UserResponseDto> expList = new ArrayList<>();
        expList.add(UserResponseDto.builder().id(1).name("User1").login("User1").build());
        expList.add(UserResponseDto.builder().id(2).name("User2").login("User2").build());
        expList.add(UserResponseDto.builder().id(3).name("User3").login("User3").build());
        return new PageImpl<>(expList);
    }
}