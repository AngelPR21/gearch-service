package com.gearch.gearchbackend.repositories;


import com.gearch.gearchbackend.entities.Cita;
import com.gearch.gearchbackend.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByUsuarioId(Long usuarioId);

    List<Cita> findByTallerId(Long tallerId);

    // Filtra citas de un taller por estado (para que el admin vea solo las PENDIENTES, etc.)
    List<Cita> findByTallerIdAndEstado(Long tallerId, EstadoCita estado);

    // Comprueba si ya existe una cita en ese taller a esa hora (evita duplicados)
    boolean existsByTallerIdAndFechaHora(Long tallerId, LocalDateTime fechaHora);

    // Citas de un taller en un rango de fechas (calendario)
    List<Cita> findByTallerIdAndFechaHoraBetween(Long tallerId, LocalDateTime inicio, LocalDateTime fin);

    // Citas próximas de un usuario (a partir de ahora, ordenadas)
    List<Cita> findByUsuarioIdAndFechaHoraAfterOrderByFechaHoraAsc(Long usuarioId, LocalDateTime ahora);
}
