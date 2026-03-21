package com.educativa.intranet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "Password es obligatorio")
    private String password;
}
