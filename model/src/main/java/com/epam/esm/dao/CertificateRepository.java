package com.epam.esm.dao;

import com.epam.esm.dao.entity.Certificate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CertificateRepository extends PagingAndSortingRepository<Certificate, Integer> {
    @Modifying
    @Transactional
    @Query("Update Certificate c set c.name =:name where c.id=:id")
    void updateName(Integer id, String name);
    @Modifying
    @Transactional
    @Query("Update Certificate c set c.price =:price where c.id=:id")
    void updatePrice(Integer id, Double price);
    @Modifying
    @Transactional
    @Query("Update Certificate c set c.description =:description where c.id=:id")
    void updateDescription(Integer id, String description);
    @Modifying
    @Transactional
    @Query("Update Certificate c set c.duration =:duration where c.id=:id")
    void updateDuration(Integer id, Integer duration);
}
