package com.epam.esm.controller;

import com.epam.esm.controller.config.security.jwt.JwtTokenUtil;
import com.epam.esm.controller.config.security.jwt.entity.AuthRequest;
import com.epam.esm.controller.config.security.jwt.entity.JwtResponse;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.dto.entity.UserResponseDto;
import com.epam.esm.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequest.getLogin(), authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok()
                .body(new JwtResponse(jwtTokenUtil.generateToken(userDetails),userDetails.getUsername(),defineRole(userDetails)));
    }
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUpUser(@RequestBody UserDto user) {
        UserResponseDto userDto = userService.create(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    private String defineRole(UserDetails userDetails) {
        Optional<? extends GrantedAuthority> optionalGrantedAuthority = userDetails.getAuthorities().stream()
                .findFirst();
        String role = null;
        if (optionalGrantedAuthority.isPresent()) {
            role = optionalGrantedAuthority.get().getAuthority();
        }
        return role;
    }
}
