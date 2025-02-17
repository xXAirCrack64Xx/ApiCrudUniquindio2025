package com.uniquindio.api.crud.dto;

import com.uniquindio.api.crud.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UsuarioDTO(


        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String nombre,

        @NotBlank(message = "La cédula es obligatoria")
        @Size(max = 10, message = "La cédula debe tener máximo 10 dígitos")
        @Pattern(regexp = "^\\d+$", message = "La cédula solo puede contener números")
        String cedula,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        @Size(max = 50, message = "El correo no puede superar los 50 caracteres")
        String email,

        @Pattern(regexp = "ESTUDIANTE|PROFESOR", message = "El rol solo puede ser 'ESTUDIANTE' o 'PROFESOR'")
        @NotNull(message = "El rol es obligatorio")
        String rol,

        @Size(max = 50, message = "El nombre de la clase no puede superar los 50 caracteres")
        String clase,

        @Schema(description = "contraseña del usuario", example = "123")
        @Size (max = 20, min = 5, message = "solo se pueden claves entre 5 y 20 caracteres")
        @NotBlank (message = "la contraseña es obligatoria")
        String clave



) {




}
