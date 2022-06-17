package com.epam.esm.dao.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.Role;
import com.epam.esm.dao.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/certificates_script.sql")
class UserDaoImplTest {
    List<User> usersExpected = new ArrayList<>();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    void addValues() {
        Role role = roleRepository.findByRoleType(Role.RoleType.USER);

        usersExpected.add(new User(1,"Valentin", "Valentin", "12345", role));
        usersExpected.add(new User(2, "Victor", "Victor", "12345", role));
        usersExpected.add(new User(3, "Laura", "Laura", "12345", role));
        usersExpected.add(new User(4,"Denchik", "Denchik", "12345", role));
        usersExpected.add(new User(5,"Stepan", "Stepan", "12345", role));
    }

    @Test
    void create_trueTest() {
        User expectedUser = new User("Sophia", "Sophia", "12345");
        Role role = roleRepository.findByRoleType(Role.RoleType.USER);

        User actualUser = userRepository.save(new User("Sophia", "Sophia", "12345",role));
        expectedUser.setUserId(actualUser.getUserId());
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void delete_trueTest() {
        userRepository.deleteById(1);
        assertFalse(userRepository.existsById(1));
    }

    @Test
    void findAll_trueTest() {
        assertEquals(userRepository.findAll(PageRequest.of(0, 15)).toList(), usersExpected);
    }

    @Test
    void find_trueTest() {
        User userExpected = usersExpected.get(0);
        assertEquals(userExpected, userRepository.findById(1).get());
    }

    @Test
    void findCostAndDateOfBuyForUserByOrderId_positiveTest() {
        assertEquals(1200, userRepository.findCostAndDateOfBuyForUserByOrderId(3, 3).get().getCost());
    }

    @Test
    void findUserIdWithTheBiggestSumOrders_positiveTest() {
        assertEquals(3, userRepository.findUserIdWithTheBiggestSumOrders());
    }
}
