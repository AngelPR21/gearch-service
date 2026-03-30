package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.entities.DisponibilidadTaller;
import com.gearch.gearchbackend.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadTallerRepository extends JpaRepository<DisponibilidadTaller, Long> {
    List<DisponibilidadTaller> findByTallerId(Long tallerId);
    DisponibilidadTaller findByTallerIdAndDiaSemana(Long tallerId, DiaSemana diaSemana);
    boolean existsByTallerIdAndDiaSemana(Long tallerId, DiaSemana diaSemana);
}
