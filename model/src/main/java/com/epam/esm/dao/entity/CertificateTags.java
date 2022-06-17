package com.epam.esm.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "gift_certificate_tags")
public class CertificateTags implements Serializable {
    private static final long serialVersionUID = 32L;
    @Id
    @Column(name = "gift_certificate_id")
    Integer giftId;

    @Column(name = "tag_id")
    Integer tagId;

    public CertificateTags() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CertificateTags)) return false;
        CertificateTags that = (CertificateTags) o;
        return getGiftId().equals(that.getGiftId()) && getTagId().equals(that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGiftId(), getTagId());
    }

}
