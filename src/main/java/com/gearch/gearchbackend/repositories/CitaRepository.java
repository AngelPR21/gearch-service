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
    List<Cita> findByTallerIdAndEstado(Long tallerId, EstadoCita estado);
    boolean existsByTallerIdAndFechaHora(Long tallerId, LocalDateTime fechaHora);
    List<Cita> findByTallerIdAndFechaHoraBetween(Long tallerId, LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findByUsuarioIdAndFechaHoraAfterOrderByFechaHoraAsc(Long usuarioId, LocalDateTime ahora);
}
