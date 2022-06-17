package com.epam.esm.service.dto.entity;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDto {

    @NotNull
    @Size(min=3, max = 50)
    private String login;

    private String token;
}
