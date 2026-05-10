package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.models.DisponibilidadTaller;
import com.gearch.gearchbackend.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadTallerRepository extends JpaRepository<DisponibilidadTaller, Long> {

    // Horario completo de un taller (todos sus dias)
    List<DisponibilidadTaller> findByTallerId(Long tallerId);

    // Horario de un taller para un dia concreto
    // Devuelve null si el taller no trabaja ese dia
    DisponibilidadTaller findByTallerIdAndDiaSemana(Long tallerId, DiaSemana diaSemana);
}
