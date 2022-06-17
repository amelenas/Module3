package com.epam.esm.dao.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
@Entity
@Getter
@Setter
@ToString
@Builder
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 196L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Size(min=3, max = 50)
    @Column(name = "user_name")
    private String userName;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User(Integer userId, String userName, String login, String password, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.login = login;
        this.password = password;
        this.role = role;
    }
    public User(String userName, String login, String password,  Role role) {
        this.userName = userName;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String userName, String login, String password) {
        this.userName = userName;
        this.login = login;
        this.password = password;
    }
    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId().equals(user.getUserId()) && getUserName().equals(user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUserName());
    }
}

