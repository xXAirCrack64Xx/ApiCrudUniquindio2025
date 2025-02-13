package com.uniquindio.api.crud.repository;

import com.uniquindio.api.crud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aquí puedes agregar métodos de consulta personalizados si es necesario.
}
