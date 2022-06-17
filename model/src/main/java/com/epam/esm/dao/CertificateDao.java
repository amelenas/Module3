package com.epam.esm.dao;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CertificateDao {
    List<Certificate> findByAnyParams(List<Tag> tags, String substr, String sortDirection,
                                      Integer skip, Integer limit, String... sort);

}
