package com.epam.esm.controller.config.security.jwt;

import com.epam.esm.dao.entity.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String ROLE = "ROLE_";
    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        if (!Objects.requireNonNull(grantedAuthorities).isEmpty()) {
            return new JwtAuthenticationToken(jwt, grantedAuthorities);
        } else {
             return new JwtAuthenticationToken(jwt, createAuthorityList(ROLE + Role.RoleType.USER));
        }
    }
}
