package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.Tag;
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
class TagDaoImplTest {
    Tag tagCosmetics = new Tag(1, "Cosmetics");
    Tag tagMusic = new Tag(2, "Music store");
    Tag tagFitness = new Tag(3, "Fitness");
    Tag tagFood = new Tag(4, "Food");

    @Autowired
    private TagDao tagDao;

    @Test
    void findAllTags_Expected4Tags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(tagCosmetics);
        tags.add(tagMusic);
        tags.add(tagFitness);
        tags.add(tagFood);

        assertEquals(4, tagDao.findAll(0,15).size());
    }

    @Test
    void findTagById_ExpectedEqualsTest() {
        assertEquals(tagCosmetics.getName(), tagDao.find(1).get().getName());
    }

    @Test
    void findTagByName_ExpectedEqualsTest() {
        assertEquals(tagFitness.getName(), tagDao.findByName("Fitness").get().getName());
    }


    @Test
    void deleteTag_trueTest() {
        assertTrue(tagDao.delete(2));

    }
    @Test
    void deleteTag_FalseTest() {
        assertFalse(tagDao.delete(55));
    }

    @Test
    void create_newTag_EqualsTrueTest() {
        Tag newTag = new Tag("Car");
        Tag tagFromDB = tagDao.create(newTag).get();
        newTag.setId(tagFromDB.getId());
        assertEquals(tagFromDB, newTag);
    }

    @Test
    void findMostPopularTagOfUserWithHighestCostOfOrder() {
        assertEquals(1, tagDao.findMostPopularTagOfUserWithHighestCostOfOrder().get().getId());
    }
}
