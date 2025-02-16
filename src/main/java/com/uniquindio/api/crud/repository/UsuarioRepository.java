package com.uniquindio.api.crud.repository;

import com.uniquindio.api.crud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCedula(String cedula);
    boolean existsByEmail(String email);
    Optional<Usuario> findByCedula(String cedula);
    Optional<Usuario> findByEmail(String email);
}
