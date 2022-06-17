package com.epam.esm.dao;

import com.epam.esm.dao.entity.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {
    Optional<Tag> findTagByName(String name);
}
