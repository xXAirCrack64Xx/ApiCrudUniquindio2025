package com.uniquindio.api.crud.controller;

import com.uniquindio.api.crud.exception.ResourceNotFoundException;
import com.uniquindio.api.crud.model.Usuario;
import com.uniquindio.api.crud.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios: creación, consulta, actualización y eliminación.")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Listar todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados.")
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Operation(summary = "Obtener un usuario por ID", description = "Recupera la información de un usuario dado su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(
            @Parameter(description = "ID del usuario a consultar", required = true)
            @PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un usuario nuevo en el sistema.")
    @PostMapping
    public Usuario createUsuario(
            @Parameter(description = "Objeto JSON con los datos del nuevo usuario", required = true)
            @RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Operation(summary = "Actualizar un usuario existente", description = "Actualiza los datos de un usuario existente dado su ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Objeto JSON con los nuevos datos del usuario", required = true)
            @RequestBody Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setRol(usuarioDetails.getRol());
        usuario.setClase(usuarioDetails.getClase());
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema dado su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true)
            @PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}
