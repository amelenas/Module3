package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/certificates_script.sql")
class CertificateDaoImplTest {
    Certificate certificate = new Certificate(2, "Music store",
            "Music store description", 100.0, 12, Instant.EPOCH, Instant.EPOCH);
    @Autowired
    @Qualifier("certificateDaoImpl")
    CertificateDao certificateDao;
    @Autowired
    CertificateRepository certificateRepository;

    @Test
    void deleteCertificateById_trueTest() {
        certificateRepository.deleteById(1);
        assertFalse(certificateRepository.existsById(1));
    }

    @Test
    void getCertificates_positiveTest() {
        assertEquals(6, certificateRepository.
                findAll(PageRequest.of(0, 15)).getTotalElements());
    }

    @Test
    void getCertificateById_positiveTest() {
        Certificate certificateFromDB = certificateRepository.findById(2).get();
        assertEquals(certificate.getName(), certificateFromDB.getName());
        assertEquals(certificate.getDescription(), certificateFromDB.getDescription());
    }

    @Test
    void updateCertificates_updateSomeFieldsTest() {
        String name = "Sport";
        Map<String, Object> updates = new TreeMap<>();
        updates.put("name", name);
        updates.put("price", 50.0);
        updates.put("description", "Sport");
        certificateRepository.updateName(2, String.valueOf(updates.get("name")));
        certificateRepository.updatePrice(2, (Double) updates.get("price"));
        certificateRepository.updateDescription(2, String.valueOf(updates.get("description")));
        certificateRepository.updateDuration(2, 1);
        assertEquals(name, certificateRepository.findById(2).get().getName());
    }

    @Test
    void findByAnyParams_positiveTest() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("Food"));
        assertEquals("Pizza", certificateDao.findByAnyParams(tagList, "Pi", "ASC", 0, 5, "name").get(0).getName());
    }
}
