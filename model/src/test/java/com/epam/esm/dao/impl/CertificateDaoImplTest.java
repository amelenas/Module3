package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/certificates_script.sql")

public class CertificateDaoImplTest {

    Certificate certificate = new Certificate(2, "Music store",
            "Music store description", 100.0, 12, Instant.EPOCH,  Instant.EPOCH);

    @Autowired
    CertificateDao certificateDao;

    @Test
    void getCertificates_positiveTest() {
        assertEquals(6, certificateDao.findAll(0, 15).size());
    }

    @Test
    void getCertificateById_positiveTest() {
        Certificate certificateFromDB = certificateDao.find(2).get();
        certificate.setTagNames(certificateFromDB.getTagNames());
        certificate.setLastUpdateDate(certificateFromDB.getLastUpdateDate());
        certificate.setCreateDate(certificateFromDB.getCreateDate());
        certificate.setLock(0);
        assertEquals(certificate, certificateFromDB);
    }

    @Test
    void createCertificate_CreatedAndReturnedValueEqualsCertificateTest() {
        Set<Tag> tagList = new HashSet<>();
        tagList.add(new Tag("Sport1"));
        Certificate certificate = new Certificate("Gym",
                "1 hour of gym", 120, 12, tagList);
        certificate.setDuration(12);
        Certificate certificateFromDB = certificateDao.create(certificate).get();
        certificate.setId(certificateFromDB.getId());
        certificate.setCreateDate(certificateFromDB.getCreateDate());
        certificate.setLastUpdateDate(certificateFromDB.getLastUpdateDate());
        certificate.setTagNames(certificateFromDB.getTagNames());
        assertEquals(certificate, certificateFromDB);
        certificateDao.delete(6);
    }

    @Test
    void deleteCertificateById_trueTest() {
        assertTrue(certificateDao.delete(1));
    }

    @Test
    void deleteCertificateById_falseTest() {
        assertFalse(certificateDao.delete(568));
    }

    @Test
    void updateCertificates_updateSomeFieldsTest() {
        Map<String, Object> updates = new TreeMap<>();
        updates.put("name", "Sport");
        updates.put("price", 50.0);
        updates.put("description", "Sport");

        Optional<Certificate> certificateFromDb = certificateDao.update(2, updates);
        assertEquals(certificateDao.find(2).get().getName(), certificateFromDb.get().getName());
    }

    @Test
    void update_positiveTest() {
        assertEquals(certificate.getName(), certificateDao.update(2, certificate).get().getName());
    }

    @Test
    void findByAnyParams_positiveTest() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("Food"));
        assertEquals(certificateDao.findByAnyParams(tagList, "Pi", 0, 5, "-name").get(0).getName(), "Pizza");
    }

    @Test
    void findSize() {
       assertEquals(certificateDao.findSize(), 6);
    }
}
