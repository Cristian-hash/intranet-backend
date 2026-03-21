package com.educativa.intranet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String rol;
    private String nombre;
}
