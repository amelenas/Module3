package com.epam.esm.service.dto.entity;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {

    @NotNull
    @Size(min=3, max = 50)
    private String login;

    @NotNull
    @Size(min=3, max = 50)
    private String password;
}
