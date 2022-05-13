package com.epam.esm.dao.entity;

import com.epam.esm.dao.entity.audit.AuditUserListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@EntityListeners(AuditUserListener.class)
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 196L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @NotNull
    @Size(min=3, max = 50)
    @Column(name = "user_name")
    private String userName;


    @Column(name = "lock_user")
    private Integer lock;

    public User() {
    }

    public User(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId() == user.getUserId() && getUserName().equals(user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUserName());
    }

    @Override
    public String toString() {
        return this.getClass() +
                ", userId = " + userId +
                ", userName = " + userName;
    }
}

