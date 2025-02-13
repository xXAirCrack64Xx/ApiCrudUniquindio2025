package com.uniquindio.api.crud.model;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario (estudiante o profesor)")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@uniquindio.edu")
    private String email;

    @Schema(description = "Rol del usuario (PROFESOR o ESTUDIANTE)", example = "ESTUDIANTE")
    private String rol;

    @Schema(description = "Clase a la que pertenece el usuario", example = "Programación I")
    private String clase;

    // Constructores, getters y setters
    public Usuario() { }

    public Usuario(String nombre, String email, String rol, String clase) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.clase = clase;
    }


    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
}
