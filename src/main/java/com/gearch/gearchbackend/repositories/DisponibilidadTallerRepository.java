package com.gearch.gearchbackend.repositories;


import com.gearch.gearchbackend.entities.DisponibilidadTaller;
import com.gearch.gearchbackend.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibilidadTallerRepository extends JpaRepository<DisponibilidadTaller, Long> {

    // Todos los días configurados de un taller
    List<DisponibilidadTaller> findByTallerId(Long tallerId);

    // Horario de un taller para un día concreto
    Optional<DisponibilidadTaller> findByTallerIdAndDiaSemana(Long tallerId, DiaSemana diaSemana);

    // Comprobar si ya existe ese día configurado para ese taller
    boolean existsByTallerIdAndDiaSemana(Long tallerId, DiaSemana diaSemana);
}
