package com.epam.esm.controller.config.security.jwt.entity;

public class JwtResponse {
    private final String token;
    private final String username;
    private final String role;

    public JwtResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return this.getClass() +" {" + "token='" + token +
                '}';
    }
}
