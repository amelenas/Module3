package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.Role;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.CostAndDateOfBuyDto;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.dto.entity.UserResponseDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.epam.esm.service.exception.ExceptionMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final DtoConverter<User, UserDto> userDtoConverter;
    private final DtoConverter<User, UserResponseDto> userResponseDtoDtoConverter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User findUserByLogin(String login) {
        Optional<User> optionalUser = userRepository.findByLogin(login);
        return optionalUser.orElseThrow(() -> new ServiceException("user.not.found"));
    }

    public UserResponseDto create(UserDto userDto) throws ServiceException {
        User user = userDtoConverter.convertToEntity(userDto);
        Validator.validateName(user.getUserName());
        Validator.validateName(user.getLogin());
        Validator.validatePassword(user.getPassword());
        if (userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            throw new ServiceException(USER_EXIST);
        }
        Role role = roleRepository.findByRoleType(Role.RoleType.USER);
        user.setRole(role);
        user.setPassword(encode(user.getPassword()));
        User result = userRepository.save(user);
        return userResponseDtoDtoConverter.convertToDto(result);
    }


    public void delete(Integer id) throws ServiceException {
        Validator.isGreaterZero(id);
        userRepository.deleteById(id);
    }

    public Page<UserResponseDto> findAll(Pageable pageable) throws ServiceException {
        Page<User> result = userRepository.findAll(pageable);
        if (result.isEmpty()) {
            throw new ServiceException(EMPTY_LIST);
        } else {
            return result.map(userResponseDtoDtoConverter::convertToDto);
        }
    }

    public UserResponseDto find(Integer id) throws ServiceException {
        Validator.isGreaterZero(id);
        return userResponseDtoDtoConverter.convertToDto(userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND)));
    }


    public CostAndDateOfBuyDto findCostAndDateOfBuyForUserByOrderId(Integer userId, Integer orderId) {
        Validator.isGreaterZero(userId);
        Validator.isGreaterZero(orderId);
        Order orderResult = userRepository.findCostAndDateOfBuyForUserByOrderId(userId, orderId).orElseThrow(() -> new ServiceException(ORDER_NOT_FOUND));
        return CostAndDateOfBuyDto.builder().cost(orderResult.getCost()).dateOfBuy(orderResult.getDateOfBuy()).build();
    }

    private String encode(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
