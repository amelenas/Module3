package com.epam.esm.dao.entity.audit;

import com.epam.esm.dao.entity.Certificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class AuditCertificateListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createCertificate(Certificate certificate) {
        certificate.setLock(LOCK);
        setCreateDate(certificate);
        setUpdateDate(certificate);
    }

    @PreUpdate
    public void updateCertificate(Certificate certificate) {
        certificate.setLock(LOCK);
        setUpdateDate(certificate);
    }

    private void setCreateDate(Certificate certificate) {
        certificate.setCreateDate(Instant.now());
    }

    private void setUpdateDate(Certificate certificate) {
        certificate.setLastUpdateDate(Instant.now());
    }
}
