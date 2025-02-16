package com.uniquindio.api.crud.services;

import com.uniquindio.api.crud.model.RolUsuario;
import com.uniquindio.api.crud.model.Usuario;
import com.uniquindio.api.crud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class UsuarioService{

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    UsuarioRepository usuarioRepository;

    //GET USUARIO POR ID
    public Usuario findById(Long id) {
        logger.info("Buscando usuario con ID: {}", id);

        try {
            return usuarioRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Usuario no encontrado con ID: {}", id);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
                    });
        } catch (ResponseStatusException e) {
            throw e;  // Mantenemos la excepción capturada.
        } catch (Exception e) {
            logger.error("Error interno al buscar usuario con ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno en el servidor", e);
        }
    }

    //GET TODOS LOS USUARIOS
    public List<Usuario> findAll() {
        logger.info("Obteniendo todos los usuarios");

        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            if (usuarios.isEmpty()) {
                logger.warn("No se encontraron usuarios en la base de datos");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay usuarios registrados");
            }
            return usuarios;
        }catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Error al obtener los usuarios", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    //DELETE USUARIO
    public void deleteById(Long id) {
        logger.info("Intentando eliminar usuario con ID: {}", id);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isEmpty()) {
            logger.warn("No se encontró el usuario con ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id);
        }
        try {
            usuarioRepository.deleteById(id);
            logger.info("Usuario eliminado correctamente con ID: {}", id);
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar usuario con ID: {} - {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el usuario");
        }
    }

    //POST USUARIOS
    public Usuario save(Usuario usuario) {
        logger.info("Intentando guardar un nuevo usuario en la base de datos...");

        // Verificar si cedula o correo ya existen
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            logger.warn("Intento de crear usuario con cédula duplicada: {}", usuario.getCedula());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cédula ya está registrada");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            logger.warn("Intento de crear usuario con correo duplicado: {}", usuario.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        try {
            Usuario guardado = usuarioRepository.save(usuario);
            logger.info("Usuario guardado con éxito: {}", guardado.getNombre());
            return guardado;

        } catch (DataIntegrityViolationException e) {
            logger.warn("Error al guardar usuario: Datos inválidos o duplicados.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos inválidos o duplicados");

        } catch (Exception e) {
            logger.error("Error inesperado al guardar usuario: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }

    //ACTUALIZACION completa Usuarios
    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        logger.info("Buscando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        logger.info("Verificando unicidad de cédula: {}", usuarioDetails.getCedula());
        Optional<Usuario> usuarioPorCedula = usuarioRepository.findByCedula(usuarioDetails.getCedula());
        if (usuarioPorCedula.isPresent() && !usuarioPorCedula.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cédula ya está en uso por otro usuario.");
        }

        logger.info("Verificando unicidad de correo: {}", usuarioDetails.getEmail());
        Optional<Usuario> usuarioPorCorreo = usuarioRepository.findByEmail(usuarioDetails.getEmail());
        if (usuarioPorCorreo.isPresent() && !usuarioPorCorreo.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo ya está en uso por otro usuario.");
        }

        logger.info("Actualizando datos del usuario con ID: {}", id);
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setCedula(usuarioDetails.getCedula());
        usuario.setRol(usuarioDetails.getRol());
        usuario.setClase(usuarioDetails.getClase());

        try {
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            logger.info("Usuario con ID {} actualizado correctamente.", id);
            return updatedUsuario;
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar usuario con ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar el usuario");
        }
    }

    //ACTUALIZACION parcial Usuarios
    public Usuario partialUpdateUsuario(Long id, Map<String, Object> updates) {
        logger.info("Buscando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        updates.forEach((key, value) -> {
            logger.info("Actualizando campo: {} con valor: {}", key, value);

            switch (key) {
                case "nombre":
                    usuario.setNombre((String) value);
                    break;
                case "email":
                    String newEmail = (String) value;
                    Optional<Usuario> usuarioPorCorreo = usuarioRepository.findByEmail(newEmail);
                    if (usuarioPorCorreo.isPresent() && !usuarioPorCorreo.get().getId().equals(id)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo ya está en uso por otro usuario.");
                    }
                    usuario.setEmail(newEmail);
                    break;
                case "cedula":
                    String newCedula = (String) value;
                    Optional<Usuario> usuarioPorCedula = usuarioRepository.findByCedula(newCedula);
                    if (usuarioPorCedula.isPresent() && !usuarioPorCedula.get().getId().equals(id)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Cédula ya está en uso por otro usuario.");
                    }
                    usuario.setCedula(newCedula);
                    break;
                case "rol":
                    usuario.setRol(RolUsuario.valueOf((String) value));
                    break;
                case "clase":
                    usuario.setClase((String) value);
                    break;
                default:
                    logger.warn("Campo desconocido: {} - Se ignora.", key);
            }
        });

        try {
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            logger.info("Usuario con ID {} actualizado parcialmente.", id);
            return updatedUsuario;
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar usuario con ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar el usuario");
        }
    }

}
