package com.uniquindio.api.crud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Schema(description="Clase para manejar errores personalizados")
public class ErrorResponse {

    @Schema(description = "estado de la solicitud", example = "200")
    private int status;

    @Schema(description = "Mensaje detallado de la solicitud", example = "Se obtuvieron los usuarios de forma exitosa")
    private String message;


}

