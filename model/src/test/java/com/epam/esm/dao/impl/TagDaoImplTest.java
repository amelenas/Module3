package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/certificates_script.sql")
class TagDaoImplTest {
    Tag tagCosmetics = new Tag(1, "Cosmetics");
    Tag tagFitness = new Tag(3, "Fitness");

    @Autowired
    private TagRepository tagRepository;

    @Test
    void findAllTags_Expected4Tags() {
        assertEquals(4, tagRepository.findAll(PageRequest.of(0, 15)).getTotalElements());
    }

    @Test
    void findTagById_ExpectedEqualsTest() {
        assertEquals(tagCosmetics.getName(), tagRepository.findById(1).get().getName());
    }

    @Test
    void findTagByName_ExpectedEqualsTest() {
        assertEquals(tagFitness.getName(), tagRepository.findTagByName("Fitness").get().getName());
    }

    @Test
    void deleteTag_PositiveTest() {
        tagRepository.deleteById(2);
        assertFalse(tagRepository.existsById(2));
    }

    @Test
    void create_newTag_EqualsTrueTest() {
        Tag newTag = new Tag("Car");
        Tag tagFromDB = tagRepository.save(newTag);
        newTag.setId(tagFromDB.getId());
        assertEquals(tagFromDB, newTag);
    }
}
