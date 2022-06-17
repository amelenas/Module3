package com.epam.esm.dao.entity.audit;

import com.epam.esm.dao.entity.Certificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class AuditCertificateListener {
    @PrePersist
    public void createCertificate(Certificate certificate) {
        certificate.setCreateDate(Instant.now());
        certificate.setLastUpdateDate(Instant.now());
        setCreateDate(certificate);
        setUpdateDate(certificate);
    }
    @PreUpdate
    public void updateCertificate(Certificate certificate) {
        certificate.setLastUpdateDate(Instant.now());
        setUpdateDate(certificate);
    }

    private void setCreateDate(Certificate certificate) {
        certificate.setCreateDate(Instant.now());
    }

    private void setUpdateDate(Certificate certificate) {
        certificate.setLastUpdateDate(Instant.now());
    }
}
