package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.models.Cita;
import com.gearch.gearchbackend.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Spring genera automaticamente las queries a partir del nombre de los metodos
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Todas las citas de un usuario
    List<Cita> findByUsuarioId(Long usuarioId);

    // Todas las citas de un taller
    List<Cita> findByTallerId(Long tallerId);

    // Citas de un taller filtradas por estado
    List<Cita> findByTallerIdAndEstado(Long tallerId, EstadoCita estado);

    // Comprueba si ya existe una cita en ese taller a esa hora exacta
    boolean existsByTallerIdAndFechaHora(Long tallerId, LocalDateTime fechaHora);

    // Citas de un taller en un rango de fechas (usado para calcular horas libres)
    List<Cita> findByTallerIdAndFechaHoraBetween(Long tallerId, LocalDateTime inicio, LocalDateTime fin);

    // Proximas citas de un usuario ordenadas por fecha ascendente
    List<Cita> findByUsuarioIdAndFechaHoraAfterOrderByFechaHoraAsc(Long usuarioId, LocalDateTime ahora);
}
