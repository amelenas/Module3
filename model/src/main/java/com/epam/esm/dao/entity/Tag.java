package com.epam.esm.dao.entity;

import com.epam.esm.dao.entity.audit.AuditTagListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@EntityListeners(AuditTagListener.class)
@Table(name = "tags")
public class Tag implements Serializable {

    private static final long serialVersionUID = 12L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Size(min=3, max = 50)
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "lock_tag")
    private Integer lock;

    @ManyToMany(mappedBy = "tagNames", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Certificate> certificates;

    public Tag() {}

    public Tag(long id) {
        this.id = id;
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(long id, String name,  List<Certificate> certificates) {
        this.id = id;
        this.name = name;
        this.certificates = certificates;
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

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return this.getClass()+
                ", id = " + id +
                ", name = " + name +
                ", gifts = " + certificates +
                ", lock = " + lock;
    }
}
