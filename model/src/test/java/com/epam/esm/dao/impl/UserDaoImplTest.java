package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/certificates_script.sql")
class UserDaoImplTest {
    @Autowired
    private UserDao userDao;

    @Test
    void create_trueTest() {
        User expectedUser = new User("Sophia");
        User actualUser = userDao.create(new User(1, "Sophia")).get();
        expectedUser.setUserId(actualUser.getUserId());
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void delete_trueTest() {
        assertTrue(userDao.delete(1));
    }

    @Test
    void delete_falseTest() {
        assertFalse(userDao.delete(156));
    }

    @Test
    void findAll_trueTest() {
        List<User> userExpected = new ArrayList<>();
        userExpected.add(new User(1, "Valentin"));
        userExpected.add(new User(2, "Victor"));
        userExpected.add(new User(3, "Laura"));
        userExpected.add(new User(4, "Denchik"));
        userExpected.add(new User(5, "Stepan"));
        assertEquals(userDao.findAll(0,15), userExpected);
    }

    @Test
    void find_trueTest() {
        User userExpected = new User(1, "Valentin");
        assertEquals(userExpected, userDao.find(1).get());
    }

    @Test
    void update_trueTest() {
        User userExpected = new User(1,"German");
        assertEquals(userExpected, userDao.update(1, new User("German")).get());
    }
}
