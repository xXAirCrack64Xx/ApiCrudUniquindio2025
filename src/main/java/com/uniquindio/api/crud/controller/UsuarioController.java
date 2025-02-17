package com.uniquindio.api.crud.controller;

import com.uniquindio.api.crud.dto.ErrorResponse;
import com.uniquindio.api.crud.dto.UsuarioDTO;
import com.uniquindio.api.crud.dto.UsuarioResponseDTO;
import com.uniquindio.api.crud.model.RolUsuario;
import com.uniquindio.api.crud.services.UsuarioService;
import com.uniquindio.api.crud.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios: creación, consulta, actualización y eliminación.")
@RestController
@RequestMapping("/api/usuarios") //Rama Andres
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    @Operation(
            summary = "Listar todos los usuarios",
            description = "Devuelve una lista de todos los usuarios registrados en el sistema. " +
                    "Si no hay usuarios, retorna un estado 204 No Content."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"codigo\": 204, \"mensaje\": \"No hay usuarios registrados en el sistema.\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"codigo\": 500, \"mensaje\": \"Error interno del servidor. Inténtelo nuevamente más tarde.\"}")))
    })
    @GetMapping
    public ResponseEntity<?> getAllUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Solicitud recibida para obtener usuarios paginados - Página: {}, Tamaño: {}", page, size);
        try {
            Page<UsuarioResponseDTO> usuarios = usuarioService.findAll(PageRequest.of(page, size));

            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ErrorResponse(204, "No hay usuarios registrados"));
            }
            return ResponseEntity.ok(usuarios);
        } catch (ResponseStatusException e) {
            logger.warn("Error en la obtención de usuarios: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ErrorResponse(e.getStatusCode().value(), e.getReason()));
        } catch (Exception ex) {
            logger.error("Error inesperado al obtener los usuarios", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error interno del servidor"));
        }
    }



    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Recupera la información de un usuario dado su ID. " +
                    "Si el ID no es válido o el usuario no existe, devuelve un mensaje de error."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido (no es un número válido)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 400,
                                  "mensaje": "El ID debe ser un número válido"
                                }
                                """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 404,
                                  "mensaje": "Usuario no encontrado con ID: 99"
                                }
                                """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 500,
                                  "mensaje": "Error interno del servidor. Inténtelo nuevamente más tarde."
                                }
                                """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(
            @Parameter(description = "ID del usuario a consultar", required = true, example = "1")
            @PathVariable String id) {

        logger.info("Solicitud recibida para obtener usuario con ID: {}", id);

        try {
            Long userId = Long.parseLong(id);
            UsuarioResponseDTO usuario = usuarioService.findById(userId);

            return ResponseEntity.ok(usuario);  // 200 OK con UsuarioDTO

        } catch (NumberFormatException e) {
            logger.warn("ID inválido recibido: {}", id);
            return ResponseEntity.badRequest().body(new ErrorResponse(400, "El ID debe ser un número válido"));
        } catch (ResponseStatusException e) {
            logger.warn("Error al buscar usuario: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ErrorResponse(e.getStatusCode().value(), e.getReason()));
        } catch (Exception ex) {
            logger.error("Error inesperado al obtener el usuario con ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error interno del servidor"));
        }
    }




    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Crea un usuario nuevo en el sistema. " +
                    "El usuario debe proporcionar información válida y cumplir con las restricciones establecidas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (datos incorrectos o incompletos)"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (datos incorrectos o incompletos)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 400,
                                  "mensaje": "El campo 'cédula' es obligatorio"
                                }
                                """))),
            @ApiResponse(responseCode = "409", description = "Conflicto (usuario con cédula o correo ya registrado)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 409,
                                  "mensaje": "Ya existe un usuario registrado con el correo juan.perez@uniquindio.edu"
                                }
                                """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 500,
                                  "mensaje": "Error interno del servidor. Inténtelo nuevamente más tarde."
                                }
                                """)))
    })
    @PostMapping

    public ResponseEntity<?> createUsuario(
            @Parameter(description = "Datos del usuario a crear", required = true, schema = @Schema(implementation = UsuarioDTO.class))
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        logger.info("Solicitud recibida para crear un nuevo usuario: {}", usuarioDTO.nombre());

        try {
            // Convertir UsuarioDTO a Usuario
            Usuario usuario = new Usuario(
                    null, // Se asume que el ID es autogenerado
                    usuarioDTO.nombre(),
                    usuarioDTO.cedula(),
                    usuarioDTO.email(),
                    RolUsuario.valueOf(usuarioDTO.rol()),
                    usuarioDTO.clase(),
                    usuarioDTO.clave()
            );

            // Guardar usuario en la base de datos y recibir el DTO resultante
            UsuarioResponseDTO usuarioGuardadoDTO = usuarioService.save(usuario);

            logger.info("Usuario creado exitosamente con ID: {}", usuarioGuardadoDTO.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardadoDTO); // 201 Created

        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse(409, e.getReason()));
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(400, e.getReason()));
            } else {
                logger.error("Error interno al crear usuario", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Error interno del servidor"));
            }
        }
    }




    @Operation(
            summary = "Actualizar un usuario existente",
            description = "Modifica los datos de un usuario registrado en el sistema dado su ID. " +
                    "Se debe proporcionar un objeto JSON con los nuevos valores."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (datos incorrectos o incompletos)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 400,
                                  "mensaje": "El campo 'email' no es válido"
                                }
                                """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID especificado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 404,
                                  "mensaje": "Usuario no encontrado con ID: 99"
                                }
                                """))),
            @ApiResponse(responseCode = "409", description = "Conflicto (usuario con cédula o correo ya registrado)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 409,
                                  "mensaje": "Ya existe un usuario con el correo juan.perez@uniquindio.edu"
                                }
                                """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 500,
                                  "mensaje": "Error interno al actualizar el usuario"
                                }
                                """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Objeto JSON con los nuevos datos del usuario", required = true, schema = @Schema(implementation = UsuarioDTO.class))
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        logger.info("Solicitud recibida para actualizar usuario con ID: {}", id);

        try {
            UsuarioDTO usuarioActualizadoDTO = usuarioService.updateUsuario(id, usuarioDTO);
            logger.info("Usuario con ID {} actualizado correctamente.", id);
            return ResponseEntity.ok(usuarioActualizadoDTO); // 200 OK

        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Usuario con ID {} no encontrado.", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(404, "Usuario no encontrado con ID: " + id));
            } else if (e.getStatusCode() == HttpStatus.CONFLICT) {
                logger.warn("Conflicto al actualizar usuario con ID {}: {}", id, e.getReason());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse(409, e.getReason()));
            } else {
                logger.error("Error interno al actualizar usuario con ID {}: {}", id, e.getReason());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Error interno al actualizar el usuario"));
            }
        }
    }


    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina un usuario del sistema dado su ID. Si el usuario no existe, devuelve un error 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID especificado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 404,
                                  "mensaje": "Usuario no encontrado con ID: 99"
                                }
                                """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 500,
                                  "mensaje": "Error interno al intentar eliminar el usuario"
                                }
                                """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id) {

        logger.info("Solicitud recibida para eliminar usuario con ID: {}", id);

        try {
            usuarioService.deleteById(id);
            logger.info("Usuario con ID {} eliminado exitosamente.", id);
            return ResponseEntity.noContent().build(); // 204 No Content

        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Usuario con ID {} no encontrado.", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(404, "Usuario no encontrado con ID: " + id));
            } else {
                logger.error("Error interno al eliminar usuario con ID {}: {}", id, e.getReason());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Error interno al intentar eliminar el usuario"));
            }
        }
    }


    @Operation(
            summary = "Actualizar parcialmente un usuario",
            description = "Permite actualizar uno o varios campos de un usuario existente dado su ID. " +
                    "Solo los campos proporcionados en el cuerpo de la solicitud serán modificados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(value = """
                                {
                                  "nombre": "Nuevo Nombre",
                                  "cedula": "1234567890",
                                  "email": "nuevo.email@example.com",
                                  "rol": "ESTUDIANTE",
                                  "clase": "Programación II"
                                }
                                """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID especificado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 404,
                                  "mensaje": "Usuario no encontrado con ID: 99"
                                }
                                """))),
            @ApiResponse(responseCode = "409", description = "Conflicto al actualizar el usuario",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 409,
                                  "mensaje": "Ya existe un usuario registrado con el correo juan.perez@uniquindio.edu"
                                }
                                """))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "codigo": 500,
                                  "mensaje": "Error interno al actualizar el usuario"
                                }
                                """)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Campos a actualizar en formato JSON. Solo los campos enviados serán modificados.",
                    required = true,
                    example = "{ \"nombre\": \"Nuevo Nombre\", \"email\": \"nuevo.email@example.com\" }")
            @RequestBody Map<String, Object> updates) {

        logger.info("Solicitud PATCH recibida para actualizar usuario con ID: {}", id);

        try {
            UsuarioDTO updatedUsuario = usuarioService.partialUpdateUsuario(id, updates);
            logger.info("Usuario con ID {} actualizado parcialmente.", id);
            return ResponseEntity.ok(updatedUsuario);

        } catch (ResponseStatusException e) {
            HttpStatus status = (HttpStatus) e.getStatusCode();
            logger.warn("Error al actualizar usuario con ID {}: {}", id, e.getReason());
            return ResponseEntity.status(status).body(new ErrorResponse(status.value(), e.getReason()));
        } catch (Exception e) {
            logger.error("Error interno al actualizar usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error interno al actualizar el usuario"));
        }
    }



}
