package com.epam.esm.dao.entity;

import com.epam.esm.dao.entity.audit.AuditCertificateListener;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Builder
@EntityListeners(AuditCertificateListener.class)
@Getter
@Setter
@ToString
@Table(name = "gift_certificates")
public class Certificate implements Serializable {
    private static final long serialVersionUID = 11L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Size(min=3, max = 50)
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "description")
    private String description;

    @Min(value = 0)
    @Max(value = 1000000)
    @Column(name = "price")
    private double price;

    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "duration")
    private Integer duration;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "last_update_date")
    private Instant lastUpdateDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "gift_certificate_tags",
            joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Tag> tagNames;

    public Certificate() {
    }

    public Certificate(Integer id) {
        this.id = id;
    }

    public Certificate(Integer id, String name,String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;

    }

    public Certificate(String name,String description,
                       double price, Integer duration,
                       Set<Tag> tagNames) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.tagNames = tagNames;
        this.duration = duration;
    }

    public Certificate(Integer id, String name, String description,
                        double price, Integer duration,
                       Set<Tag> tagNames) {

       this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.tagNames = tagNames;
        this.duration = duration;
    }

    public Certificate(String name, String description, double price,
                       Integer duration, Instant createDate, Instant lastUpdateDate) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.description = description;
    }

    public Certificate(Integer id, String name, String description, double price,
                       Integer duration, Instant createDate, Instant lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.description = description;
    }

    public Certificate(Integer id, String name, String description,
                       double price, Integer duration, Instant createDate,
                       Instant lastUpdateDate, Set<Tag> tagNames) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tagNames = tagNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Certificate)) return false;
        Certificate that = (Certificate) o;
        return Double.compare(that.getPrice(), getPrice()) == 0
                && Objects.equals(getDuration(), that.getDuration())
                && getId().equals(that.getId())
                && getName().equals(that.getName())
                && getDescription().equals(that.getDescription())
                && getCreateDate().equals(that.getCreateDate())
                && getLastUpdateDate().equals(that.getLastUpdateDate())
                && getTagNames().equals(that.getTagNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(),
                getPrice(), getDuration(), getCreateDate(), getLastUpdateDate(), getTagNames());
    }

}
