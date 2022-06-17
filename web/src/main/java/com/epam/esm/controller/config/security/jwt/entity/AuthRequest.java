package com.epam.esm.controller.config.security.jwt.entity;

public class AuthRequest {
    private String login;
    private String password;


    @Override
    public String toString() {
        return this.getClass() + "{" + "username='" + login +
                ", password='" + password +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

