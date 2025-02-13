package com.uniquindio.api.crud.model;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Entity
@Table(name = "trabajos")
@Schema(description = "Entidad que representa un trabajo enviado por un estudiante, con la posibilidad de ser calificado por un profesor.")
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del trabajo", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Título del trabajo", example = "Implementación de CRUD en Spring Boot", required = true)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descripción o contenido del trabajo (por ejemplo, código o explicación detallada)", example = "Este trabajo muestra la implementación de un CRUD con Spring Boot...", required = true)
    private String descripcion;

    @Schema(description = "Calificación asignada por el profesor. Puede ser nula si aún no se ha calificado.", example = "85.0", nullable = true)
    private Double calificacion;

    @Schema(description = "Fecha y hora en que se envió el trabajo", example = "2025-02-01T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaEnvio;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    @Schema(description = "El estudiante que envió el trabajo. Debe ser un usuario con rol 'ESTUDIANTE'.", required = true)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    @Schema(description = "El profesor que calificó el trabajo. Es nulo si el trabajo aún no ha sido calificado.", nullable = true)
    private Usuario profesor;

    // Constructor vacío (requerido por JPA)
    public Trabajo() { }

    // Constructor con parámetros (sin incluir calificación ni profesor, que se asignan posteriormente)
    public Trabajo(String titulo, String descripcion, LocalDateTime fechaEnvio, Usuario estudiante) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaEnvio = fechaEnvio;
        this.estudiante = estudiante;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Usuario getProfesor() {
        return profesor;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }
}

