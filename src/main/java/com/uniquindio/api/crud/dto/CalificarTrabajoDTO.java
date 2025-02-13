package com.uniquindio.api.crud.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que representa la información necesaria para calificar un trabajo")
public class CalificarTrabajoDTO {

    @Schema(description = "ID del profesor que califica el trabajo", example = "1", required = true)
    private Long profesorId;

    @Schema(description = "Calificación asignada", example = "95.0", required = true)
    private Double calificacion;

    public CalificarTrabajoDTO() { }

    public CalificarTrabajoDTO(Long profesorId, Double calificacion) {
        this.profesorId = profesorId;
        this.calificacion = calificacion;
    }

    // Getters y setters...


    public Long getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Long profesorId) {
        this.profesorId = profesorId;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }
}
