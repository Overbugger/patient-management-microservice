package com.endpoint.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private final String token;

}
