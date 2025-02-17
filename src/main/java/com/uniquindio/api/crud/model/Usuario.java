package com.uniquindio.api.crud.model;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario (estudiante o profesor)")
@Data
public class Usuario {

    /**
     * Identificador único del usuario, autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    /**
     * Nombre completo del usuario.
     * Debe contener entre 3 y 50 caracteres y no puede estar vacío.
     */
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    /**
     * Número de cédula del usuario, debe ser único.
     * Solo puede contener hasta 10 dígitos numéricos.
     */
    @Schema(description = "Cédula del usuario", example = "1234567890")
    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 10, message = "La cédula debe tener máximo 10 dígitos")
    @Pattern(regexp = "^\\d+$", message = "La cédula solo puede contener números")
    @Column(unique = true, nullable = false)
    private String cedula;

    /**
     * Correo electrónico del usuario, debe ser único y tener un formato válido.
     */
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@uniquindio.edu")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 50, message = "El correo no puede superar los 50 caracteres")
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Rol del usuario dentro del sistema.
     * Puede ser PROFESOR o ESTUDIANTE.
     */
    @Schema(description = "Rol del usuario (PROFESOR o ESTUDIANTE)", example = "ESTUDIANTE")
    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    /**
     * Clase a la que pertenece el usuario.
     * Es opcional y no puede superar los 50 caracteres.
     */
    @Schema(description = "Clase a la que pertenece el usuario", example = "Programación I")
    @Size(max = 50, message = "El nombre de la clase no puede superar los 50 caracteres")
    private String clase;

    @Schema(description = "contraseña del usuario", example = "123")
    @Size (max = 20, min = 5, message = "solo se pueden claves entre 5 y 20 caracteres")
    @NotBlank (message = "la contraseña es obligatoria")
    private String clave;

    public Usuario(Long id, String nombre, String cedula, String email, RolUsuario rolUsuario, String clase, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.rol = rolUsuario;
        this.clase = clase;
        this.clave = clave;
    }
    // Constructor por defecto (requerido por Hibernate)
    public Usuario() {
    }
}

