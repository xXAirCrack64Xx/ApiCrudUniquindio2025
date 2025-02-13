package com.uniquindio.api.crud.controller;

import com.uniquindio.api.crud.dto.CalificarTrabajoDTO;
import com.uniquindio.api.crud.exception.ResourceNotFoundException;
import com.uniquindio.api.crud.model.Trabajo;
import com.uniquindio.api.crud.model.Usuario;
import com.uniquindio.api.crud.repository.TrabajoRepository;
import com.uniquindio.api.crud.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Trabajos", description = "Endpoints para gestionar trabajos: envío y calificación.")
@RestController
@RequestMapping("/api/trabajos")
public class TrabajoController {

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Listar todos los trabajos", description = "Devuelve una lista de todos los trabajos enviados.")
    @GetMapping
    public List<Trabajo> getAllTrabajos() {
        return trabajoRepository.findAll();
    }

    @Operation(summary = "Obtener un trabajo por ID", description = "Recupera la información de un trabajo dado su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Trabajo> getTrabajoById(
            @Parameter(description = "ID del trabajo a consultar", required = true)
            @PathVariable Long id) {
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado con id: " + id));
        return ResponseEntity.ok(trabajo);
    }

    @Operation(summary = "Crear un nuevo trabajo", description = "Permite a un estudiante enviar un trabajo. Se requiere indicar el ID del estudiante en el objeto JSON.")
    @PostMapping
    public ResponseEntity<Trabajo> createTrabajo(
            @Parameter(description = "Objeto JSON con los datos del trabajo a crear (debe incluir el ID del estudiante)", required = true)
            @RequestBody Trabajo trabajo) {
        if (trabajo.getEstudiante() == null || trabajo.getEstudiante().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Verificar que el usuario sea un estudiante
        Usuario estudiante = usuarioRepository.findById(trabajo.getEstudiante().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + trabajo.getEstudiante().getId()));
        if (!"ESTUDIANTE".equalsIgnoreCase(estudiante.getRol())) {
            return ResponseEntity.badRequest().build();
        }
        trabajo.setEstudiante(estudiante);
        trabajo.setFechaEnvio(LocalDateTime.now());
        Trabajo trabajoGuardado = trabajoRepository.save(trabajo);
        return ResponseEntity.ok(trabajoGuardado);
    }

    @Operation(summary = "Obtener los trabajos de un estudiante", description = "Devuelve todos los trabajos enviados por un estudiante dado su ID.")
    @GetMapping("/estudiante/{estudianteId}")
    public List<Trabajo> getTrabajosByEstudiante(
            @Parameter(description = "ID del estudiante", required = true)
            @PathVariable Long estudianteId) {
        return trabajoRepository.findByEstudianteId(estudianteId);
    }

    @Operation(summary = "Obtener los trabajos calificados por un profesor", description = "Devuelve todos los trabajos calificados por un profesor dado su ID.")
    @GetMapping("/profesor/{profesorId}")
    public List<Trabajo> getTrabajosByProfesor(
            @Parameter(description = "ID del profesor", required = true)
            @PathVariable Long profesorId) {
        return trabajoRepository.findByProfesorId(profesorId);
    }

    @Operation(summary = "Calificar un trabajo", description = "Permite que un profesor califique un trabajo. Se envía un objeto JSON con el ID del profesor y la calificación.")
    @PutMapping("/{id}/calificar")
    public ResponseEntity<Trabajo> calificarTrabajo(
            @Parameter(description = "ID del trabajo a calificar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Objeto JSON que contiene el ID del profesor y la calificación", required = true)
            @RequestBody CalificarTrabajoDTO calificarDTO) {
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado con id: " + id));
        Usuario profesor = usuarioRepository.findById(calificarDTO.getProfesorId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con id: " + calificarDTO.getProfesorId()));
        if (!"PROFESOR".equalsIgnoreCase(profesor.getRol())) {
            return ResponseEntity.badRequest().build();
        }
        trabajo.setCalificacion(calificarDTO.getCalificacion());
        trabajo.setProfesor(profesor);
        Trabajo trabajoActualizado = trabajoRepository.save(trabajo);
        return ResponseEntity.ok(trabajoActualizado);
    }
}

