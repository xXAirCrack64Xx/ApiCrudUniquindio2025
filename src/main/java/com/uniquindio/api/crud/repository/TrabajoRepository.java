package com.uniquindio.api.crud.repository;

import com.uniquindio.api.crud.model.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {
    // Obtiene los trabajos enviados por un estudiante
    List<Trabajo> findByEstudianteId(Long estudianteId);

    // Obtiene los trabajos calificados por un profesor
    List<Trabajo> findByProfesorId(Long profesorId);
}
