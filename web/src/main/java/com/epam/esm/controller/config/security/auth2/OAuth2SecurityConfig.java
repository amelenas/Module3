package com.epam.esm.controller.config.security.auth2;

import com.epam.esm.controller.config.security.jwt.CustomJwtAuthenticationConverter;
import com.epam.esm.controller.config.security.jwt.CustomJwtAuthenticationProvider;
import com.epam.esm.controller.config.security.jwt.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.epam.esm.dao.entity.Role.RoleType.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ROLE = "ROLE_";
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomJwtAuthenticationProvider customJwtAuthenticationProvider;
    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    @Autowired
    public OAuth2SecurityConfig(UserDetailsService userDetailsServiceImpl,
                                JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                CustomJwtAuthenticationProvider customJwtAuthenticationProvider,
                                CustomJwtAuthenticationConverter customJwtAuthenticationConverter) {
        this.userDetailsService = userDetailsServiceImpl;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customJwtAuthenticationProvider = customJwtAuthenticationProvider;
        this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(customJwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().authorities(AuthorityUtils.createAuthorityList(ROLE + GUEST));
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/signup", "/login").hasRole(GUEST.toString())
                .antMatchers(HttpMethod.GET, "/tags/**", "/certificates/**").hasAnyRole(GUEST.toString(), USER.toString(), ADMIN.toString()).and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .logout()
                .logoutUrl("/logout").invalidateHttpSession(true).and()
                .cors().and()
                .csrf().disable()
                .oauth2ResourceServer().jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)
                );
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
