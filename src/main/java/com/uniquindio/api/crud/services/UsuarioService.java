package com.uniquindio.api.crud.services;

import com.uniquindio.api.crud.dto.UsuarioDTO;
import com.uniquindio.api.crud.dto.UsuarioResponseDTO;
import com.uniquindio.api.crud.model.RolUsuario;
import com.uniquindio.api.crud.model.Usuario;
import com.uniquindio.api.crud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import javax.swing.*;

@Service
public class UsuarioService{

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    UsuarioRepository usuarioRepository;

    // GET USUARIO POR ID
    public UsuarioResponseDTO findById(Long id) {
        logger.info("Buscando usuario con ID: {}", id);

        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Usuario no encontrado con ID: {}", id);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
                    });
            return convertirDTOResponse(usuario);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al buscar usuario con ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno en el servidor", e);
        }
    }

    // GET TODOS LOS USUARIOS
    public Page<UsuarioResponseDTO> findAll(Pageable pageable) {
        logger.info("Obteniendo usuarios con paginación: {}", pageable);
        try {
            Page<Usuario> usuarios = usuarioRepository.findAll(pageable);

            if (usuarios.isEmpty()) {
                logger.warn("No se encontraron usuarios en la base de datos");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay usuarios registrados");
            }
            return usuarios.map(this::convertirDTOResponse);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al obtener los usuarios", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }



    //DELETE USUARIO
    public void deleteById(Long id) {
        logger.info("Intentando eliminar usuario con ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            logger.warn("No se encontró el usuario con ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
        }

        try {
            usuarioRepository.deleteById(id);
            logger.info("Usuario eliminado correctamente con ID: {}", id);
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar usuario con ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el usuario");
        }
    }


    //POST USUARIOS
    public UsuarioResponseDTO save(Usuario usuario) {
        logger.info("Intentando guardar un nuevo usuario en la base de datos...");

        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        try {
            Usuario guardado = usuarioRepository.save(usuario);
            return convertirDTOResponse(guardado);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar usuario: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }


    //ACTUALIZACION completa Usuarios
    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        if (usuarioRepository.existsByCedula(usuarioDetails.cedula()) && !usuario.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cédula ya está en uso por otro usuario.");
        }

        if (usuarioRepository.existsByEmail(usuarioDetails.email()) && !usuario.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo ya está en uso por otro usuario.");
        }

        usuario.setNombre(usuarioDetails.nombre());
        usuario.setEmail(usuarioDetails.email());
        usuario.setCedula(usuarioDetails.cedula());
        usuario.setRol(RolUsuario.valueOf(usuarioDetails.rol()));
        usuario.setClase(usuarioDetails.clase());

        try {
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return convertirADTO(updatedUsuario);
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar usuario con ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar el usuario");
        }
    }

    //ACTUALIZACION parcial Usuarios
    public UsuarioDTO partialUpdateUsuario(Long id, Map<String, Object> updates) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        if (updates.containsKey("nombre")) {
            usuario.setNombre(String.valueOf(updates.get("nombre")));
        }

        if (updates.containsKey("email")) {
            String newEmail = String.valueOf(updates.get("email"));
            if (usuarioRepository.findByEmail(newEmail).isPresent() && !usuario.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo ya está en uso por otro usuario.");
            }
            usuario.setEmail(newEmail);
        }

        if (updates.containsKey("cedula")) {
            String newCedula = String.valueOf(updates.get("cedula"));
            if (usuarioRepository.findByCedula(newCedula).isPresent() && !usuario.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cédula ya está en uso por otro usuario.");
            }
            usuario.setCedula(newCedula);
        }

        if (updates.containsKey("rol")) {
            try {
                usuario.setRol(RolUsuario.valueOf(String.valueOf(updates.get("rol"))));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de rol no válido.");
            }
        }

        if (updates.containsKey("clase")) {
            usuario.setClase(String.valueOf(updates.get("clase")));
        }

        try {
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return convertirADTO(updatedUsuario);
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar usuario con ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar el usuario");
        }
    }




    private UsuarioDTO convertirADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId() != null ? usuario.getId() : 0L,
                usuario.getNombre() != null ? usuario.getNombre() : "",
                usuario.getCedula() != null ? usuario.getCedula() : "",
                usuario.getEmail() != null ? usuario.getEmail() : "",
                usuario.getRol() != null ? usuario.getRol().toString() : "ESTUDIANTE", // Asigna un valor por defecto
                usuario.getClase() != null ? usuario.getClase() : "",
                usuario.getClave() != null ? usuario.getClave() : ""
        );
    }

    private UsuarioResponseDTO convertirDTOResponse(Usuario usuario) {

        return new UsuarioResponseDTO(
                usuario.getId() != null ? usuario.getId() : 0L,
                usuario.getNombre() != null ? usuario.getNombre() : "",
                usuario.getEmail() != null ? usuario.getEmail() : "",
                usuario.getClase() != null ? usuario.getClase() : ""

        );
    }

}
