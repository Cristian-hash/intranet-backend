package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDTO {
    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    private String nuevaPassword;
}
