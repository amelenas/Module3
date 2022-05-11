package com.epam.esm.dao.entity;

import com.epam.esm.dao.entity.audit.AuditCertificateListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@EntityListeners(AuditCertificateListener.class)
@Table(name = "gift_certificates")
public class Certificate implements Serializable {

    private static final long serialVersionUID = 11L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    private int duration;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "last_update_date")
    private Instant lastUpdateDate;

    @Column(name = "lock_certificate")
    private Integer lock;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "gift_certificate_tags", joinColumns = {@JoinColumn(name = "gift_certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tagNames;

    public Certificate() {
    }

    public Certificate(long id) {
        this.id = id;
    }

    public Certificate(long id, String name,String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;

    }

    public Certificate(String name,String description, double price, int duration, Set<Tag> tagNames) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.tagNames = tagNames;
        this.duration = duration;
    }

    public Certificate(long id, String name, String description,
                        double price, int duration,
                       Set<Tag> tagNames) {

       this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.tagNames = tagNames;
        this.duration = duration;
    }

    public Certificate(String name, String description, double price,
                       int duration, Instant createDate, Instant lastUpdateDate) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.description = description;
    }

    public Certificate(long id, String name, String description, double price,
                       int duration, Instant createDate, Instant lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.description = description;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Set<Tag> getTagNames() {
        return tagNames;
    }

    public void setTagNames(Set<Tag> tagNames) {
        this.tagNames = tagNames;
    }

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return this.getClass() +
                ", id = " + id +
                ", name = " + name +
                ", description = " + description +
                ", price = " + price +
                ", duration = " + duration +
                ", createDate = " + createDate +
                ", lastUpdateDate = " + lastUpdateDate +
                ", lock = " + lock;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Certificate)) return false;
        Certificate that = (Certificate) o;
        return getId() == that.getId() && Double.compare(that.getPrice(), getPrice()) == 0 && getDuration() == that.getDuration() && getName().equals(that.getName()) && getDescription().equals(that.getDescription()) && getCreateDate().equals(that.getCreateDate()) && getLastUpdateDate().equals(that.getLastUpdateDate()) && getLock().equals(that.getLock()) && getTagNames().equals(that.getTagNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getPrice(), getDuration(), getCreateDate(), getLastUpdateDate(), getLock(), getTagNames());
    }
}
