package com.epam.esm.dao.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@Table(name = "tags")
public class Tag implements Serializable {
    private static final long serialVersionUID = 12L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(min=3, max = 50)
    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "tagNames", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Certificate> certificates;

    public Tag() {}

    public Tag(Integer id) {
        this.id = id;
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(Integer id, String name,  List<Certificate> certificates) {
        this.id = id;
        this.name = name;
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return getId().equals(tag.getId()) && getName().equals(tag.getName())
                && getCertificates().equals(tag.getCertificates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(),
                getCertificates());
    }
}
